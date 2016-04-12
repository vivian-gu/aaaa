package app.activities;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import app.entities.NetworkProperties;
import app.entities.SysApplication;
import app.preview.FileUtil;
import app.preview.ImageThumbnail;
import app.preview.MyLog;
import app.preview.SaveBitMap;

import com.example.soosokanv3.R;

public class AddItemActivity extends Activity {

	public static final String TAG = "ITEMADD";
	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/item/add";
	private static final int TAKE_PICTURE_WITH_CAMERA = 0;

	private CheckBox discount_box;
	
	private long startTime,endTime; 
	private EditText itemprice, itemkeyword, itemname, discountno;
	private String itempriceString, itemkeywordString, itemnameString,
			discountnoString;
//	private String picURL = "";
	private String picName;
	// private CheckBox checkdiscount;
	private Button add_item_btn, add_photo;
	private String path;
	private ImageView item_pic;
	private boolean picBoolean = false;
	
//	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	Bitmap bm = null;
	SharedPreferences sp;
	String sellerId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.addActivity(this, TAG);
		setContentView(R.layout.activity_item_add);
		discountnoString = "100";
		sp = this.getSharedPreferences("userInfo", 0);
		sellerId = sp.getString("EMAIL", "");

		discount_box = (CheckBox) findViewById(R.id.checkdiscount);

		OnCheckBoxClickListener checkbox_listener = new OnCheckBoxClickListener();
		discount_box.setOnClickListener(checkbox_listener);
		itemprice = (EditText) findViewById(R.id.itemprice);
		itemkeyword = (EditText) findViewById(R.id.itemkeyword);
		itemname = (EditText) findViewById(R.id.itemname);
		discountno = (EditText) findViewById(R.id.discountno);
		discountno.setEnabled(false);
		
		add_item_btn = (Button) findViewById(R.id.add_item_btn);
		item_pic = (ImageView) findViewById(R.id.item_pic);

	
	}

	class OnCheckBoxClickListener implements OnClickListener {

		@Override
		public void onClick(View view) {

			if (discount_box.isChecked()) {

				discountno.setEnabled(true);
				
				if (discountnoString.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter the discount.", Toast.LENGTH_LONG)
							.show();
				}
				discountnoString = discountno.getText().toString();
			} else {
				discountnoString = "100";
				Toast.makeText(getApplicationContext(),
						"Check Box is not selected!.", Toast.LENGTH_LONG)
						.show();
			}
		}

	}

	public void item_add_btn(View vw) throws IOException {

		itempriceString = itemprice.getText().toString();
		itemkeywordString = itemkeyword.getText().toString();
		itemnameString = itemname.getText().toString();
		


		// Toast.makeText(this, "Bitmap to String: "+pictureString,
		// Toast.LENGTH_LONG).show();

		WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, this,
				"Posting....");

		wst.addNameValuePair("Itemprice", itempriceString);
		wst.addNameValuePair("Itemkeyword", itemkeywordString);
		wst.addNameValuePair("Itemname", itemnameString);
		
		if (discount_box.isChecked()) 
			discountnoString = discountno.getText().toString();
		if(discountnoString.equals("")){
			discountnoString="100";
		}
		if(picBoolean){
			wst.addNameValuePair("Picture", picName);
			UploadFileTask uploadFileTask=new UploadFileTask(this);
			uploadFileTask.execute(path);
		}else{
			wst.addNameValuePair("Picture", "21.jpg");
		}
		wst.addNameValuePair("Discountno", discountnoString);
		wst.addNameValuePair("sellerId", sellerId);
//		wst.addNameValuePair("Picture", pictureString);

		startTime=System.currentTimeMillis();
		wst.execute(new String[] { SERVICE_URL });

	}

	public void handleResponseLocal(String response) {
		if (!response.equals("ok")) {
			Toast.makeText(this, "Fail to save, Please save again.",
					Toast.LENGTH_LONG).show();

		} else {
			Toast.makeText(this, "Save successfully.", Toast.LENGTH_LONG)
					.show();

//			SaveBitMap.deleteFile("1");
			SysApplication.close(AddItemActivity.TAG);
			SysApplication.close(ItemManageActivity.TAG);
			Intent intent = new Intent(AddItemActivity.this,
					ItemManageActivity.class);
			startActivity(intent);
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
			InputMethodManager inputManager = (InputMethodManager) AddItemActivity.this
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			inputManager.hideSoftInputFromWindow(AddItemActivity.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

		}

		@Override
		public void handleResponse(String response) {
			// TODO Auto-generated method stub
			endTime=System.currentTimeMillis();
			MyLog.writeLog("General Add Text and Image Reuqest Response Time", (endTime-startTime) + "ms");
			handleResponseLocal(response);
		}

	}

	public void item_pic_button(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.item_pic:
			// Intent intent = new Intent(AddItemActivity.this,
			// CameraActivity.class);
			// startActivity(intent);
	//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			 fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
//			    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			    
			  
			
			
			Date date = new Date();
		    picName = sellerId.replaceAll("[^\\w]", "")+new SimpleDateFormat("yyyyMMdd_HHmmss")
			.format(date)+".jpg";
		    
		    path = FileUtil.getPath(picName);
			
			    File file = new File(path);
			    Uri outputFileUri = Uri.fromFile(file);

			    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			    
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high


//			    startActivityForResult(intent, TAKE_PICTURE); 
			
			startActivityForResult(intent, TAKE_PICTURE_WITH_CAMERA);
			break;

		}

	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
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
				
//				imageView.setImageBitmap(bitmap);
			}else{
			}
			 
//			 bitmap = BitmapFactory.decodeFile(path);
			 
//			selected_photo.setImageBitmap(bitmap);
			
//            Toast.makeText(this, "Image saved to: "+path
//            		, Toast.LENGTH_LONG).show();
            
            picBoolean = true;
        } else if (resultCode == RESULT_CANCELED) {
            // User cancelled the image capture
        } else {
            // Image capture failed, advise user
        }


			
		item_pic.setImageBitmap(bitmap);

	}
	
		   

}
