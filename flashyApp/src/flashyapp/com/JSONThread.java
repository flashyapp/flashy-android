package flashyapp.com;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;



	public class JSONThread extends AsyncTask<String, String, String> {
		public static final int LOGIN=1;
		public static final int REGISTER=2;
		public static final int NEWDECK=3;
		public static final int GETDECKLIST=4;
		public static final int LOGOUT=5;
		public static final int VIEWDECK=6;
		public static final int DECKFROMIMAGE=7;
		public static final int SAVERESOURCE=8;
		public static final int DELETEDECK=9;
		
		
		
		
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
	    private boolean wifiOn;
	    
	    /*
	     * constructor
	     */
	    public JSONThread( Context context, OnResponseListener orl, int Type)
	    {
	    	
	    	mcontext=context;
	    	morl=orl;
	    	type=Type;
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
	    
	    public void BeforeDecksPage(String user, String session)
	    {
	    	JSONObject deckJSON=new JSONObject();
			deckJSON=MyJSON.addString(deckJSON,"username",user);
			if (deckJSON==null){
				//do what?
			}
		
			deckJSON=MyJSON.addString(deckJSON,"session_id",session);
			if (deckJSON==null){
				//do what?
			}
			
			username=user;
			
			jsend=deckJSON;
			mError=null;
			url="http://www.flashyapp.com/api/deck/get_decks";
	    }
	    public void BeforeSaveResource(String user, String session,String resourceName)
	    {
	    	/*JSONObject resourceJSON=new JSONObject();
			resourceJSON=MyJSON.addString(resourceJSON,"username",user);
			if (resourceJSON==null){
				//do what?
			}
		
			resourceJSON=MyJSON.addString(resourceJSON,"session_id",session);
			if (resourceJSON==null){
				//do what?
			}*/
			/*
			 * 
			 * post resource name and w.e. else is needed!
			 * 
			 * 
			 */
			
			/*
			 * 
			 * FIX CASE STATEMENT AT BOTTOM AS WELL!!!!!
			 * 
			 */
			username=user;
			
			//jsend=resourceJSON;
			jsend=null;
			mError=null;
			url="www.flashyapp.com/resources/"+resourceName;
	    }
	    
	    /*public void BeforeMakePic(String user, String session, String pathName)
	    {
	    	JSONObject deckJSON=new JSONObject();
			deckJSON=MyJSON.addString(deckJSON,"username",user);
			if (deckJSON==null){
				//do what?
			}
		
			deckJSON=MyJSON.addString(deckJSON,"session_id",session);
			if (deckJSON==null){
				//do what?
			}
			*
			
	    	
	    
	    	File imgFile = new  File(pathName);
	    	
	    	url="http://www.flashyapp.com/api/deck/new/upload_image";
			MyJSON.HttpPost(url, imgFile,session,user);
			
			username=user;
			jsend=null;
			url=null;
			//jsend=deckJSON;
			mError=null;
			
	    }*/
	    public void BeforeLogout(String user, String session)
	    {
	    	JSONObject logoutJSON=new JSONObject();
			logoutJSON=MyJSON.addString(logoutJSON,"username",user);
			if (logoutJSON==null){
				//do what?
			}
		
			logoutJSON=MyJSON.addString(logoutJSON,"session_id",session);
			if (logoutJSON==null){
				//do what?
			}
			
			username=user;
			
			jsend=logoutJSON;
			mError=null;
			url="http://www.flashyapp.com/api/user/logout";
	    }
	    
	    public void BeforeDeleteDeck(String user, String session, String deckId)
	    {
	    	JSONObject deleteDeckJSON=new JSONObject();
			deleteDeckJSON=MyJSON.addString(deleteDeckJSON,"username",user);
			if (deleteDeckJSON==null){
				//do what?
			}
		
			deleteDeckJSON=MyJSON.addString(deleteDeckJSON,"session_id",session);
			if (deleteDeckJSON==null){
				//do what?
			}
			
			username=user;
			
			jsend=deleteDeckJSON;
			mError=null;
			url="http://www.flashyapp.com/api/deck/"+deckId+"/delete";
	    }
	    
	    public void BeforeLineSubmission(JSONObject param)
	    {
	    	url="http://www.flashyapp.com/api/deck/new/from_image";
	    	jsend=param;
	    	
	    }
	    
	    @Override
	    protected String doInBackground(String... urls) {
	    	
	    	if(wifiOn){
				HttpResponse httpResponse=MyJSON.sendJSONObject(jsend,url);
				if (httpResponse == null)
					return null;
				
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
	    	
	    	
	     if (!wifiOn){
	    	 
		   MyJSON.WifiAlert(mcontext);
		    return;
	     }
	      try{  
	        mError=MyJSON.errorChecker(jresponse,mcontext);
			
	        Log.d("DEBUG", "RETURN String of errorChecker"+mError);
	        
	        switch (type){
	        case LOGIN:
	        	morl.onReturnLogin(mError,jresponse,username);
	        	break;
	        case REGISTER:
	        	morl.onReturnRegister(mError,jresponse);
	        	break;
	        case GETDECKLIST:
	        	morl.onReturnDecksPage(mError, jresponse,mcontext);
	        	break;
	        case LOGOUT:
	        	morl.onReturnLogout(mError);
	        	break;
	        case VIEWDECK:
	        	/*
	        	 * 
	        	 * 
	        	 * 
	        	 */
	        	break;
	        case DECKFROMIMAGE:
	        	morl.onReturnDeckFromImage(mcontext,mError,jresponse);
	        	break;
	        case DELETEDECK:
	        	morl.onReturnDeleteDeck(mError,mcontext);
	        	break;
	       // case SAVERESOURCE:
	        	
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
			public void onReturnDecksPage(String error, JSONObject jresponse, Context context);
			public void onReturnLogout(String error);
			public void onReturnDeleteDeck(String error, Context context);
			public void onReturnDeckFromImage(Context context, String mError, JSONObject jresponse);
			
			}

	    
	    
	   /* public String returnVal()
	    {
	    	return "THESE are Return values";
	    }*/
	}
	
