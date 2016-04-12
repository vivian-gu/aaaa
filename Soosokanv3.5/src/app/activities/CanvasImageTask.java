package app.activities;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;
import app.entities.NetworkProperties;

public class CanvasImageTask extends AsyncTask<ImageView, Void, Bitmap> {
	private ImageView gView;

	public Bitmap getCacheImage(String itemId) {
		Bitmap bitmap = null;
		bitmap = ImageDownLoader.showCacheBitmap(itemId);
		if (bitmap != null) {
			return bitmap;
		} else {
		}
		return bitmap;
	}

	protected Bitmap doInBackground(ImageView... views) {
		Bitmap bmp = null;
		ImageView view = views[0];
		this.gView = view;
		String subUrl = null;
		if (view.getTag() != null) {
			try {
//				if (URLUtil.isHttpUrl(view.getTag().toString())) {
					String url = NetworkProperties.picStorage + view.getTag().toString();
					
					String temp = url.toString();
					subUrl = temp.replaceAll("[^\\w]", "");
					bmp = getCacheImage(subUrl);

					if (bmp != null) {
						System.out.println("get image from cache");
						return bmp;
					}

					
					URL mImageUrl = new URL(url);
					HttpURLConnection con = (HttpURLConnection) mImageUrl.openConnection();
					
					con.setDoInput(true);
					con.connect();
					InputStream is = con.getInputStream();
					bmp = BitmapFactory.decodeStream(is);
					is.close();
					
					
			} catch (Exception e) {
				Log.v("img", e.getMessage());
				return null;
			}
		}
		if (bmp != null) {
			ImageDownLoader.addBitmapToMemoryCache(subUrl, bmp);
		}

		return bmp;
	}

	protected void onPostExecute(Bitmap bm) {
		if (bm != null) {
			this.gView.setImageBitmap(bm);
			this.gView = null;
		}
	}

}