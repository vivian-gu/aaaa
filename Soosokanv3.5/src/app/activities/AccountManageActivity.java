package app.activities;

import com.example.soosokanv3.R;

import android.app.Activity;
import android.os.Bundle;
import app.entities.SysApplication;


public class AccountManageActivity extends Activity{
	public static final String TAG = "ACCOUNTMNG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		setContentView(R.layout.activity_account_manage);
	}
}
