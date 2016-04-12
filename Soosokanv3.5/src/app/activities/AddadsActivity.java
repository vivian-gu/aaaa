package app.activities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import app.entities.NetworkProperties;
import app.preview.FileUtil;
import app.preview.ImageThumbnail;
import app.preview.ImageUtil;
import app.preview.SaveBitMap;

import com.example.soosokanv3.R;

public class AddadsActivity extends Activity {

	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/ads/add";
	private static final int TAKE_PICTURE_WITH_CAMERA = 0;

	private EditText edAdstitle, edAdsdes;
	private String Adstitle, Adsdes;
	private String picName,path;
	private Boolean picBoolean;
	private ImageView adsPic;
	private Bitmap bm = null;
	SharedPreferences sp;
	String sellerId;

	// spinner

	TextView spinnerview1, spinnerview2;

	Spinner spinner1, spinner2;
	private String[] state = { "1", "2", "3" };
	private String[] type = { "Discount", "Voucher", "New Product", "Other" };
	
	OnItemSelectedListener l1,l2;

	private ArrayAdapter<String> adapter_state, adapter_type;

	private String selState;

	private String selType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_ads_add);
		sp = this.getSharedPreferences("userInfo", 0);
		sellerId = sp.getString("EMAIL", "");

		edAdstitle = (EditText) findViewById(R.id.adstitle);
		edAdsdes = (EditText) findViewById(R.id.adsdes);
		adsPic = (ImageView)findViewById(R.id.AdsPic_add);

		spinnerview1 = (TextView) findViewById(R.id.spinnerText1);
		spinnerview2 = (TextView) findViewById(R.id.spinnerText2);
		spinner1 = (Spinner) findViewById(R.id.ads_range);
		spinner2 = (Spinner) findViewById(R.id.ads_type);

		 adapter_state = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, state);
		adapter_state
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter_state);
		spinner1.setOnItemSelectedListener(
		new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {

				spinner1.setSelection(position);
				selState = (String) spinner1.getSelectedItem();
						// Showing selected spinner item
				spinnerview1.setText("Selected Range (KM):" + selState);
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			
			}
		});
        

	    adapter_type = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, type);
		adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter_type);
		spinner2.setOnItemSelectedListener(
			new OnItemSelectedListener(){
				public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
			 
					spinner2.setSelection(position);
					selType = (String) spinner2.getSelectedItem();
					spinnerview2.setText("Belongs to Type:" + selType);
				}
				public void onNothingSelected(AdapterView<?> arg0) {

				}
		});
	}

	public void ads_add_btn(View vw) throws IOException {
		String pictureString = null;
		Adstitle = edAdstitle.getText().toString();
		Adsdes = edAdsdes.getText().toString();
		if (Adstitle.equals("") || Adsdes.equals("")) {
			Toast.makeText(this, "Please enter in all required fields.",
					Toast.LENGTH_LONG).show();
			return;
		}

		WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, this,
				"Posting data...");
		 if (bm == null) {
			 ;
			 } else {
			pictureString = SaveBitMap.BitMapToString(bm);
			
			 }
		wst.addNameValuePair("distance",selState );
		wst.addNameValuePair("adsType", selType);
		
//		wst.addNameValuePair("distance","1000" );
//		wst.addNameValuePair("adsType", "Discount");
		
		if(picBoolean){
			wst.addNameValuePair("Picture", picName);
			UploadFileTask uploadFileTask=new UploadFileTask(this);
//			File file = new File(picName);
//			inputstreamtofile(bs,file);
			uploadFileTask.execute(path);
		}else{
			wst.addNameValuePair("Picture", "21.jpg");
		}
		
		wst.addNameValuePair("sellerID", sellerId);
		wst.addNameValuePair("AdsTitle", Adstitle);
		wst.addNameValuePair("dsDes", Adsdes);
		wst.addNameValuePair("adsPicture", pictureString);
		

		wst.execute(new String[] { SERVICE_URL });

	}

	public void handleResponseLocal(String response) {
		if (!response.equals(null)) {
			if(response.equals("false")){
				Toast.makeText(this, "Please pay!!",
						Toast.LENGTH_LONG).show();
			}else
				Toast.makeText(this, "Add Success!!",Toast.LENGTH_LONG).show();
			Intent intent=new Intent(AddadsActivity.this, AdsManageActivity.class);
			startActivity(intent);
//			SysApplication.close(AddItemActivity.TAG);
//			AddadsActivity.this.finish();
		}
	}

	private class WebServiceTask extends AbstractWebServiceTask {
		public WebServiceTask(int taskType, Context mContext,String processMessage) {
			super(taskType, mContext, processMessage);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void hideKeyboard() {
			// TODO Auto-generated method stub
			InputMethodManager inputManager = (InputMethodManager)AddadsActivity.this.
					getSystemService(Context.INPUT_METHOD_SERVICE);

			inputManager.hideSoftInputFromWindow(AddadsActivity.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
		@Override
		public void handleResponse(String response) {
			// TODO Auto-generated method stub
			handleResponseLocal(response);
		}
	}
	
	public void ads_pic_button(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.AdsPic_add:
//			Intent intent = new Intent(AddadsActivity.this,
//					CameraActivity.class);
//			startActivity(intent);
			Date date = new Date();
		    picName = sellerId.replaceAll("[^\\w]", "")+new SimpleDateFormat("yyyyMMdd_HHmmss")
			.format(date)+".jpg";
		    
		    path = FileUtil.getPath(picName);
			
			    File file = new File(path);
			    Uri outputFileUri = Uri.fromFile(file);

			    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			    
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); 
			
//			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, TAKE_PICTURE_WITH_CAMERA);
			break;

		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		super.onActivityResult(requestCode, resultCode, intent);
		Bitmap bitmap = null;
		if (resultCode == RESULT_OK) {
//			Bundle bundle = intent.getExtras();
//			 bitmap = (Bitmap) bundle.get("data");
            // Image captured and saved to fileUri specified in the Intent
			if(path.endsWith("jpg")||path.endsWith("png"))
			{
				
				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();    
				bitmapOptions.inSampleSize = 8;    
				File file = new File(path);    
				/**  
				 * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转  
				 */    
				int degree = ImageThumbnail.readPictureDegree(file.getAbsolutePath());    
				Bitmap cameraBitmap = BitmapFactory.decodeFile(path, bitmapOptions);    
				bitmap = cameraBitmap;    
				/**  
				 * 把图片旋转为正的方向  
				 */    
				bitmap = ImageThumbnail.rotaingImageView(degree, bitmap); 
				FileUtil.saveBitmap2file(bitmap, picName);
				
			}else{
			}
			 
            Toast.makeText(this, "Image saved to: "+path
            		, Toast.LENGTH_LONG).show();
            
            picBoolean = true;
        } else if (resultCode == RESULT_CANCELED) {
        } else {
        }


			
		adsPic.setImageBitmap(bitmap);
	}
	
	

}
