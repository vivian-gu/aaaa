package app.activities;

import java.sql.Timestamp;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.entities.ItemEntity;
import app.entities.NetworkProperties;
import app.entities.SysApplication;
import app.preview.SaveBitMap;

import com.example.soosokanv3.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ModItemActivity extends Activity {

	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/item";

	public static final String TAG = "ITEMMOD";
	
	private EditText itemprice, itemkeyword, itemname, discountno;
	private String itemId, sellerId, name,keyword,picture,discountString;
	private int discount; 
	private float price;
	private Timestamp time;
	private CheckBox checkdiscount;
	private Button upadd_item_btn;
	private ImageView uptake_photo;
//	Bitmap pict=SaveBitMap.getImageFromSDCard("1.jpg");
	// spinner
	private TextView view;

	private ArrayAdapter<String> adapter;
	private String ItemId;
	private ItemEntity ie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		setContentView(R.layout.activity_item_modify);
//		getItemIDandSellerID();//get sellerID and ItemID

		Intent intent = this.getIntent();
		itemId = intent.getStringExtra("itemId");
		
		getOneItem(itemId);//retrieve old item information

		itemprice = (EditText) findViewById(R.id.upitemprice);
		itemkeyword = (EditText) findViewById(R.id.upitemkeyword);
		itemname = (EditText) findViewById(R.id.upitemname);
		discountno = (EditText) findViewById(R.id.updiscountno);
//		upadd_item_btn = (Button) findViewById(R.id.upadd_item_btn);
//		uptake_photo = (ImageView) findViewById(R.id.ItemPic1);
		
		
		itemprice.setText(String.valueOf(price));
		itemkeyword.setText(keyword);
		itemname.setText(name);
		discountno.setText(discountString);
	}

	/**
	 * retrieve old item information
	 * @Put Path("/update")
	 */
	private void getOneItem(String ItemId) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("itemId", ItemId);
		// path needs change,to getOneItem
		client.get(SERVICE_URL + "/byItemId", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						if (arg0 == 404)
							Toast.makeText(getApplicationContext(),
									"Requested Resource not found",
									Toast.LENGTH_LONG).show();
						else if (arg0 == 500)
							Toast.makeText(getApplicationContext(),
									"Something wrong at the server end",
									Toast.LENGTH_LONG).show();
						else
							Toast.makeText(getApplicationContext(),
									"Unexpected error", Toast.LENGTH_LONG)
									.show();
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String response = NetworkProperties.byteToString(arg2);

//						Toast.makeText(getApplicationContext(), response,
//								Toast.LENGTH_LONG).show();
						/*
						 * get old item information
						 */
						JSONObject myJsonObject;
						try {
							myJsonObject = new JSONObject(response);

							itemId = myJsonObject.getString("itemId");
							sellerId = myJsonObject.getString("sellerId");
							name = myJsonObject.getString("name");
							keyword = myJsonObject.getString("keyword");
							String priceString = myJsonObject
									.getString("price");
							price = Float.valueOf(priceString);
							String timeString = myJsonObject.getString("time");
							time = Timestamp.valueOf(timeString);
							picture = myJsonObject.getString("picture");
							String discountString = myJsonObject
									.getString("discount");
							discount = Integer.parseInt(discountString);

							ie = new ItemEntity(itemId, sellerId, name,
									keyword, price, time, discount,
									picture);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					
				});

	}

	/*
	 * when clicking button, xml:onclick method
	 */
	public void item_modify_btn(View vw) {
		/*
		 * get new information of item
		 */
		 name = itemname.getText().toString();
		 keyword = itemkeyword.getText().toString();
		   String priceString =  itemprice.getText().toString();
		   price=Float.valueOf(priceString);
//		   picture=SaveBitMap.BitMapToString(pict);
		   String discountString=discountno.getText().toString();
		   discount=Integer.valueOf(discountString);
		   
		   /*
		    * send to server
		    */
		   AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.add("itemId", ItemId);
			params.add("sellerId", sellerId);
			params.add("name", name);
			params.add("keyword", keyword);
			params.add("price", String.valueOf(price));
			params.add("time", String.valueOf(time));
			
			params.add("picture", picture);
			params.add("discount", String.valueOf("discount"));

			//url needs change
			client.post(SERVICE_URL, params, new AsyncHttpResponseHandler(){

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					
					Toast.makeText(getApplicationContext(),
							"Something wrong!",
							Toast.LENGTH_LONG).show();
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//					Toast.makeText(getApplicationContext(),
//							"success",
//							Toast.LENGTH_LONG).show();
				}
			});

	}

}
