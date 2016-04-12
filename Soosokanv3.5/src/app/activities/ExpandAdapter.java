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
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.entities.NetworkProperties;
import app.preview.SaveBitMap;

import com.example.soosokanv3.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class ExpandAdapter extends BaseExpandableListAdapter {

	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/ads/adsPic";
    private Context mContext;
    private LayoutInflater mInflater = null;
    private String[]   mGroupStrings = {"Discount","Voucher","New Product","Other"};
    private JSONArray  mData = null;

    public ExpandAdapter(Context ctx, JSONArray list) {
        mContext = ctx;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //mGroupStrings = mContext.getResources().getStringArray(R.array.groups);
        mData = list;
    }

    public void setData(JSONArray list) {
        mData = list;
    }
    
    public Bitmap getCacheImage(String adsId) {
		Bitmap bitmap = null;
		bitmap = ImageDownLoader.showCacheBitmap(adsId);
		if (bitmap != null) {
			return bitmap;
		} else {
		}
		return bitmap;
	}
    
    public void initPic(final String adsId, final ImageView imageView) {
		Bitmap bitmap = getCacheImage(adsId);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.add("adsId", adsId);
			System.out.println("Ads pic request!!!!!!!!!!!!!!!!!!");
			// String response = null;
			client.get(SERVICE_URL, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
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

					if (!Response.equals(null)) {
						String response = byteToString(Response);
						// pic.put(itemId, response);
						// Toast.makeText(mContext, response, Toast.LENGTH_LONG)
						// .show();

						Bitmap bm = SaveBitMap.StringToBitMap(response);

						System.out.println("save bitmap test");
						ImageDownLoader.addBitmapToMemoryCache(adsId, bm);
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

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return mData.length();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        try {
			return mData.getJSONArray(groupPosition).length();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
        
    }

    @Override
    public JSONArray  getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        try {
			return mData.getJSONArray(groupPosition);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

    @Override
    public JSONObject getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        try {
			return mData.getJSONArray(groupPosition).getJSONObject(childPosition);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_item_layout, null);
        }
        GroupViewHolder holder = new GroupViewHolder();
        holder.mGroupName = (TextView) convertView
                .findViewById(R.id.group_name);
        holder.mGroupName.setText(mGroupStrings[groupPosition]);
        holder.mGroupCount = (TextView) convertView
                .findViewById(R.id.group_count);
        try {
			holder.mGroupCount.setText("[" + mData.getJSONArray(groupPosition).length() + "]");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			holder.mGroupCount.setText("["+0+"]");
		}
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.child_item_layout, null);
        }
        ChildViewHolder holder = new ChildViewHolder();
        holder.mIcon = (ImageView) convertView.findViewById(R.id.img);
        //holder.mIcon.setBackgroundResource(R.drawable.head);
        holder.mChildName = (TextView) convertView.findViewById(R.id.item_name);
        try {
			holder.mChildName.setText(getChild(groupPosition, childPosition).getString("title"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
        String picName = "--";
        holder.mDetail = (TextView) convertView.findViewById(R.id.item_detail);
        try {
			holder.mDetail.setText(getChild(groupPosition, childPosition).getString("description"));
			String adsId = getChild(groupPosition, childPosition).getString("adsId");
			picName = getChild(groupPosition, childPosition).getString("picture");
			System.out.println("Picture name:!!!!!!!"+picName);
			holder.mIcon.setTag(picName);
			new CanvasImageTask().execute(holder.mIcon);
			
//			initPic(adsId, holder.mIcon);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        
        return true;
    }

    private class GroupViewHolder {
        TextView mGroupName;
        TextView mGroupCount;
    }

    private class ChildViewHolder {
        ImageView mIcon;
        TextView mChildName;
        TextView mDetail;
    }

}
