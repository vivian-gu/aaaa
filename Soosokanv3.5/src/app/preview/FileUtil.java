package app.preview;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	private static final  String TAG = "FileUtil";
	private static final File parentPath = Environment.getExternalStorageDirectory();
	private static   String storagePath = "";
	private static String cachePath="";
	private static final String DST_FOLDER_NAME = "soosokan";
	private static final String IMAGE_CACHE_DIR = "soosokancache";

	/**��ʼ������·��
	 * @return
	 */
	public static String initPath(){
		if(storagePath.equals("")){
			storagePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME;
			File f = new File(storagePath);
			if(!f.exists()){
				f.mkdir();
			}
		}
		return storagePath;
	}

	
	public long getFileSize(String fileName) {
		return new File(cachePath+ fileName).length();
	}

	
	
	
	public static String getPath(String filename){
		String path = initPath();
	    String filepath = path + "/" +filename; 
	    
	    System.out.println("save"+filepath);

	    return filepath;
	}
	
	public static void saveBitmap2file(Bitmap b,String filename){

		String path = initPath();
		//String dataTake ="1";
//		String jpegName = path + "/" +filename; System.out.println("save"+jpegName);
//		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		File f = new File(path+"/",filename);
		try {
			if(f.exists()){
				f.delete();
			}
			FileOutputStream fout = new FileOutputStream(f);
			//BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, fout);
//			bos.flush();
			fout.close();
			Log.i(TAG, "saveBitmap Success!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "saveBitmap Fail!");
			e.printStackTrace();
		}
//		return jpegName;

	}
	
	/**save bitmap to sd card,from cache
	 * @param b
	 */
	public static void saveCacheBitmap(Bitmap b,String itemId){
		
		if(cachePath.equals("")){
			cachePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME;
			File f = new File(cachePath);
			if(!f.exists()){
				f.mkdir();
			}
		}
		String path = cachePath;
		//String dataTake ="1";
		String jpegName = path + "/" + itemId +".jpg"; System.out.println("save"+jpegName);
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			Log.i(TAG, "saveBitmap Success!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "saveBitmap Fail!");
			e.printStackTrace();
		}

	}


}
