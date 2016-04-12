package app.activities;

import java.util.concurrent.ExecutorService;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import app.entities.NetworkProperties;
import app.preview.SaveBitMap;

public class ImageDownLoader {
	/**
	 * The calss to save the Image to cache
	 */
	private static LruCache<String, Bitmap> mMemoryCache;
	//private static FileUtil fileUtils;
	private static ExecutorService mImageThreadPool = null;
	private static final String SERVICE_URL = NetworkProperties.nAddress
			+ "/item/itemPic";
	private static Context context;
	
	public static void initImageLoader(){
		int maxMemory = (int) Runtime.getRuntime().maxMemory();  
        int mCacheSize = maxMemory / 8;
        //LruCache 1/8 cache 4M
		mMemoryCache = new LruCache<String, Bitmap>(mCacheSize){

			//the size of bitmap
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
			
		};
	}
	
	
	public static void initContext(Context contexts){
		System.out.println("Init ImageDownloader test!!!!!!!!!!!!");
		context=contexts;
		//fileUtils = new FileUtils(context);
	}
	
	
//	public static ExecutorService getThreadPool(){
//		if(mImageThreadPool == null){
//			synchronized(ExecutorService.class){
//				if(mImageThreadPool == null){
//					mImageThreadPool = Executors.newFixedThreadPool(2);
//				}
//			}
//		}
//		
//		return mImageThreadPool;
//		
//	}
	
	/**
	 * Save Bitmap to cache
	 * @param key
	 * @param bitmap
	 */
	public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {  
	    if (getBitmapFromMemCache(key) == null && bitmap != null) {  
	        mMemoryCache.put(key, bitmap);  
	    }  
	}  
	 
	/**
	 * get bitmap from cache
	 * @param key
	 * @return
	 */
	public static Bitmap getBitmapFromMemCache(String key) { 
		Bitmap bitmap = null;
		try{
			bitmap = mMemoryCache.get(key);
			System.out.println(bitmap);
		}catch(Exception e){}
		
	    return bitmap;  
	} 
	
//	/**
//	 * get image from cache => get image from sd card => download image
//	 * @param url
//	 * @param listener
//	 * @return
//	 */
//	public Bitmap downloadImage(final String itemId,final ImageView imageView, final onImageLoaderListener listener){
//		
//		//final String subUrl = url.replaceAll("[^\\w]", "");
//		Bitmap bitmap = showCacheBitmap(itemId);
//		if(bitmap != null){
//			return bitmap;
//		}else{
//			
//			final Handler handler = new Handler(){
//				@Override
//				public void handleMessage(Message msg) {
//					super.handleMessage(msg);
//					listener.onImageLoader((Bitmap)msg.obj, itemId);
//				}
//			};
//			
//			getThreadPool().execute(new Runnable() {
//				
//				@Override
//				public void run() {
//					Bitmap bitmap = getBitmapFormUrl(itemId,imageView);
//					Message msg = handler.obtainMessage();
//					msg.obj = bitmap;
//					handler.sendMessage(msg);
//					
//					try {
//						//save to sd card or device directory
//						fileUtils.savaBitmap(itemId, bitmap);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					
//					//save bitmap to cache
//					addBitmapToMemoryCache(itemId, bitmap);
//				}
//			});
//		}
//		
//		return null;
//	}
	
	/**
	 * get bitmap, cache=>sd card
	 * @param url
	 * @return
	 */
	public static Bitmap showCacheBitmap(String itemId){
		//if()
		Bitmap bitmap = null;
		bitmap = getBitmapFromMemCache(itemId);
		try{
			if( !bitmap.equals("")&& !bitmap.equals(null)){
				return getBitmapFromMemCache(itemId);
			}
		}catch(Exception e){
			if(SaveBitMap.getImageCacheFromSDCard(itemId)!= null){
				//get Bitmap from sd card
				System.out.println("get image cache test form sd card");
				bitmap = SaveBitMap.getImageCacheFromSDCard(itemId);
				//put Bitmap to cache
				addBitmapToMemoryCache(itemId, bitmap);
				return bitmap;
			}
		}
		//System.out.println("cacacaca"+bitmap.toString());
		
		
		return null;
	}
	

	
}
