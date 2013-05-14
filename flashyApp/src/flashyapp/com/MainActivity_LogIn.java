package flashyapp.com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import flashyapp.com.JSONThread.OnResponseListener;

public class MainActivity_LogIn extends Activity {
	
	private EditText etName;
	private EditText etPswd;
	
	//store constants for use throughout the activities
	public final static String SESSION_FILE="sessionId.txt";
	public final static String GETDECKS_FILE="getDecks.txt";
	public final static String INTENT_EXTRA_DATA_SESSION = "sessionId";
	public final static String INTENT_EXTRA_DATA_REGISTER = "sessionId";
	public final static String INTENT_EXTRA_DATA_USER = "username";
	public final static String INTENT_EXTRA_DATA_DECKID="deckId";
	public final static String INTENT_EXTRA_DATA_DECKLIST="deckList";
	public final static String FILE_DIR="/flashyapp/";
	public final static String CAMERA_FILE="cameraFile.jpg";
	
	
	 protected OnResponseListener onResponseListener = new OnResponseListener() {
		 public void onReturnRegister(String error, JSONObject jresponse){}
		 public void onReturnLogout(String error){}
		 public void onReturnDeleteDeck(String error, Context context){}
		 public void onReturnSaveDeck(Context context, String mError, JSONObject jresponse){}
		 public void onReturnDeckFromImage(Context context, String mError, JSONObject jresponse){}
		 public void onReturnDecksPage(String error, JSONObject jresponse, Context context){}
		 public void onReturnLogin(String error, JSONObject jresponse,String name) {
			 String sessionId=null;
			 	
			 try{
					if (error == null)// no error
					{
						sessionId=jresponse.getString("session_id");
					}
					
						
				
				}
				catch(Exception e) {
					 Log.d("Error", "Error reading 'ERROR' from LoginRequest ");
					e.printStackTrace();
			       
			    }
			// save session Id and username to a file on the phone	
			 if (sessionId != null){
					Log.d("SessionID",sessionId);
					
				    //String fileName=SESSION_FILE;
				    try {
				        //writing SessionId
				    	DataOutputStream out = 
				                new DataOutputStream(openFileOutput(SESSION_FILE,Context.MODE_PRIVATE));
				        out.writeUTF(sessionId);
				        out.writeUTF(name);
				        out.close();
				    }catch (IOException e) {
				        Log.i("Data Input Sample", "I/O Error");
				        e.printStackTrace();
				    }
					
					Intent intent = new Intent(MainActivity_LogIn.this, DecksPage.class);
					intent.putExtra(INTENT_EXTRA_DATA_SESSION, sessionId);
					intent.putExtra(INTENT_EXTRA_DATA_USER, name);
					startActivity(intent);
				}
				
				else{
					
					Log.e("SessionId","No SessionId was found--Check the errors");
				}
				
			 
		 }
		 
	 };
	
	 @Override
		//prevents the up/back key from working
		public boolean onKeyDown(int keyCode, KeyEvent event) {
		     if (keyCode == KeyEvent.KEYCODE_BACK) {
		     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
		     return true;
		     }
		     return super.onKeyDown(keyCode, event);    
		}
	
	
	
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity__log_in);
		
		Log.d("DEGUG","In onCreate() for MainActivity");
		
		Intent intent=getIntent();
		String message = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_REGISTER);
		if (message != null)
		{
			TextView view=(TextView) findViewById(R.id.login_text);
			view.setText(message);
		}

		
		checkForSessionId();
		
		etName = (EditText) findViewById(R.id.login_name);
		etPswd = (EditText) findViewById(R.id.password);
		 
		
		 
		 etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {          
			 	@Override
		        public void onFocusChange(View v, boolean hasFocus) {
		        	if(hasFocus){
		              EditText et=(EditText)v;
		              et.setError(null);
		        	}
		        	   
		        }
		    });
		
		 Button btnLogin=(Button)findViewById(R.id.login_button);
		 Button btnReg=(Button)findViewById(R.id.register_button);
		 btnLogin.setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		        	// make sure name and password are filled in
		        	if(validateText(etName,"Username Required") && 	validateText(etPswd,"Password Required"))
					{
		        	Log.d("INTENTS","Login INTENT BEING CALLED by login BUTTON");
					loginIntent();
					}
		        }
		    });
		 btnReg.setOnClickListener(new OnClickListener() {
		        
			 
			 public void onClick(View v) {
		        	
		        	Log.d("INTENTS","REGISTER INTENT BEING CALLED by REGISTER BUTTON");
					registerIntent();
		        }
		    });
		 
		 
		}
	        
	private void checkForSessionId(){
		
		 String sessionId=null;
		 String username=null;
	    try {
	        //reading SessionId

	    	DataInputStream in = new DataInputStream(openFileInput(SESSION_FILE));
	       
	        
	    	try {
	    			sessionId=in.readUTF();
	    			username=in.readUTF();
	    		
	            	Log.i("Data Input Username", username);
	              	Log.i("Data Input Session", sessionId);
	    			
	    	
	          
	        } catch (EOFException e) {
	            Log.i("Data Input Sample from MAIN", "End of file reached");
	        }
	        in.close();
	    } catch (IOException e) {
	        Log.i("Data Input Sample", "I/O Error--file isn't there!");
	        //there is no file which is fine. just return
	        return;
	    }
	    
	    
	    if (sessionId != null){
	    	//if you have the sessionId just login
	    	Log.d("DEBUG","Before entering DecksPage");
		    Intent intent = new Intent(this, DecksPage.class);
		    intent.putExtra(INTENT_EXTRA_DATA_SESSION, sessionId);
		    intent.putExtra(INTENT_EXTRA_DATA_USER,username);
			startActivity(intent);
	    }
	    return;
	}
			
	
	//validate that the EditText has actual text
	// put the error on the EditText if it doesn't have text
	private boolean validateText(EditText et,String error){
		String str=et.getText().toString();
		if (str.equalsIgnoreCase("")){
			et.setError(error);
			return false;
		}
			return true;
	}
	

	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity__log_in, menu);
		return true;
	}
	
	

	//call the login JSONThread functions
	private void loginIntent(){
		
		JSONThread thread=new JSONThread((Context)this, onResponseListener, JSONThread.LOGIN);
		 if (thread.wifiOn()){
			 thread.BeforeLogin(etName, etPswd);
			 thread.execute(new String[]{null});
		 }
		 else
			 MyJSON.WifiAlert(this);
		
		
		
	}
	
	//go to the Register Page
	private void registerIntent(){
		Intent intent = new Intent(this, Registration.class);
		startActivity(intent);
	}
	
	

}



























