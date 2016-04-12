package app.activities;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.entities.NetworkProperties;
import app.preview.MyLog;
import app.preview.SaveBitMap;

import com.example.soosokanv3.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ItemListAdapter extends BaseAdapter {

	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/item/itemPic";
	private Context mContext;
	private long startTime,endTime; 
	// private ImageDownLoader mImageDownLoader;

	private LayoutInflater mInflater = null;
	// private Map<String, String> pic = new HashMap<String, String>();
	// private String[] mGroupStrings = {"Food","Wearing","Daliy Use","Others"};
	private JSONArray mData = null;

	public ItemListAdapter(Context ctx, JSONArray list) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mData = list;
		// mImageDownLoader = new ImageDownLoader(ctx);
	  
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

	public void initPic(final String itemId, final ImageView imageView) {
		Bitmap bitmap = getCacheImage(itemId);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.add("itemId", itemId);
			// String response = null;
			startTime=System.currentTimeMillis();
			client.get(SERVICE_URL, params, new AsyncHttpResponseHandler() {

				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					 endTime=System.currentTimeMillis();
					 MyLog.writeLog("General Picture Reuqest Failure Response Time", (endTime-startTime) + "ms");
					if (arg0 == 404)
						Toast.makeText(mContext,
								"Requested Resource not found",
								Toast.LENGTH_LONG).show();
					else if (arg0 == 500)
						Toast.makeText(mContext,
								"Something wrong at the server end",
								Toast.LENGTH_LONG).show();
					else
						Toast.makeText(mContext, "Unexpected error",
								Toast.LENGTH_LONG).show();
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] Response) {

					 endTime=System.currentTimeMillis();
					 MyLog.writeLog("General Picture Reuqest Success Response Time", (endTime-startTime) + "ms");
					if (!Response.equals(null)) {
						String response = byteToString(Response);
						// pic.put(itemId, response);
						// Toast.makeText(mContext, response, Toast.LENGTH_LONG)
						// .show();

						Bitmap bm = SaveBitMap.StringToBitMap(response);

						System.out.println("save bitmap test");
						ImageDownLoader.addBitmapToMemoryCache(itemId, bm);
						//FileUtil.saveCacheBitmap(bm, itemId);

						imageView.setImageBitmap(bm);
					}
				}

				private String byteToString(byte[] response) {
					String a = "";
					for (int i = 0; i < response.length; i++)
						a += (char) response[i];
					return a;
				}

			});
		}
	}

	public void setData(JSONArray list) {
		mData = list;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	private class ViewHolder {
		TextView mName;
		TextView mPrice;
		ImageView mIcon;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		System.out.println("JsonArray lenght:"+mData.length());
		return mData.length();
	}

	@Override
	public JSONObject getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return mData.getJSONObject(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_item, null);
		}

		ViewHolder holder = new ViewHolder();
		holder.mName = (TextView) convertView.findViewById(R.id.row_ItemName);
		holder.mPrice = (TextView) convertView.findViewById(R.id.row_ItemPrice);
		holder.mIcon = (ImageView) convertView.findViewById(R.id.itempic);
		convertView.setTag(holder);

		String price = "--";
		String name = "--";
		String itemId = "--";
		String url = "--";
		
//		Bitmap bitmap = ImageDownLoader.showCacheBitmap(itemId);
//		if (bitmap != null) {
//			holder.mIcon.setImageBitmap(bitmap);
//		} else {
//			holder.mIcon.setImageDrawable(mContext.getResources().getDrawable(
//					R.drawable.ic_empty));
//		}

		try {
			url = getItem(position).getString("picture");
			price = getItem(position).getString("price");
			name = getItem(position).getString("name");
			itemId = getItem(position).getString("itemId");
			
//			initPic(itemId, holder.mIcon);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final String subUrl = url.replaceAll("[^\\w]", "");
		Bitmap bitmap = ImageDownLoader.showCacheBitmap(subUrl);
		
		if (bitmap != null) {
			holder.mIcon.setImageBitmap(bitmap);
		} else {
			holder.mIcon.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.ic_empty));
		}

		holder.mIcon.setTag(url);
		System.out.println("HAHAHAHAH:"+url);
		new CanvasImageTask().execute(holder.mIcon);
		
		holder.mName.setText(name);

		holder.mPrice.setText(price);
		// holder.mIcon.setImageBitmap(bm);
		return convertView;
	}
}
