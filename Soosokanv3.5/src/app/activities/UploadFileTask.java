package app.activities;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;


public class UploadFileTask extends AsyncTask<String, Void, String>{
//	public static final String requestURL="http://soosokanimage.cloudapp.net/AndroidUploadFileWeb/FileImageUploadServlet";
	public static final String requestURL="http://soosokanimage.cloudapp.net/AndroidUploadFileWeb/FileImageUploadServlet";

   private  ProgressDialog pdialog;
   private  Activity context=null;
    public UploadFileTask(Activity ctx){
    	this.context=ctx;
//    	pdialog=ProgressDialog.show(context, "Posting...", "Please wait");  
    }
    @Override
    protected void onPostExecute(String result) {
        
        pdialog.dismiss(); 
        if(UploadUtils.SUCCESS.equalsIgnoreCase(result)){
        	Toast.makeText(context, "success!",Toast.LENGTH_LONG ).show();
        }else{
        	Toast.makeText(context, "fail!",Toast.LENGTH_LONG ).show();
        }
    }

	  @Override
	  protected void onPreExecute() {
	  }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

		@Override
		protected String doInBackground(String... params) {
			File file=new File(params[0]);
			return UploadUtils.uploadFile( file, requestURL);
		}
	       @Override
	       protected void onProgressUpdate(Void... values) {
	       }

	
}