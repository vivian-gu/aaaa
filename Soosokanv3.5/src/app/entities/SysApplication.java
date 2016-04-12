package app.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Application;

public class SysApplication extends Application {
	private static Map<String,Activity> mList = new HashMap<String,Activity>();

	// add Activity
	public static void addActivity(Activity activity,String TAG) {
		mList.put(TAG, activity);
	}
	
	public static void close(String TAG){
		Activity a = mList.remove(TAG);
		a.finish();
	}
	
	public static void closeall(){
		int size = mList.size();
		System.out.println("All Activity : "+ size);
		Set<String> keySet = mList.keySet();
		Iterator iterator = keySet.iterator();
		while(iterator.hasNext()){
			Object key = iterator.next();
			Activity a = mList.get(key);
			a.finish();
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

}
