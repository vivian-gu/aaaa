package app.activities;


//import android.R;
import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.entities.NetworkProperties;
import app.entities.SysApplication;
import app.preview.SaveBitMap;

import com.example.soosokanv3.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AdsDetailActivity extends Activity {
	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/ads/adsPic";
	public static final String TAG = "ADSDETAIL";
	private TextView adstime,adstitle,adsdes;
	private ImageView imageview;
	private String adstimeString,adstitleString,adsdesString;
	public AdsDetailActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.addActivity(this, TAG);
        setContentView(R.layout.activity_ads_detail);
        Intent intent = getIntent();
        
        String id = intent.getStringExtra("adsId");
        //String sellerId = intent.getStringExtra("sellerId");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
       // String time = intent.getStringExtra("time");
        String distance = intent.getStringExtra("distance");
        String attribute = intent.getStringExtra("attribute");
        String adsPicName = intent.getStringExtra("picture");
         
        
        adstime=(TextView)findViewById(R.id.adstime);
        adstitle=(TextView)findViewById(R.id.adstitle);
        adsdes=(TextView)findViewById(R.id.adsdes);
        imageview=(ImageView)findViewById(R.id.adsPic);
        
       // adstime.setText(time);
        adstitle.setText(title);
        adsdes.setText(description);
        
//        initPic(id, imageview);
        
        imageview.setTag(adsPicName);
		new CanvasImageTask().execute(imageview);
        

    }
	


}
