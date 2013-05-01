package flashyapp.com;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;



	public class JSONThread extends AsyncTask<String, String, String> {
		public static final int LOGIN=1;
		public static final int REGISTER=2;
		public static final int NEWDECK=3;
		public static final int GETDECKLIST=4;
		
		
		
		
	   /*private ProgressBar pbar;*/
	    //ProgressBar pbar;
	    private Context mcontext;
	    private JSONObject jresponse;
	    private JSONObject jsend;
	    private String url;
	    private String mError;
	    private OnResponseListener morl;
	    private String username;
	    private int type;
	    private ProgressDialog progressDialog;
	    
	    /*
	     * constructor
	     */
	    public JSONThread( Context context, OnResponseListener orl, int Type)
	    {
	    	
	    	mcontext=context;
	    	morl=orl;
	    	type=Type;
	    }
	    
	   
	    public void BeforeLogin(EditText etName, EditText etPswd)
	    {
	    	
	    	String name=etName.getText().toString();
			String pswd=etPswd.getText().toString();
			username=name;
			JSONObject loginJSON=new JSONObject();
			loginJSON=MyJSON.addString(loginJSON,"username",name);
			if (loginJSON==null){
				//do what?
			}
			loginJSON=MyJSON.addString(loginJSON,"password",pswd);
			if (loginJSON==null){
				//do what?
			}
			
			url="http://www.flashyapp.com/api/user/login";
			jsend=loginJSON;
			mError=null;
	    	
	    }

	    
	    public void BeforeRegister(EditText etName, EditText etPswd, EditText etEmail)
	    {

			String name=etName.getText().toString();
			String pswd=etPswd.getText().toString();
			String email=etEmail.getText().toString();
			JSONObject loginJSON=new JSONObject();
			loginJSON=MyJSON.addString(loginJSON,"username",name);
			if (loginJSON==null){
				//do what?
			}
			loginJSON=MyJSON.addString(loginJSON,"password",pswd);
			if (loginJSON==null){
				//do what?
			}
			loginJSON=MyJSON.addString(loginJSON,"email",email);
			if (loginJSON==null){
				//do what?
			}
			url="http://www.flashyapp.com/api/user/create_user";
			jsend=loginJSON;
			mError=null;
	    	
	    }
	    
	    
	    
	    
	    @Override
	    protected String doInBackground(String... urls) {
	    	
	    	
	    	
	    	
	    	
	    	
			HttpResponse httpResponse=MyJSON.sendJSONObject(jsend,url);
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
	        
	        switch (type){
	        case LOGIN:
	        	morl.onReturnLogin(mError,jresponse,username);
	        	break;
	        case REGISTER:
	        	morl.onReturnRegister(mError,jresponse);
			default:
				
	        }
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
	    
	    
		public interface OnResponseListener {
			public void onReturnLogin(String error, JSONObject jresponse,String name);
			public void onReturnRegister(String error, JSONObject jresponse);
			//public void onFailure(String message);
			}

	    
	    
	   /* public String returnVal()
	    {
	    	return "THESE are Return values";
	    }*/
	}
	
