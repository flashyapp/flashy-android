package flashyapp.com;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;



	public class JSONThread extends AsyncTask<String, String, String> {
		
		// a number system to find out which specific command is being called
		public static final int LOGIN=1;
		public static final int REGISTER=2;
		public static final int NEWDECK=3;
		public static final int GETDECKLIST=4;
		public static final int LOGOUT=5;
		public static final int VIEWDECK=6;
		public static final int DECKFROMIMAGE=7;
		public static final int SAVEDECK=8;
		public static final int DELETEDECK=9;
		
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
	    
	    //constructor
	    public JSONThread( Context context, OnResponseListener orl, int Type)
	    {
	    	
	    	mcontext=context;
	    	morl=orl;
	    	type=Type;
	    	//check that the wifi is on
	    	WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	    	wifiOn=false;
			if (wifi.isWifiEnabled()){
				wifiOn=true;
				Log.d("WIFI is actually on", "Wifi");
			}
			else
				wifiOn=false;
	    }
	    
	   // let caller know if wifi is on
	    public boolean wifiOn()
	    {
	    	return wifiOn;
	    }
	    
	    
	    // set up Login in HttpPost
	    public void BeforeLogin(EditText etName, EditText etPswd)
	    {
	    	
	    	String name=etName.getText().toString();
			String pswd=etPswd.getText().toString();
			
			JSONObject loginJSON=new JSONObject();
			loginJSON=MyJSON.addString(loginJSON,"username",name);
			loginJSON=MyJSON.addString(loginJSON,"password",pswd);
			
			username=name;
			url="http://www.flashyapp.com/api/user/login";
			jsend=loginJSON;
			mError=null;
	    	
	    }

	    // setup HttpPost to register
	    public void BeforeRegister(EditText etName, EditText etPswd, EditText etEmail)
	    {

			String name=etName.getText().toString();
			String pswd=etPswd.getText().toString();
			String email=etEmail.getText().toString();
			JSONObject loginJSON=new JSONObject();
			
			loginJSON=MyJSON.addString(loginJSON,"username",name);
			loginJSON=MyJSON.addString(loginJSON,"password",pswd);
			loginJSON=MyJSON.addString(loginJSON,"email",email);
			
			url="http://www.flashyapp.com/api/user/create_user";
			jsend=loginJSON;
			mError=null;
	   
	    }
	    
	    //set up the call to update the deck List
	    public void BeforeDecksPage(String user, String session)
	    {
	    	JSONObject deckJSON=new JSONObject();
			deckJSON=MyJSON.addString(deckJSON,"username",user);
			deckJSON=MyJSON.addString(deckJSON,"session_id",session);
			
			username=user;
			jsend=deckJSON;
			mError=null;
			url="http://www.flashyapp.com/api/deck/get_decks";
	    }
	    
	    //set up the call to get information about a specific deck
	    public void BeforeSaveDeck(String user, String session,String deckId)
	    {
	    	JSONObject resourceJSON=new JSONObject();
			resourceJSON=MyJSON.addString(resourceJSON,"username",user);
			resourceJSON=MyJSON.addString(resourceJSON,"session_id",session);
			
			username=user;
			jsend=resourceJSON;
			mError=null;
			url="http://www.flashyapp.com/api/deck/"+deckId+"/get";
			
	    }
	   
	    //set up a logout HttpPost
	    public void BeforeLogout(String user, String session)
	    {
	    	JSONObject logoutJSON=new JSONObject();
			logoutJSON=MyJSON.addString(logoutJSON,"username",user);
			logoutJSON=MyJSON.addString(logoutJSON,"session_id",session);
			
			username=user;
			jsend=logoutJSON;
			mError=null;
			url="http://www.flashyapp.com/api/user/logout";
	    }
	    
	    //set up an httpPost to delete a deck
	    public void BeforeDeleteDeck(String user, String session, String deckId)
	    {
	    	JSONObject deleteDeckJSON=new JSONObject();
			deleteDeckJSON=MyJSON.addString(deleteDeckJSON,"username",user);
			deleteDeckJSON=MyJSON.addString(deleteDeckJSON,"session_id",session);
			
			username=user;
			jsend=deleteDeckJSON;
			mError=null;
			url="http://www.flashyapp.com/api/deck/"+deckId+"/delete";
	    }
	    
	    //before the httpPost was created to submit the lines
	    public void BeforeLineSubmission(JSONObject param)
	    {
	    	url="http://www.flashyapp.com/api/deck/new/from_image";
	    	jsend=param;
	    	
	    }
	    
	    
	    // Every single request does this to actually execute the HttpPost
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

	    
	    //universal set up
	    @Override
	    protected void onPreExecute() {

	        super.onPreExecute();
	        progressDialog = ProgressDialog.show(mcontext, null, "Please wait...", true); 
	        
	    }
	    
	    
	    
	   
	    //universal termination
	    @Override
	    protected void onPostExecute(String result) {
	    	progressDialog.cancel();
	    	
	    //alert is done here because it can't be done from "doInBackground" method	
	     if (!wifiOn){
	    	 
		   MyJSON.WifiAlert(mcontext);
		    return;
	     }
	     //check for error from server
	      try{  
	        mError=MyJSON.errorChecker(jresponse,mcontext);
			
	        Log.d("DEBUG", "RETURN String of errorChecker"+mError);
	        // return appropriate information to appropriate caller 
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
	        	break;
	        case DECKFROMIMAGE:
	        	morl.onReturnDeckFromImage(mcontext,mError,jresponse);
	        	break;
	        case DELETEDECK:
	        	morl.onReturnDeleteDeck(mError,mcontext);
	        	break;
	        case SAVEDECK:
	        	morl.onReturnSaveDeck(mcontext,mError, jresponse);
	        	
			default:
				
	        }
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
	    
	    
		public interface OnResponseListener {
			public void onReturnLogin(String error, JSONObject jresponse,String name);
			public void onReturnRegister(String error, JSONObject jresponse);
			public void onReturnDecksPage(String error, JSONObject jresponse, Context context);
			public void onReturnLogout(String error);
			public void onReturnDeleteDeck(String error, Context context);
			public void onReturnDeckFromImage(Context context, String mError, JSONObject jresponse);
			public void onReturnSaveDeck(Context context, String mError, JSONObject jresponse);
			}
		
	}
	
