package flashyapp.com;

import java.io.File;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;


public class SaveResourceThread extends AsyncTask<String, String, String> {
	
    private Context mcontext;
    private JSONObject jresponse;
   
    private String url;
    private String mError;
    private OnResponseSaveResourceListener orsrl;
    private String sessionId;
    private String username;
    File mfile;
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
    
    public boolean wifiOn()
    {
    	return wifiOn;
    }

    
   
    public void BeforeSaveResource(String resourceName)
    {
    
    	mResourceName=resourceName;
    	
    	url="http://www.flashyapp.com/resources/"+resourceName;
		//MyJSON.HttpPost(url, imgFile,session,user);
		/*sessionId=session;
		username=user;
		*/
		Log.d("GET REQUEST URL: ", url);
		mError=null;
		
    }
    
    
    
    
    @Override
    protected String doInBackground(String... urls) {
    	
		//HttpResponse httpResponse=MyJSON.getResource(url);
		//String response=MyJSON.responseChecker(httpResponse);
		//HttpClient client=new HttpClient();
		
    	
    	
    	bitmap=null;
    	HttpEntity entity;
    	HttpResponse response;
    	InputStream inputStream=null;
		try{
			
			//jresponse=new JSONObject(response);
			response=MyJSON.getResource(url);
			//bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
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

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        //pbar.setVisibility(View.VISIBLE);
        progressDialog = ProgressDialog.show(mcontext, null, "Please wait...", true); 
        
    }
    
    
    
   

    @Override
    protected void onPostExecute(String result) {
    	progressDialog.cancel();
        /*pbar.setVisibility(View.INVISIBLE);*/
        /*String sessionId=null;*/
		//String error=null;
      try{  
       // mError=MyJSON.errorChecker(jresponse,mcontext);
		
       // Log.d("DEBUG", "RETURN String of errorChecker"+mError);
        
       
        orsrl.onReturnSaveResource( mcontext, bitmap, mSide, mCounter, mResourceName);
        	
       
			
        
      }
		catch(Exception e) {
			//morl.onFailure("FAILED GAH");
			 Log.d("Error", "Cannot turn response to JSON");
			e.printStackTrace();
	       
	    }
		
				
		/*if (sessionId != null){
			Log.d("SessionID",sessionId);
		}*/
    }

    @Override
    protected void onProgressUpdate(String... values) {
    	 super.onProgressUpdate(values);
    	//pbar.setProgress(Integer.parseInt(values[0]));
       
    }
    
    
	public interface OnResponseSaveResourceListener {
		public void onReturnSaveResource(Context context, Bitmap bitmap, String side, int counter, String name);
		
		}

    
    
   /* public String returnVal()
    {
    	return "THESE are Return values";
    }*/
}
