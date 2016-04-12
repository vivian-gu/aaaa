package app.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import app.entities.NetworkProperties;
import app.entities.SysApplication;
import app.preview.MyLog;

import com.example.soosokanv3.R;

public class LoginActivity extends Activity {
	

	private static final String SERVICE_URL = NetworkProperties.nAddress+"/login";
	public static final String TAG = "LOGIN";
	private CheckBox rem_pw, auto_login;
	private SharedPreferences sp;
	private SharedPreferences.Editor localEditor;
	private EditText edEmail, edPassword;
	private String email, password;
	private long startTime,endTime; 
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		setContentView(R.layout.activity_login);
		ImageDownLoader.initImageLoader();
		ImageDownLoader.initContext(getApplicationContext());
		
		sp = this.getSharedPreferences("userInfo", 0);
	    localEditor = sp.edit();
	     
//		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		rem_pw = (CheckBox) findViewById(R.id.lgcb_repass);
		auto_login = (CheckBox) findViewById(R.id.lgcb_autolog);
		edEmail = (EditText) findViewById(R.id.selleremail_editText);
		edPassword = (EditText) findViewById(R.id.Password_editText);

		if (sp.getBoolean("ISCHECK", false)) {
			rem_pw.setChecked(true);
			edEmail.setText(sp.getString("EMAIL", ""));
			edPassword.setText(sp.getString("PASSWORD", ""));
			
			if (sp.getBoolean("AUTO_ISCHECK", false)) {
				auto_login.setChecked(true);

			}
			
		}

		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (rem_pw.isChecked()) {

					System.out.println("Remember Checked!");
					sp.edit().putBoolean("ISCHECK", true).commit();

				} else {

					System.out.println("Remember Unchecked!");
					sp.edit().putBoolean("ISCHECK", false).commit();

				}
			}
		});

		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (auto_login.isChecked()) {
					
					System.out.println("Auto login checked!");
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
					
				} else {
					
					System.out.println("Auto login unchecked!");
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
					
				}
			}
		});
	}

	public void postData(View vw) {

		email = edEmail.getText().toString();
		password = edPassword.getText().toString();

		if (email.equals("") || password.equals("")) {
			Toast.makeText(this, "Please enter in all required fields.",
					Toast.LENGTH_LONG).show();
			return;
		}

		WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, this,
				"Posting data...");

		wst.addNameValuePair("email", email);
		wst.addNameValuePair("password", password);

		// the passed String is the URL we will POST to
		startTime=System.currentTimeMillis();
		wst.execute(new String[] { SERVICE_URL });

		if (rem_pw.isChecked()) {
			Editor editor = sp.edit();
			editor.putString("EMAIL", email);
			editor.putString("PASSWORD", password);
			editor.commit();
		}
		localEditor.putString("email", email);
        localEditor.commit();
		

	}

	public void register(View vw) {
		
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);
	}

	public void handleResponseLocal(String response) {
		try {
			if(!response.equals(null)) {
				
//				JSONArray arr=(JSONArray) JSON.parse(response);
//				   JSONObject o=(JSONObject) arr.get(0);
//				   System.out.println(o.get("EMAIL"));
				   
				 JSONObject myJsonObject = new JSONObject(response);
				   //��ȡ��Ӧ��ֵ
				   String email = myJsonObject.getString("email");
				   String name =  myJsonObject.getString("name");
				   String address =  myJsonObject.getString("description");
				   
				   String password = myJsonObject.getString("password");
				   
				   if(password.equals("wrong")){
					   Toast.makeText(this, "Wrong Password! Please Re-enter!",
								Toast.LENGTH_LONG).show();
					   
					   
				   }else{
					   Editor editor = sp.edit();
						editor.putString("EMAIL", email);
						editor.putString("NAME", name);
						editor.putString("ADDRESS", address);
						
						editor.commit();
//						Toast.makeText(this, response, Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, SellerMainActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
				   }
				   
				   
				  

				
				
			} else {
//				Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
		}

	}

	private class WebServiceTask extends AbstractWebServiceTask {

		public WebServiceTask(int taskType, Context mContext,
				String processMessage) {
			super(taskType, mContext, processMessage);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void hideKeyboard() {
			// TODO Auto-generated method stub
			InputMethodManager inputManager = (InputMethodManager) LoginActivity.this
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			inputManager.hideSoftInputFromWindow(LoginActivity.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

		}

		@Override
		public void handleResponse(String response) {
			// TODO Auto-generated method stub
			endTime=System.currentTimeMillis();
			MyLog.writeLog("General Login Reuqest Response Time", (endTime-startTime) + "ms");
			handleResponseLocal(response);
		}

	}

}
