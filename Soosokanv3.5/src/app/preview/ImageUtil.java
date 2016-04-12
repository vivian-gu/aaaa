package app.preview;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

public class ImageUtil {
//	/**
//	 * ��תBitmap
//	 * 
//	 * @param b
//	 * @param rotateDegree
//	 * @return
//	 */
//	public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
//		Matrix matrix = new Matrix();
//		matrix.postRotate((float) rotateDegree);
//		Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
//				b.getHeight(), matrix, false);
//		return rotaBitmap;
//	}


	public static Bitmap getSmallBitmap() {  
        final BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        String path = FileUtil.initPath();
		String dataTake ="1";
		String jpegName = path + "/" + dataTake +".jpg";
        BitmapFactory.decodeFile(jpegName, options);  
  
        // Calculate inSampleSize  
    options.inSampleSize = calculateInSampleSize(options, 720, 1080);  
  
        // Decode bitmap with inSampleSize set  
    options.inJustDecodeBounds = false;  
  
    return BitmapFactory.decodeFile(jpegName, options);  
    }  

//	public static Bitmap compressBitmap(Bitmap bitmap, float size) {
//		if (bitmap == null || getSizeOfBitmap(bitmap) <= size) {
//			return bitmap;// 如果图片本身的大小已经小于这个大小了，就没必要进行压缩
//		}
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 如果签名是png的话，则不管quality是多少，都不会进行质量的压缩
//		int quality = 100;
//		while (baos.toByteArray().length / 1024f > size) {
//			quality = quality - 4;// 每次都减少4
//			baos.reset();// 重置baos即清空baos
//			if (quality <= 0) {
//				break;
//			}
//			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//			//Log.e("------质量--------" + baos.toByteArray().length / 1024f);
//		}
//		return bitmap;
//	} 
	public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
	
//	@SuppressLint("NewApi")
//	protected static float getSizeOfBitmap(Bitmap data) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
//            return data.getRowBytes() * data.getHeight();
//        } else {
//            return data.getByteCount();
//        }
//    }
	
	/**
	 * 把bitmap转换成String
	 * 
	 * @param filePath
	 * @return
	 */
//	public static String bitmapToString(String filePath) {
//
//		Bitmap bm = getSmallBitmap(filePath);
//		
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
//		byte[] b = baos.toByteArray();
//		
//		return Base64.encodeToString(b, Base64.DEFAULT);
//		
//	}

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	

	
//	/**
//	 * 根据路径获得突破并压缩返回bitmap用于显示
//	 * 
//	 * @param imagesrc
//	 * @return
//	 */
//	public static Bitmap getSmallBitmap(String filePath) {
//		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(filePath, options);
//
//		// Calculate inSampleSize
//		options.inSampleSize = calculateInSampleSize(options, 480, 800);
//
//		// Decode bitmap with inSampleSize set
//		options.inJustDecodeBounds = false;
//
//		return BitmapFactory.decodeFile(filePath, options);
//	}

	/**
	 * 根据路径删除图片
	 * 
	 * @param path
	 */
	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
	
	public static Bitmap compression(Bitmap bm){
		//Bitmap bm = PictureUtil.getSmallBitmap(path);	
		
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        
//		FileOutputStream fos = new FileOutputStream(new File(PictureUtil.getAlbumDir(),
//				"small01_" + f.getName()));
		
		int options = 100;  
		// 如果大于80kb则再次压缩,最多压缩三次
		while (baos.toByteArray().length / 1024 > 80 && options != 10) { 
			// 清空baos
			baos.reset();  
			// 这里压缩options%，把压缩后的数据存放到baos中  
			bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 30;
		}
		return bm;
	}

	/**
	 * 添加到图库
	 */
	public static void galleryAddPic(Context context, String path) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}

	/**
	 * 获取保存图片的目录
	 * 
	 * @return
	 */
	public static File getAlbumDir() {
		File dir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getAlbumName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 获取保存 隐患检查的图片文件夹名称
	 * 
	 * @return
	 */
	public static String getAlbumName() {
		return "sheguantong";
	}
}
