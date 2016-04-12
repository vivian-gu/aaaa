package app.activities;

import java.sql.Timestamp;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.entities.AdsEntity;
import app.entities.NetworkProperties;
import app.entities.SysApplication;

import com.example.soosokanv3.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ModAdsActivity extends Activity {

	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/ads";

	public static final String TAG = "ADSMOD";

	private EditText up_ad_title, up_ad_description, up_ad_distance,
			up_ad_attribute;
	private String title, description, attribute, timeString;
	private int distance;
	private Timestamp time;
	private Button upadd_ads_btn;
	// spinner
	private TextView view;
	private String AdsID, SellerID;

	private ArrayAdapter<String> adapter;
	private AdsEntity ae;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		setContentView(R.layout.activity_ads_modify);
		getAdsIDandSellerID();// get Ads ID and sellerID

		getOneAds();// get ads info from server by sending adsId

		up_ad_title = (EditText) findViewById(R.id.up_ad_title);
		up_ad_description = (EditText) findViewById(R.id.up_ad_description);
		up_ad_distance = (EditText) findViewById(R.id.up_ad_distance);
		up_ad_attribute = (EditText) findViewById(R.id.up_ad_attribute);
		upadd_ads_btn = (Button) findViewById(R.id.upadd_ads_btn);

		// display old ads information
		// up_ad_title.setText(String.valueOf(title));
		// up_ad_description.setText(description);
		// up_ad_distance.setText(distance);
		// up_ad_attribute.setText(attribute);

	}

	private void getAdsIDandSellerID() {
		AdsID = "1";
		SellerID = "1";

	}

	/*
	 * @Put Path("/update")
	 */
	private void getOneAds() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		// params.add("adsID", adsID);
		// path needs change,to getOneads
		client.get(SERVICE_URL + "/byadsId", params,
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
						JSONObject myJsonObject;
						try {
							myJsonObject = new JSONObject(response);

							AdsID = myJsonObject.getString("_id");
							SellerID = myJsonObject.getString("sellerId");
							title = myJsonObject.getString("title");
							description = myJsonObject.getString("description");
							timeString = myJsonObject.getString("time");

							// time=Timestamp.valueOf(timeString);
							// distance = myJsonObject.getString("distance");
							String attribute = myJsonObject
									.getString("attribute");

							ae = new AdsEntity(AdsID, SellerID, title,
									description, timeString, distance,
									attribute);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});

	}

	/*
	 * updating an item by clicking button upads_add_btn
	 */
	public void ads_modify_btn(View vw) {
		// title = up_ad_title.getText().toString();
		// description=up_ad_description.getText().toString();
		// distance=up_ad_distance.getText().toString();
		// attribute=up_ad_attribute.getText().toString();

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		// params.add("_id", AdsID);
		// params.add("sellerId", SellerID);
		// params.add("title", title);
		// params.add("description", description);
		// params.add("distance", distance);

		// params.add("time", String.valueOf(time));
		params.add("attribute", attribute);

		/*
		 * url needs change, params
		 */

		client.post(SERVICE_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

				Toast.makeText(getApplicationContext(), "Something wrong!",
						Toast.LENGTH_LONG).show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				Toast.makeText(getApplicationContext(), "success",
//						Toast.LENGTH_LONG).show();

			}

		});

	}

}
