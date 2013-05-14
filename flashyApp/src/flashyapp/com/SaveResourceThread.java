package flashyapp.com;


import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

//Thread used specifically for getting resources
public class SaveResourceThread extends AsyncTask<String, String, String> {
	
    private Context mcontext;
    private String url;
    private OnResponseSaveResourceListener orsrl;
    private String mSide;
    private ProgressDialog progressDialog;
    private int mCounter;
    private Bitmap bitmap;
    private String mResourceName;
    private Boolean wifiOn;
    
    /*
     * constructor
     */
    public SaveResourceThread( Context context, OnResponseSaveResourceListener orl, String side, int counter)
    {
    	
    	mcontext=context;
    	orsrl=orl;
    	mSide=side;
    	mCounter=counter;
    	WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    	wifiOn=false;
		if (wifi.isWifiEnabled()){
			wifiOn=true;
			Log.d("WIFI is actually on", "Wifiiiiiiiii");
		}
		else
			wifiOn=false;
    }
    
    //return if wifi is on
    public boolean wifiOn()
    {
    	return wifiOn;
    }

    
   // function before HttpGet request
    public void BeforeSaveResource(String resourceName)
    {
    	mResourceName=resourceName;
    	url="http://www.flashyapp.com/resources/"+resourceName;
		Log.d("GET REQUEST URL: ", url);
		
    }
    
    
    
    // actually execute the HttpGet request
    @Override
    protected String doInBackground(String... urls) {
    	
    	bitmap=null;
    	HttpEntity entity;
    	HttpResponse response;
    	InputStream inputStream=null;
		try{
			
			response=MyJSON.getResource(url);
			entity=response.getEntity();
			inputStream=entity.getContent();
			bitmap = BitmapFactory.decodeStream(inputStream);
			
		
		}
		catch(Exception e) {
			 Log.d("Error", "Cannot decode Bitmap from resource download");
			e.printStackTrace();
	       
	    }
		
        return null;
    }

    
    //set up progress bar
    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog = ProgressDialog.show(mcontext, null, "Please wait...", true); 
        
    }
    
    
    
   

    @Override
    protected void onPostExecute(String result) {
    	progressDialog.cancel();
      try{  
        orsrl.onReturnSaveResource( mcontext, bitmap, mSide, mCounter, mResourceName); 
      }
		catch(Exception e) {
			 Log.d("Error", "Cannot turn response to JSON");
			e.printStackTrace();
	    }
    }

    
    @Override
    protected void onProgressUpdate(String... values) {
    	 super.onProgressUpdate(values);
    	//pbar.setProgress(Integer.parseInt(values[0]));
       
    }
    
    // call back function, defines what is returned to main thread
	public interface OnResponseSaveResourceListener {
		public void onReturnSaveResource(Context context, Bitmap bitmap, String side, int counter, String name);
		
		}

}
