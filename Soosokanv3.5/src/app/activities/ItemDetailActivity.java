package app.activities;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.entities.NetworkProperties;
import app.entities.SysApplication;

import com.example.soosokanv3.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ItemDetailActivity extends Activity {

	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/item/byItemId";
	private TextView itemName, itemPrice, itemKeyword, itemDiscountno;
	private ImageView ItemPic;
	private String itemId, itemid, sellerId, name, keyword, pictureName;
	// private ImageDownLoader mImageDownLoader;
	private float price;
	private int discount;
	// private Bitmap bm;
	public static final String TAG = "ITEMDETAIL";

	public ItemDetailActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		SysApplication.addActivity(this, TAG);
		// mImageDownLoader = new ImageDownLoader();
		Intent intent = this.getIntent();
		itemId = intent.getStringExtra("itemId");
		itemName = (TextView) this.findViewById(R.id.itemName);
		itemPrice = (TextView) this.findViewById(R.id.itemPrice);
		itemKeyword = (TextView) this.findViewById(R.id.itemKeyword);
		itemDiscountno = (TextView) this.findViewById(R.id.itemDiscountno);
		ItemPic = (ImageView) this.findViewById(R.id.ItemPic);

		// initPic(itemId, ItemPic);
		getOneItem(itemId);
	}

	private void getOneItem(String itemId) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("itemId", itemId);
		client.get(SERVICE_URL, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
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
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String response = NetworkProperties.byteToString(arg2);

				JSONObject myJsonObject;
				try {
					myJsonObject = new JSONObject(response);
					itemid = myJsonObject.getString("itemId");
					sellerId = myJsonObject.getString("sellerId");
					name = myJsonObject.getString("name");
					keyword = myJsonObject.getString("keyword");
					pictureName = myJsonObject.getString("picture");

					String priceString = myJsonObject.getString("price");
					price = Float.valueOf(priceString);

					String discountString = myJsonObject.getString("discount");
					discount = Integer.valueOf(discountString);

					itemName.setText(name);
					itemPrice.setText(String.valueOf(price));
					itemKeyword.setText(keyword);
					itemDiscountno.setText(String.valueOf(discount));
					initPic(pictureName, ItemPic);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public Bitmap getCacheImage(String itemId) {
		Bitmap bitmap = null;
		bitmap = ImageDownLoader.showCacheBitmap(itemId);
		if (bitmap != null) {
			return bitmap;
		} else {
		}
		return bitmap;
	}

	public void initPic(final String picName, final ImageView imageView) {
		imageView.setTag(picName);
		new CanvasImageTask().execute(imageView);

	}

	public void itemmodify(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("itemId", itemid);
		intent.setClass(ItemDetailActivity.this, ModItemActivity.class);
		startActivity(intent);
		ItemDetailActivity.this.finish();
	}

	public void itemdelete(View v) {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("Delete Item")
				.setMessage("Sure to Delete?")
				.setIcon(R.drawable.ic_launcher)
				// �൱�ڵ��ȷ�ϰ�ť
				.setPositiveButton("Sure",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// �˴����delete����
								// SysApplication.close(ItemDetailActivity.TAG);
								Intent intent = new Intent(
										ItemDetailActivity.this,
										DeleteItemActivity.class);
								intent.putExtra("itemId", itemId);
								startActivity(intent);
							}
						})
				// �൱�ڵ��ȡ����ť
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Toast.makeText(ItemDetailActivity.this,
										"Delete cancel!", Toast.LENGTH_LONG)
										.show();
							}
						}).create();
		dialog.show();

	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	// //���µ������BACK��ͬʱû���ظ�
	// // SysApplication.close(ItemManageActivity.TAG);
	// Intent intent = new Intent();
	// intent.setClass(ItemDetailActivity.this,ItemManageActivity.class);
	// startActivity(intent);
	// return true;
	// }
	//
	// return super.onKeyDown(keyCode, event);
	// }

}
