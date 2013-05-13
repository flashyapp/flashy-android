package flashyapp.com;

import java.io.File;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import flashyapp.com.JSONThread.OnResponseListener;

public class MIMEThread extends AsyncTask<String, String, String> {
	
	
	
	
	
   
    private Context mcontext;
    private JSONObject jresponse;
   
    private String url;
    private String mError;
    private OnResponseMIMEListener morl;
    private String sessionId;
    private String username;
    private  File mfile;
    private boolean wifiOn;
    private ProgressDialog progressDialog;
    
    /*
     * constructor
     */
    public MIMEThread( Context context, OnResponseMIMEListener orl)
    {
    	
    	mcontext=context;
    	morl=orl;
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

    
   
    public void BeforeMakePic(String user, String session, String pathName)
    {
    
    	mfile = new  File(pathName);
    	
    	url="http://www.flashyapp.com/api/deck/new/upload_image";
		//MyJSON.HttpPost(url, imgFile,session,user);
		sessionId=session;
		username=user;
		
		
		mError=null;
		
    }
    
    
    @Override
    protected String doInBackground(String... urls) {
    	
		HttpResponse httpResponse=MyJSON.sendMIMEPost(url, mfile,sessionId,username);
		String response=MyJSON.responseChecker(httpResponse);
		Log.d("LoginHttpResponse",response); 
		
		
		jresponse=null;
		
		try{
			
			jresponse=new JSONObject(response);
			
			
				
		
		}
		catch(Exception e) {
			 Log.d("Error", "Cannot turn response to JSON");
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
        mError=MyJSON.errorChecker(jresponse,mcontext);
		
        Log.d("DEBUG", "RETURN String of errorChecker"+mError);
        
       
        morl.onReturnMIMEIn(mError,jresponse,username,mcontext);
        	
       
			
        
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
    
    
	public interface OnResponseMIMEListener {
		public void onReturnMIMEIn(String error, JSONObject jresponse,String name, Context context);
		
		}

    
    
   /* public String returnVal()
    {
    	return "THESE are Return values";
    }*/
}
