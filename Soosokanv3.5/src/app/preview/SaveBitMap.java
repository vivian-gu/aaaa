package app.preview;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class SaveBitMap {  
	  
    private final static String CACHE = "/soosokan";  
  
  
    /** 
     * 获取sd卡的缓存路径， 一般在卡中sdCard就是这个目录 
     *  
     * @return SDPath 
     */  
    public static String getSDPath() {  
        File sdDir = null;  
//        boolean sdCardExist = Environment.getExternalStorageState().equals(  
//                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在  
//        if (sdCardExist) {  
            sdDir = Environment.getExternalStorageDirectory();// 获取根目录  
//        } else {  
////            Log.e("ERROR", "没有内存卡");  
//        }  
        return sdDir.toString();  
    }  
  
    /** 
     * 获取缓存文件夹目录 如果不存在创建 否则则创建文件夹 
     *  
     * @return filePath 
     */  
    public static String isExistsFilePath() {  
        String filePath = getSDPath() + CACHE;  
        File file = new File(filePath);  
        if (!file.exists()) {  
            file.mkdirs();  
        }  
        return filePath;  
    }  
    /** 
     * 获取SDCard文件 
     *  
     * @return Bitmap 
     */  
    public static Bitmap getImageFromSDCard(String imageName) {  
        String filepath = getSDPath() + CACHE  + "/" + imageName+".jpg";  
//        System.out.println("get: "+filepath);
        File file = new File(filepath);  
        if (file.exists()) {  
            Bitmap bm = BitmapFactory.decodeFile(filepath);  
            return bm;  
        }  
        return null;  
    }  
    
    /** 
     * 获取SDCard文件 
     *  
     * @return Bitmap 
     */  
    public static Bitmap getImageCacheFromSDCard(String imageName) {  
        String filepath = getSDPath() + "soosokancache"  + "/" + imageName+".jpg";  
//        System.out.println("get: "+filepath);
        File file = new File(filepath);  
        if (file.exists()) {  
            Bitmap bm = BitmapFactory.decodeFile(filepath);  
            return bm;  
        }  
        return null;  
    }  
    
    public static String BitMapToString(Bitmap bitmap) throws IOException{
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
//        String newStr = temp;
        String newStr = new String(temp.getBytes(), "UTF-8"); 
//        newStr = ZipUtil.compress(newStr);
//        System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFf"+newStr);
        return newStr;
  }
    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public static Bitmap StringToBitMap(String encodedString){
   try{
	 //  System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSS:" + encodedString);
	   if(encodedString.startsWith("null")){
		   encodedString = encodedString.split("null")[1];
	   }
	   
	 //  encodedString = ZipUtil.unCompress(encodedString);
	   
	     byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
		 Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	     if(bitmap == null){
	    	 System.out.println("bitmap is null");
	     }
	     return bitmap;
   }catch(Exception e){
	   Log.d("your tag", "Exception has occurred: ", e);
	   return null;
   }
    }
    
    
    /** 
       * 删除SD卡或者手机的缓存图片和目录 
       */  
      public static void deleteFile(String filename) {  

          File dirFile = new File(Environment.getExternalStorageDirectory()  +"/soosokan"+filename+".jpg");  
         if(! dirFile.exists()){  
             return;  
          }  
          if (dirFile.isDirectory()) {  
              String[] children = dirFile.list();  
              for (int i = 0; i < children.length; i++) {  
                  new File(dirFile, children[i]).delete();  
              }  
          }  
           
          dirFile.delete();  
     }
      
//      public static String FileToString(String imageName){
//    	//对文件的操作  
//    	  FileInputStream in;
//    	  String result = null;
//		try {
//			in = new FileInputStream(getSDPath() + CACHE  + "/" + imageName+".jpg");
//			byte buffer[] = StreamUtil.read(in);//把图片文件流转成byte数组  
//	    	  byte[] encod = Base64.encode(buffer,Base64.DEFAULT);//使用base64编码
//	    	   result  = new String(encod); 
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//    	  
//
//		return result;
//      }

//	public static boolean saveBitmap2file(Bitmap bmp, String filename) {
//		CompressFormat format = Bitmap.CompressFormat.JPEG;
//		int quality = 100;
//		OutputStream stream = null;
//		try {
//			stream = new FileOutputStream("/sdcard/" + filename);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return bmp.compress(format, quality, stream);  
//         }
}  
