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
		// ��ȡλ����Ϣ
		// ��������ò�ѯҪ��getLastKnownLocation�������˵Ĳ���ΪLocationManager.GPS_PROVIDER
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
		// ����״̬
		lm.addGpsStatusListener(listener);
		// �󶨼�������4������
		// ����1���豸����GPS_PROVIDER��NETWORK_PROVIDER����
		// ����2��λ����Ϣ�������ڣ���λ����
		// ����3��λ�ñ仯��С���룺��λ�þ���仯������ֵʱ��������λ����Ϣ
		// ����4������
		// ��ע������2��3���������3��Ϊ0�����Բ���3Ϊ׼������3Ϊ0����ͨ��ʱ������ʱ���£�����Ϊ0������ʱˢ��

		// 1�����һ�Σ�����Сλ�Ʊ仯����1�׸���һ�Σ�
		// ע�⣺�˴�����׼ȷ�ȷǳ��ͣ��Ƽ���service��������һ��Thread����run��sleep(10000);Ȼ��ִ��handler.sendMessage(),����λ��
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
            //GPS״̬Ϊ�ɼ�ʱ
            case LocationProvider.AVAILABLE:
                Log.i(TAG, "��ǰGPS״̬Ϊ�ɼ�״̬");
                break;
            //GPS״̬Ϊ��������ʱ
            case LocationProvider.OUT_OF_SERVICE:
                Log.i(TAG, "��ǰGPS״̬Ϊ��������״̬");
                break;
            //GPS״̬Ϊ��ͣ����ʱ
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.i(TAG, "��ǰGPS״̬Ϊ��ͣ����״̬");
                break;
            }
            
        }
    
        /**
         * GPS����ʱ����
         */
        public void onLocationChanged(Location location) {  
            updateWithNewLocation(location);  
        }  
        // provider ���û��رպ����  
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
    //״̬����
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
            //��һ�ζ�λ
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Log.i(TAG, "��һ�ζ�λ");
                break;
            //����״̬�ı�
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                Log.i(TAG, "����״̬�ı�");
                //��ȡ��ǰ״̬
                GpsStatus gpsStatus=lm.getGpsStatus(null);
                //��ȡ���ǿ�����Ĭ�����ֵ
                int maxSatellites = gpsStatus.getMaxSatellites();
                //����һ�������������������� 
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                int count = 0;     
                while (iters.hasNext() && count <= maxSatellites) {     
                    GpsSatellite s = iters.next();     
                    count++;     
                }   
                System.out.println("��������"+count+"������");
                break;
            //��λ����
            case GpsStatus.GPS_EVENT_STARTED:
                Log.i(TAG, "��λ����");
                break;
            //��λ����
            case GpsStatus.GPS_EVENT_STOPPED:
                Log.i(TAG, "��λ����");
                break;
            }
        };
    };

    private Criteria getCriteria(){
        Criteria criteria=new Criteria();
        //���ö�λ��ȷ�� Criteria.ACCURACY_COARSE�Ƚϴ��ԣ�Criteria.ACCURACY_FINE��ȽϾ�ϸ 
        criteria.setAccuracy(Criteria.ACCURACY_FINE);    
        //�����Ƿ�Ҫ���ٶ�
        criteria.setSpeedRequired(false);
        // �����Ƿ�������Ӫ���շ�  
        criteria.setCostAllowed(false);
        //�����Ƿ���Ҫ��λ��Ϣ
        criteria.setBearingRequired(false);
        //�����Ƿ���Ҫ������Ϣ
        criteria.setAltitudeRequired(false);
        // ���öԵ�Դ������  
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
