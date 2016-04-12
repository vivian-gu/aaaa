package app.activities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import app.entities.AdsEntity;
import app.entities.NetworkProperties;

import com.example.soosokanv3.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class AdsManageActivity extends Activity implements
		OnChildClickListener {
	private static final String SERVICE_URL = NetworkProperties.nAddress+"/ads/bySeller";
	private ExpandableListView mListView = null;
	private ExpandAdapter mAdapter = null;
	private JSONArray mData;
	SharedPreferences sp;
	String sellerId;
	public void AdsAdd(View v) {
		startActivity(new Intent(this, AddadsActivity.class));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = this.getSharedPreferences("userInfo", 0);
		sellerId = sp.getString("EMAIL", "");
		initData();
		setContentView(R.layout.activity_ads_list);
	}
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		JSONObject item = mAdapter.getChild(groupPosition, childPosition);
		Intent intent = new Intent();
		
		try{
			intent.putExtra("adsId", item.getString("adsId"));
			intent.putExtra("title", item.getString("title"));
			intent.putExtra("description", item.getString("description"));
			intent.putExtra("distance", item.getString("distance"));
			intent.putExtra("attribute",item.getString("attribute"));
			intent.putExtra("picture", item.getString("picture"));
			
	        intent.setClass(AdsManageActivity.this, AdsDetailActivity.class);
			 startActivity(intent);
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	private void initData() {
		AsyncHttpClient client = new AsyncHttpClient();
		 RequestParams params=new RequestParams();
		 params.add("sellerId", sellerId);
         client.get(SERVICE_URL,params ,new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				if(arg0==404)
					Toast.makeText(getApplicationContext(), "Requested Resource not found",
							Toast.LENGTH_LONG).show();
				else if(arg0==500)
					Toast.makeText(getApplicationContext(), "Something wrong at the server end",
							Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getApplicationContext(), "Unexpected error",
							Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] Response) {
				String reponse=NetworkProperties.byteToString(Response);
				tranferToList(reponse);
			}
         });
	}

	private void tranferToList(String response) {
		try {
			JSONArray json=new JSONArray(response);
			JSONArray jsonArray=(JSONArray) json;
			
			mData = jsonArray;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		mListView = (ExpandableListView) findViewById(R.id.nearbyadslist);
		mListView.setGroupIndicator(getResources().getDrawable(
				R.drawable.expander_floder));
		mAdapter = new ExpandAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		mListView
				.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
		mListView.setOnChildClickListener(this);
	}
	
	
	private AdsEntity JSONTOAds(JSONObject myJsonObject) {
		AdsEntity ae=null;
		try {
			String adsId=myJsonObject.getString("_id");
			String sellerId=myJsonObject.getString("sellerId");
			String adsTitle = myJsonObject.getString("title");
			String adsdes =  myJsonObject.getString("description");
			String timeString;
		
			timeString = myJsonObject.getString("time");
		
		   String distanceString =  myJsonObject.getString("distance");
		   int distance=Integer.valueOf(distanceString);
		   String attribute=myJsonObject.getString("attribute");
		   ae=new AdsEntity(adsId, sellerId, adsTitle,
				   adsdes, timeString, distance,attribute);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ae;
	}
}