package app.activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import app.entities.NetworkProperties;
import app.entities.SysApplication;
import app.preview.MyLog;

import com.example.soosokanv3.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ItemManageActivity extends Activity {
	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/item/bySeller";

	public static final String TAG = "ITEMMANAGE";
	private ListView mListView;
	private JSONArray mData;
	private String itemid;
	SharedPreferences sp;
	private ItemListAdapter mAdapter;
	String sellerId;
	private long startTime,endTime; 
//	ArrayList<HashMap<String, Object>> ItemList_item;

	public void ItemAdd(View v) {
		startActivity(new Intent(this, AddItemActivity.class));
	}
	
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		sp = this.getSharedPreferences("userInfo", 0);
		sellerId = sp.getString("EMAIL", "");
//		ItemList_item = new ArrayList<HashMap<String, Object>>();
		initData();
		setContentView(R.layout.activity_item_manage);
	}

	private void initData() {
		startTime=System.currentTimeMillis();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("sellerId", sellerId);
		client.get(SERVICE_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				 endTime=System.currentTimeMillis();
				 MyLog.writeLog("General Text Reuqest Failure Response Time", (endTime-startTime) + "ms");
				if (arg0 == 404)
					Toast.makeText(getApplicationContext(),
							"Requested Resource not found", Toast.LENGTH_LONG)
							.show();
				else if (arg0 == 500)
					Toast.makeText(getApplicationContext(),
							"Something wrong at the server end",
							Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getApplicationContext(), "Unexpected error",
							Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] Response) {
				endTime=System.currentTimeMillis();
				 MyLog.writeLog("General Text Reuqest Success Response Time", (endTime-startTime) + "ms");
				String reponse = byteToString(Response);
				tranferToList(reponse);
			}

			private String byteToString(byte[] response) {
				String a = "";
				for (int i = 0; i < response.length; i++)
					a += (char) response[i];
				return a;
			}

		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //���µ������BACK��ͬʱû���ظ�
	    	SysApplication.close(SellerMainActivity.TAG);
	    	Intent intent = new Intent();
			intent.setClass(ItemManageActivity.this,SellerMainActivity.class);
			startActivity(intent);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void tranferToList(String response) {
		try {
			JSONArray json = new JSONArray(response);
			System.out.println("json" + json.getJSONObject(0).get("name"));

			mData = json;
			int length = json.length();
			mListView = (ListView) findViewById(R.id.itemlist);
			LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mListView.getLayoutParams();
			//LayoutParams lp = (LayoutParams) mListView.getLayoutParams();
		       lp.height = length*180;
		       mListView.setLayoutParams(lp);
		
			
			mAdapter = new ItemListAdapter(this, mData);
			mListView.setAdapter(mAdapter);
			mListView.setDescendantFocusability(ListView.FOCUS_AFTER_DESCENDANTS);
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					JSONObject item = mAdapter.getItem(position);
					try {
						itemid = item.getString("itemId");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent intent = new Intent();
					intent.putExtra("itemId", itemid);
				
					intent.setClass(ItemManageActivity.this,ItemDetailActivity.class);
					startActivity(intent);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
