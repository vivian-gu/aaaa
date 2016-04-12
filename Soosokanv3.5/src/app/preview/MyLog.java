package app.preview;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

public class MyLog {

	private static final String NEW_LINE = System.getProperty("line.separator");
	public static boolean mLogcatAppender = true;
	static final File mLogFile = new File(
			Environment.getExternalStorageDirectory(), "Mylogs.txt");

	public static void init() {
		// mLogFile =
		if (!mLogFile.exists()) {
			try {
				mLogFile.createNewFile();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		 logDeviceInfo();
	}

	public static void i(String TAG, String message) {
		appendLog(TAG + " : " + message);
		if (mLogcatAppender) {
			android.util.Log.i(TAG, message);
		}
	}

	public static void d(String TAG, String message) {
		appendLog(TAG + " : " + message);
		if (mLogcatAppender) {
			android.util.Log.d(TAG, message);
		}
	}

	public static void e(String TAG, String message) {
		appendLog(TAG + " : " + message);
		if (mLogcatAppender) {
			android.util.Log.e(TAG, message);
		}
	}

	public static void v(String TAG, String message) {
		appendLog(TAG + " : " + message);
		if (mLogcatAppender) {
			android.util.Log.v(TAG, message);
		}
	}

	public static void w(String TAG, String message) {
		appendLog(TAG + " : " + message);
		if (mLogcatAppender) {
			android.util.Log.w(TAG, message);
		}
	}

	private static synchronized void appendLog(String text) {
		final SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");

		try {
			final FileWriter fileOut = new FileWriter(mLogFile, true);
			fileOut.append(sdf.format(new Date()) + " : " + text + NEW_LINE);
			fileOut.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeLog(String Type,String text){
		appendLog(Type+" : "+text);
	}

	private static void logDeviceInfo() {
//		appendLog("Model : " + android.os.Build.MODEL);
//		appendLog("Brand : " + android.os.Build.BRAND);
//		appendLog("Product : " + android.os.Build.PRODUCT);
//		appendLog("Device : " + android.os.Build.DEVICE);
//		appendLog("Codename : " + android.os.Build.VERSION.CODENAME);
//		appendLog("Release : " + android.os.Build.VERSION.RELEASE);
		appendLog("----------------------------------------------");
	}

}
