package app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.entities.SysApplication;

import com.example.soosokanv3.R;


public class SellerMainActivity extends Activity implements OnClickListener{
	 Intent intent;
	
	 SharedPreferences sp;
	 long mExitTime=0;
	 public static final String TAG = "SELLERMAIN";
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		sp = this.getSharedPreferences("userInfo", 0);
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		setContentView(R.layout.activity_seller_main);
		
		Button btn_manage_item = (Button)findViewById(R.id.Manage_item_button);
		Button btn_manage_ads = (Button)findViewById(R.id.Manage_ads_button);
		ImageButton Setting_button = (ImageButton)findViewById(R.id.Setting_button);
		
		ImageView head = (ImageView)findViewById(R.id.touxiang_roundimage);

		
		String account = sp.getString("NAME", "");

		TextView accountname = (TextView)findViewById(R.id.seller_accountname);
		accountname.setText(account);
		if(account.startsWith("Argos")){
			head.setBackgroundResource(R.drawable.argos);
		}else if (account.startsWith("Tesco")){
			head.setBackgroundResource(R.drawable.tesco_logo);
		}else if (account.startsWith("Dunnes")){
			head.setBackgroundResource(R.drawable.dunnes_logo);
		}else if (account.startsWith("O2")){
			head.setBackgroundResource(R.drawable.o2_logo);
		}else if (account.startsWith("Camera")){
			head.setBackgroundResource(R.drawable.cameracenter_logo);
		} 

		
		String address = sp.getString("ADDRESS", "");

		TextView addressname = (TextView)findViewById(R.id.seller_address);
		addressname.setText(address);
		
		btn_manage_item.setOnClickListener(this);
		btn_manage_ads.setOnClickListener(this);
		Setting_button.setOnClickListener(this);		
	}
	
	public void onClick(View v) {
		  // TODO Auto-generated method stub
		 
		  switch(v.getId()){
		  case R.id.Manage_item_button:
			 intent = new Intent (SellerMainActivity.this, ItemManageActivity.class);
			 startActivity(intent);
			 break;
			 
		  case R.id.Manage_ads_button:
			  intent = new Intent(SellerMainActivity.this, AdsManageActivity.class);
			  startActivity(intent);
			  break;
			  
		  case R.id.Setting_button:
			  intent = new Intent(SellerMainActivity.this, AccountManageActivity.class);
			  startActivity(intent);
			  break;
		 
		  default:
				  break;
		  }
		  
		 }
	
	        public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
						if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                                Object mHelperUtils;
                                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                                mExitTime = System.currentTimeMillis();
 
                        } else {
                        		SysApplication.closeall();
                        		System.exit(0);
                        }
                        return true;
                }
                return super.onKeyDown(keyCode, event);
        }
	

}
