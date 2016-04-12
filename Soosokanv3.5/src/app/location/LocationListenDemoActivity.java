package app.location;

import com.example.soosokanv3.R;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocationListenDemoActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
   
	public final static int REFRESH_LOCATION = 0x0100;
	
	
//	private Button mBtnOpenListen;

//	private Button mBtnCloseListen;
//	private TextView mTextViewListenState;
//
//
//
//	private TextView mTVChangeGPSLocation;
//	private Button mBtnGetLocationByGPS;
//	private TextView mTVNewGpsLocaton;
//
//	private Button mBtnClear;
//
//	private TextView mTVChangeNetworkLocation;
//	private Button mBtnGetLocationByNetwork;
//	private TextView mTVNewNetworkLocaton;
//
//
	private Handler mHandler;
	private MyLocationManager myLocationManager;
//
	private long curTimeGPS;
	private long curTimeNetwork;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);
        
  //      initView();
        
        initLogic();
    }
    
    
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		myLocationManager.unRegisterListen();
	}



	public void initView()
    {
//
//    	mBtnOpenListen = (Button) findViewById(R.id.btnOpenListen);
//    	mBtnOpenListen.setOnClickListener(this);
//    	mBtnCloseListen = (Button) findViewById(R.id.btnCloseListen);
//    	mBtnCloseListen.setOnClickListener(this);
//    	mTextViewListenState = (TextView) findViewById(R.id.textViewListenState);
//
//
//
//    	mTVChangeGPSLocation = (TextView) findViewById(R.id.GPSChangeLocation);
//    	mBtnGetLocationByGPS = (Button) findViewById(R.id.btnGetLocationByGps);
//    	mBtnGetLocationByGPS.setOnClickListener(this);
//    	mTVNewGpsLocaton = (TextView) findViewById(R.id.GPSNewLocaton);
//
//    	mBtnClear = (Button) findViewById(R.id.btnClear);
//    	mBtnClear.setOnClickListener(this);
//
//
//
//    	mTVChangeNetworkLocation = (TextView) findViewById(R.id.NetworkChangeLocation);
//    	mBtnGetLocationByNetwork = (Button) findViewById(R.id.btnGetLocationByNetwork);
//    	mBtnGetLocationByNetwork.setOnClickListener(this);
//    	mTVNewNetworkLocaton = (TextView) findViewById(R.id.NetworkNewLocaton);
    }


    public void initLogic()
    {
    	mHandler = new Handler()
    	{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what)
				{
				case REFRESH_LOCATION:
					Location location = (Location) msg.obj;
					
					Toast.makeText(LocationListenDemoActivity.this,
                            "onLocationChanged provider = ..." + location.getProvider(),
                            Toast.LENGTH_SHORT).show();
					
					
					if (location.getProvider().equals(LocationManager.GPS_PROVIDER))
					{
						long inteval = System.currentTimeMillis() - curTimeGPS;
						curTimeGPS = System.currentTimeMillis();
						
						String address = null;
						if (location != null)
						{
							address = myLocationManager.queryAddressByGoogle(location.getLatitude(), location.getLongitude());
						}		
						//showChangeGPSLocation(location, address, inteval);
					}else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER))
					{
						long inteval = System.currentTimeMillis() - curTimeNetwork;
						curTimeNetwork = System.currentTimeMillis();
						
						String address = null;
						if (location != null)
						{
							address = myLocationManager.queryAddressByGoogle(location.getLatitude(), location.getLongitude());
						}		
						//showChangeNetworkLocation(location, address, inteval);
					}
					
					break;
					default:
						break;
				}
			}
    		
    	};
    	
    	myLocationManager = new MyLocationManager(this, mHandler);
    }

	public void openListen()
	{
		myLocationManager.registerListen();
		setListenState(true);
		curTimeGPS = System.currentTimeMillis();
		curTimeNetwork = System.currentTimeMillis();
	}
	
	public void closeListen()
	{
		myLocationManager.unRegisterListen();
		setListenState(false);
		curTimeGPS = 0;
		curTimeNetwork = 0;
	}
	

	public void getLocationByGPS()
	{
		//MyLocationManager myLocationManager = new MyLocationManager();
		
		Location location = myLocationManager.getLocationByGps();
		String address = null;
		if (location != null)
		{
			address = myLocationManager.queryAddressByGoogle(location.getLatitude(), location.getLongitude());
		}
		
	
		//showNewGPSLocation(location, address);
	}
	

	public void getLocationByNetwork()
	{
		
		
		Location location = myLocationManager.getLocationByNetwork();
		String address = null;
		if (location != null)
		{
			address = myLocationManager.queryAddressByGoogle(location.getLatitude(), location.getLongitude());
		}
		
	
		//showNewNetworkLocation(location, address);
	}
	
	@Override
	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.btnOpenListen:
//			openListen();
//			break;
//		case R.id.btnCloseListen:
//			closeListen();
//			break;
//		case R.id.btnGetLocationByGps:
//			getLocationByGPS();
//			break;
//		case R.id.btnGetLocationByNetwork:
//			getLocationByNetwork();
//			break;
//		case R.id.btnClear:
//		//	clear();
//			break;
//		default:
//			break;
//		}
	}
	

	public void setListenState(boolean flag)
	{
		if (flag)
		{
			//mTextViewListenState.setText("open");
		}else{
			//mTextViewListenState.setText("close");
		}
	}
	
//	public void showChangeGPSLocation(Location location, String adress, long inteval)
//	{
//		String str = MyUtil.formatLocation(location);
//		if (adress != null)
//		{
//			str += adress + "\n";
//		}
//		mTVChangeGPSLocation.setText("listen from gps-location change -->" +
//				"from last time interval is " + inteval + "\n" + str);
//	}
	
//	public void showChangeNetworkLocation(Location location, String adress,  long timeinterval)
//	{
//		String str = MyUtil.formatLocation(location);
//		if (adress != null)
//		{
//			str += adress + "\n";
//		}
//			mTVChangeNetworkLocation.setText("listen from network-location change -->" +
//				"from last time interval is " + timeinterval + "\n" + str);
//	}
	
//	public void showNewGPSLocation(Location location, String adress)
//	{
//		String str = MyUtil.formatLocation(location);
//		if (adress != null)
//		{
//			str += adress + "\n";
//		}
//		mTVNewGpsLocaton.setText(str);
//	}
	
//	public void showNewNetworkLocation(Location location, String adress)
//	{
//		String str = MyUtil.formatLocation(location);
//		if (adress != null)
//		{
//			str += adress + "\n";
//		}
//		mTVNewNetworkLocaton.setText(str);
//	}
	

	
	
	
//	public void clear()
//	{
//		mTVChangeGPSLocation.setText("");
//		mTVChangeNetworkLocation.setText("");
//		mTVNewGpsLocaton.setText("");
//		mTVNewNetworkLocaton.setText("");
//
//	}
}