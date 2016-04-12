package app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import app.entities.NetworkProperties;
import app.entities.SysApplication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class DeleteAdsActivity extends Activity {
	private String AdsId;
	private static final String SERVICE_URL = NetworkProperties.nAddress+"/ads";
	public static final String TAG = "ADSDELETE";

	public DeleteAdsActivity() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AdsId = getAdsID();
		SysApplication.addActivity(this, TAG);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("AdsId", AdsId);
		client.get(SERVICE_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, org.apache.http.Header[] arg1,
					byte[] arg2, Throwable arg3) {
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
			public void onSuccess(int arg0, org.apache.http.Header[] arg1,
					byte[] arg2) {
				String reponse = NetworkProperties.byteToString(arg2);

				Toast.makeText(getApplicationContext(), "delete success!",
						Toast.LENGTH_LONG).show();
				Intent intent=new Intent(DeleteAdsActivity.this, AdsManageActivity.class);
				startActivity(intent);

			}
		});
		
	}

	private String getAdsID() {
		// TODO Auto-generated method stub
		return "1";
	}

}
