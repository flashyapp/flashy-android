package flashyapp.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import flashyapp.com.JSONThread.OnResponseListener;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

public class Registration extends Activity {
		
	private EditText etName;
	private EditText etPswd;
	private EditText etEmail;
	
	
	
	 protected OnResponseListener onResponseListener = new OnResponseListener() {
		 public void onReturnLogin(String error, JSONObject jresponse,String name) {
		 }
		 public void onReturnRegister(String error,JSONObject jresponse){
			 	/*int email_s=1;
				int password_s=1;
				int username_s=1;
				
				try{
					
					email_s=jresponse.getInt("email_s");
					password_s=jresponse.getInt("password_s");
					username_s=jresponse.getInt("username_s");
					
				}
				catch(Exception e) {
					 Log.d("Error", "Cannot turn response to JSON");
					e.printStackTrace();
			       
			    }*/
				
				/*if (email_s == 0)
					etEmail.setError("Email is invalid or has been used already");
				if (password_s == 0)
					etPswd.setError("Invalid Password");
				if (username_s ==0)
					etName.setError("Invalid Username");
				if(username_s == 1 && password_s == 1 && email_s == 1){*/
			 if (error != null){
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
		        	if(!hasFocus){
		              validateText(etName,"Username Required");
		        	}
		        	   
		        }
		    });
		 etPswd.setOnFocusChangeListener(new View.OnFocusChangeListener() {          
			 	@Override
		        public void onFocusChange(View v, boolean hasFocus) {
			 		
		        	if(!hasFocus){
		               validateText(etPswd,"Password Required");
		        		
		        	}
		        
		        }
		    });
		 etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {          
			 	@Override
		        public void onFocusChange(View v, boolean hasFocus) {
			 		
		        	if(!hasFocus){
		               validateText(etPswd,"Email Required");
		        		
		        	}
		        
		        }
		    });
		 
		 Button btnReg=(Button)findViewById(R.id.second_register_button);
		 
		 btnReg.setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		        	if(validateText(etName,"Username Required") && 	validateText(etPswd,"Password Required") 
		        			&& validateText(etEmail,"Email Required"))
					{
		        	Log.d("INTENTS","Email/register INTENT BEING CALLED by register/email BUTTON");
					returnHomeIntent();
					}
		        }
		    });
		 
		 
		 
		}
	   
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
	
	private void returnHomeIntent(){
		
		
		JSONThread thread=new JSONThread((Context)this, onResponseListener, JSONThread.REGISTER);
		thread.BeforeRegister(etName, etPswd,etEmail);
		thread.execute(new String[]{null});
		
/*
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
		String url="http://www.flashyapp.com/api/user/create_user";*/
		/*HttpResponse httpResponse=MyJSON.sendJSONObject(loginJSON,url);
		String response=MyJSON.responseChecker(httpResponse);
		Log.d("LoginHttpResponse",response); */
		/*int email_s=1;
		int password_s=1;
		int username_s=1;
		
		try{
			
			JSONObject jresponse=new JSONObject(response);
			email_s=jresponse.getInt("email_s");
			password_s=jresponse.getInt("password_s");
			username_s=jresponse.getInt("username_s");
			
		}
		catch(Exception e) {
			 Log.d("Error", "Cannot turn response to JSON");
			e.printStackTrace();
	       
	    }
		
		if (email_s == 0)
			etEmail.setError("Email is invalid or has been used already");
		if (password_s == 0)
			etPswd.setError("Invalid Password");
		if (username_s ==0)
			etName.setError("Invalid Username");
		if(username_s == 1 && password_s == 1 && email_s == 1){
			Intent intent = new Intent(this, MainActivity_LogIn.class);
			//put extra data and then on homescreen I can check for this extra data and display it saying
			//to check their email and then login
			//can pass in a sessionid or something on the intent 
			String message="You have registered but must now verify your registration by going to your email and confirming, then log in";
			intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_REGISTER, message);
			startActivity(intent);
		}*/
		
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
