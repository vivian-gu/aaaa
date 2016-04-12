package app.location;

import android.location.Location;
import android.util.Log;

import java.util.Calendar;

public class MyUtil {

	private final static String TAG = "<-------MyUtil---------->";
	
	public static String formatLocation(Location location)
	{
		if (location == null)
		{
			return "location --> null";
		}
		
		

		
		String str = 	"provider = " + location.getProvider() + "\n" +
						", lon = " + location.getLongitude() + "\n" + 
						", lat = " + location.getLatitude() + "\n" + 
						", accuray = " + location.getAccuracy() + "\n" +
						", time = " + formatTimeMillis(location.getTime()) + "\n";
		
		
		
		return str;
	}
	
	
	 public static String formatTimeMillis(long time)
	 {
		 Calendar c = Calendar.getInstance();
		 c.setTimeInMillis(time);
			
		 int mHour = c.get(Calendar.HOUR_OF_DAY);
	     int mMinute = c.get(Calendar.MINUTE);
	     int mSecond = c.get(Calendar.SECOND);	
	     String str = "hour = " + mHour +
	     				", minute = " + mMinute + 
	     				", second = " + mSecond;
	     
	     return str;		 
	 }
	
	
	 public static void display(String str)
	 {
		 if (str != null)
		 {
			 Log.i(TAG, str);
		 }
	 }
}
