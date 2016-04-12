package app.activities;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import app.entities.NetworkProperties;
import app.entities.SysApplication;

import com.example.soosokanv3.R;

public class RegisterActivity extends Activity {

	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/seller/add";
	public final static int REFRESH_LOCATION = 0x0100;

	public static final String TAG = "REGISTER";

	private LocationManager lm;
	private Location location;
	private String provider;
	String longtitude;
	String latitude;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		setContentView(R.layout.activity_register);
	}

	private boolean isValidEmail(String mail) {
		Pattern pattern = Pattern
				.compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
		Matcher matcher = pattern.matcher(mail);
		return matcher.matches();
	}

	public void Register(View vw) {

		EditText edFirstName = (EditText) findViewById(R.id.Firstname_editText);
		EditText edTelphone = (EditText) findViewById(R.id.Phoneno_editText);
		EditText edEmail = (EditText) findViewById(R.id.Email_editText);
		EditText edAddress = (EditText) findViewById(R.id.Selleraddress_editText);
		EditText edPassword_1 = (EditText) findViewById(R.id.Password_1_editText);
		EditText edPassword_2 = (EditText) findViewById(R.id.Password_2_editText);

		String firstName = edFirstName.getText().toString();
		String telphone = edTelphone.getText().toString();
		String email = edEmail.getText().toString();
		String address = edAddress.getText().toString();
		String password_1 = edPassword_1.getText().toString();
		String password_2 = edPassword_2.getText().toString();

		if (firstName.equals("") || telphone.equals("") || email.equals("")
				|| address.equals("") || password_1.equals("")
				|| password_2.equals("")) {
			Toast.makeText(this, "Please enter in all required fields.",
					Toast.LENGTH_LONG).show();
			return;
		} else if (isValidEmail(email) == false) {
			Toast.makeText(this, "Email address is not Valid.",
					Toast.LENGTH_LONG).show();
			return;

		} else if (!password_1.equals(password_2)) {
			Toast.makeText(this, "Password does not match.", Toast.LENGTH_LONG)
					.show();
			return;
		}

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "Please Open GPS...", Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 0);
			return;
		}

		provider = lm.getBestProvider(getCriteria(), true);
		// 获取位置信息
		// 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
		location = lm.getLastKnownLocation(provider);

		if(location  == null)    
		{ 
			if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 1, locationListener);
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); 
            }
//		  lm.requestLocationUpdates(provider, 60000, 1, locationListener);    
		}  

		longtitude = Double.toString(location.getLongitude());
		latitude = Double.toString(location.getLatitude());

		Toast.makeText(this,"Sign up: " + longtitude + " " + latitude, Toast.LENGTH_LONG)
				.show();
		// updateView(location);
		// 监听状态
		lm.addGpsStatusListener(listener);
		// 绑定监听，有4个参数
		// 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
		// 参数2，位置信息更新周期，单位毫秒
		// 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
		// 参数4，监听
		// 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

		// 1秒更新一次，或最小位移变化超过1米更新一次；
		// 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
				locationListener);

		WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, this,
				"Posting data...");

		wst.addNameValuePair("name", firstName);
		wst.addNameValuePair("telphone", telphone);
		wst.addNameValuePair("userName", email);
		wst.addNameValuePair("password", password_1);
		wst.addNameValuePair("email", email);
		wst.addNameValuePair("longtitude", longtitude);
		wst.addNameValuePair("latitude", latitude);
		wst.addNameValuePair("description", address);

		wst.execute(new String[] { SERVICE_URL });

	}

	public void handleResponseLocal(String response) {
		if (response.endsWith("true")) {
			Intent intent = new Intent();
			intent.setClass(RegisterActivity.this, LoginActivity.class);
			startActivity(intent);
			RegisterActivity.this.finish();
		}

	}

	
	private LocationListener locationListener=new LocationListener() {
		  
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
            //GPS状态为可见时
            case LocationProvider.AVAILABLE:
                Log.i(TAG, "当前GPS状态为可见状态");
                break;
            //GPS状态为服务区外时
            case LocationProvider.OUT_OF_SERVICE:
                Log.i(TAG, "当前GPS状态为服务区外状态");
                break;
            //GPS状态为暂停服务时
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.i(TAG, "当前GPS状态为暂停服务状态");
                break;
            }
            
        }
    
        /**
         * GPS开启时触发
         */
        public void onLocationChanged(Location location) {  
            updateWithNewLocation(location);  
        }  
        // provider 被用户关闭后调用  
        public void onProviderDisabled(String provider) {  
            updateWithNewLocation(null);  
        }  
        public void onProviderEnabled(String provider) {
            Location location=lm.getLastKnownLocation(provider);
//            updateView(location);
        }
    };
    
    private void updateWithNewLocation(Location location2) {  
        // TODO Auto-generated method stub  
        while(location == null){  
            lm.requestLocationUpdates(provider, 2000, (float) 0.1, locationListener);  
        }  
    }  
    //状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
            //第一次定位
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Log.i(TAG, "第一次定位");
                break;
            //卫星状态改变
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                Log.i(TAG, "卫星状态改变");
                //获取当前状态
                GpsStatus gpsStatus=lm.getGpsStatus(null);
                //获取卫星颗数的默认最大值
                int maxSatellites = gpsStatus.getMaxSatellites();
                //创建一个迭代器保存所有卫星 
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                int count = 0;     
                while (iters.hasNext() && count <= maxSatellites) {     
                    GpsSatellite s = iters.next();     
                    count++;     
                }   
                System.out.println("搜索到："+count+"颗卫星");
                break;
            //定位启动
            case GpsStatus.GPS_EVENT_STARTED:
                Log.i(TAG, "定位启动");
                break;
            //定位结束
            case GpsStatus.GPS_EVENT_STOPPED:
                Log.i(TAG, "定位结束");
                break;
            }
        };
    };

    private Criteria getCriteria(){
        Criteria criteria=new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细 
        criteria.setAccuracy(Criteria.ACCURACY_FINE);    
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费  
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求  
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }
	
	
	private class WebServiceTask extends AbstractWebServiceTask {

		public WebServiceTask(int taskType, Context mContext,
				String processMessage) {
			super(taskType, mContext, processMessage);
		}

		@Override
		protected void hideKeyboard() {
			InputMethodManager inputManager = (InputMethodManager) RegisterActivity.this
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			inputManager.hideSoftInputFromWindow(RegisterActivity.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

		@Override
		public void handleResponse(String response) {
			// TODO Auto-generated method stub
			handleResponseLocal(response);
		}
	}
}
