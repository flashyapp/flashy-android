package flashyapp.com;


import org.json.JSONObject;

import flashyapp.com.JSONThread.OnResponseListener;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

public class Registration extends Activity {
		
	private EditText etName;
	private EditText etPswd;
	private EditText etEmail;
	
	
	
	 protected OnResponseListener onResponseListener = new OnResponseListener() {
		 public void onReturnLogin(String error, JSONObject jresponse,String name) {}
		 public void onReturnLogout(String error){}
		 public void onReturnSaveDeck(Context context, String mError, JSONObject jresponse){}
		 public void onReturnDeleteDeck(String error, Context context){}
		 public void onReturnDeckFromImage(Context context, String mError, JSONObject jresponse){}
		 public void onReturnDecksPage(String error, JSONObject jresponse, Context context){}
		 public void onReturnRegister(String error,JSONObject jresponse){
			 // after you have registered, return to login
			 if (error == null){
					Intent intent = new Intent(Registration.this, MainActivity_LogIn.class);
					//put extra data and then on homescreen I can check for this extra data and display it saying
					//to check their email and then login
					//can pass in a sessionid or something on the intent 
					String message="You have registered but must now verify your registration by going to your email and confirming, then log in";
					intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_REGISTER, message);
					startActivity(intent);
				}
		 }
	 };
	
	
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		Log.d("DEGUG","Got into create method....");
		 etName = (EditText) findViewById(R.id.login_name_register);
		 etPswd = (EditText) findViewById(R.id.password_register);
		 etEmail=(EditText) findViewById(R.id.email_register);
		 
		
		 etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {          
			 	@Override
		        public void onFocusChange(View v, boolean hasFocus) {
			 		if(hasFocus){
			              EditText et=(EditText)v;
			              et.setError(null);
			        	}  
		        }
		    });
		 etPswd.setOnFocusChangeListener(new View.OnFocusChangeListener() {          
			 	@Override
		        public void onFocusChange(View v, boolean hasFocus) {
			 		
			 		if(hasFocus){
			              EditText et=(EditText)v;
			              et.setError(null);
			        	}
		        
		        }
		    });
		 etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {          
			 	@Override
		        public void onFocusChange(View v, boolean hasFocus) {
			 		
			 		if(hasFocus){
			              EditText et=(EditText)v;
			              et.setError(null);
			        	}
		        
		        }
		    });
		 
		 Button btnReg=(Button)findViewById(R.id.second_register_button);
		 
		 btnReg.setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		        	//all three fields have to have text to be submitted
		        	if(validateText(etName,"Username Required") && 	validateText(etPswd,"Password Required") 
		        			&& validateText(etEmail,"Email Required"))
					{
		        	Log.d("INTENTS","Email/register INTENT BEING CALLED by register/email BUTTON");
					returnHomeIntent();
					}
		        }
		    });
		 
		 
		 
		}
	   
	//make sure a field is filled, or place an error if it isn't
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
	
	
	
	//call function to register and then return to login screen
	private void returnHomeIntent(){
		
		
		JSONThread thread=new JSONThread((Context)this, onResponseListener, JSONThread.REGISTER);
		 if (thread.wifiOn()){
		thread.BeforeRegister(etName, etPswd,etEmail);
		thread.execute(new String[]{null});
		 }
		 else
			 MyJSON.WifiAlert(this);

	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
