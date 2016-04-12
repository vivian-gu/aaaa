package app.preview;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class ImageThumbnail {
	
	 public static int reckonThumbnail(int oldWidth, int oldHeight, int newWidth, int newHeight) {
	        if ((oldHeight > newHeight && oldWidth > newWidth)
	                || (oldHeight <= newHeight && oldWidth > newWidth)) {
	            int be = (int) (oldWidth / (float) newWidth);
	            if (be <= 1)
	                be = 1;
	            return be;
	        } else if (oldHeight > newHeight && oldWidth <= newWidth) {
	            int be = (int) (oldHeight / (float) newHeight);
	            if (be <= 1)
	                be = 1;
	            return be;
	        }
	        return 1;
	    }
	 
	 
	 public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {    
	        //旋转图片 动作     
	        Matrix matrix = new Matrix();;    
	        matrix.postRotate(angle);    
	        System.out.println("angle2=" + angle);    
	        // 创建新的图片     
	        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,    
	                bitmap.getWidth(), bitmap.getHeight(), matrix, true);    
	        return resizedBitmap;    
	    }  
	 
	 public static int readPictureDegree(String path) {    
	       int degree  = 0;    
	       try {    
	               ExifInterface exifInterface = new ExifInterface(path);    
	               int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);    
	               switch (orientation) {    
	               case ExifInterface.ORIENTATION_ROTATE_90:    
	                       degree = 90;    
	                       break;    
	               case ExifInterface.ORIENTATION_ROTATE_180:    
	                       degree = 180;    
	                       break;    
	               case ExifInterface.ORIENTATION_ROTATE_270:    
	                       degree = 270;    
	                       break;    
	               }    
	       } catch (IOException e) {    
	               e.printStackTrace();    
	       }    
	       return degree;    
	   } 
	 
	    public static Bitmap PicZoom(Bitmap bmp, int width, int height) {
	        int bmpWidth = bmp.getWidth();
	        int bmpHeght = bmp.getHeight();
	        Matrix matrix = new Matrix();
	        matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);

//	        matrix.setRotate(90);
	        return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
	    }

}
