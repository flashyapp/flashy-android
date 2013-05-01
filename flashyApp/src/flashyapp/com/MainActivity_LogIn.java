package flashyapp.com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import flashyapp.com.JSONThread.OnResponseListener;

public class MainActivity_LogIn extends Activity {
	
	private EditText etName;
	private EditText etPswd;
	public final static String SESSION_FILE= "sessionId.txt";
	public final static String INTENT_EXTRA_DATA_SESSION = "sessionId";
	public final static String INTENT_EXTRA_DATA_REGISTER = "sessionId";
	public final static String INTENT_EXTRA_DATA_USER = "username";
	
	
	
	
	 protected OnResponseListener onResponseListener = new OnResponseListener() {
		 public void onReturnRegister(String error, JSONObject jresponse){}
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
				
				
				
				
				
			
				if (sessionId != null){
					Log.d("SessionID",sessionId);
					
				    //String fileName=SESSION_FILE;
				    try {
				        //writing SessionId
				        DataOutputStream out = 
				                new DataOutputStream(openFileOutput(SESSION_FILE, Context.MODE_PRIVATE));
				        out.writeUTF(sessionId);
				        out.writeUTF(name);
				      
				   
				    out.close();
				    }catch (IOException e) {
				        Log.i("Data Input Sample", "I/O Error");
				    }
					
					Intent intent = new Intent(MainActivity_LogIn.this, DecksPage.class);
					//can pass in a sessionid or something on the intent 
					intent.putExtra(INTENT_EXTRA_DATA_SESSION, sessionId);
					intent.putExtra(INTENT_EXTRA_DATA_USER, name);
					startActivity(intent);
				}
					
				else{
					//if sessionId isn't null....?
					//check for error codes etc
					Log.e("SessionId","No SessionId was found--Check the errors");
					Log.d("Alert Dialog", "Should have appeared!");
				}
				
			 
		 }
		 
	 };
	
	
	
	
	
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity__log_in);
		
		Log.d("DEGUG","Got into create method....");
		
		Intent intent=getIntent();
		String message = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_REGISTER);
		if (message != null)
		{
			TextView view=(TextView) findViewById(R.id.login_text);
			view.setText(message);
		}

		//htmlWrite();
		//htmlRead();
		
		
		/*FIX ADAM!*/
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		/*Make it so when a thread is happening, a layout gets invisible or rather gone is better*/
	    /*LinearLayout llb=(LinearLayout)findViewById(R.id.button_login_layout);
	    llb.setVisibility(View.INVISIBLE);*/
		
		
		checkForSessionId();
		
		
		
		
		 etName = (EditText) findViewById(R.id.login_name);
		 etPswd = (EditText) findViewById(R.id.password);
		 
		
		 
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
		 Button btnLogin=(Button)findViewById(R.id.login_button);
		 Button btnReg=(Button)findViewById(R.id.register_button);
		 btnLogin.setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		        	if(validateText(etName,"Username Required") && 	validateText(etPswd,"Password Required"))
					{
		        	Log.d("INTENTS","Login INTENT BEING CALLED by login BUTTON");
					loginIntent();
					}
		        }
		    });
		 btnReg.setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		        	if(validateText(etName,"Username Required") && 	validateText(etPswd,"Password Required"))
					{
		        	Log.d("INTENTS","REGISTER INTENT BEING CALLED by REGISTER BUTTON");
					registerIntent();
					}
		        }
		    });
		 
		 //drawingLines();
		 
		}
	        
	private void checkForSessionId(){
		//String fileName="sessionId.txt";
		 String sessionId=null;
		 String username=null;
	    try {
	        //reading SessionId
	       
	    
	    	DataInputStream in = new DataInputStream(openFileInput(SESSION_FILE));
	       //InputStream is=new InputStream(openFileInput(SESSION_FILE));
	        
	    	try {
	    //		username=in.readUTF();
	    		
	    			
	    		
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
	        return;
	    }
	    
	    if (sessionId != null){
		    Intent intent = new Intent(this, DecksPage.class);
			//can pass in a sessionid or something on the intent
		    intent.putExtra(INTENT_EXTRA_DATA_SESSION, sessionId);
		    intent.putExtra(INTENT_EXTRA_DATA_USER,username);
			startActivity(intent);
	    }
	    return;
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
	
	


	private void loginIntent(){
		
		JSONThread thread=new JSONThread((Context)this, onResponseListener, JSONThread.LOGIN);
		thread.BeforeLogin(etName, etPswd);
		thread.execute(new String[]{null});
		/*String sessionId=thread.AfterLogin();
		
		Log.d("SessionID After thread stuff","..."+sessionId);*/
		
		/*FIX ADAM*/
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		/*make threads more versatile and pass in a layout to make it invisible.*/
		
		
		/*String name=etName.getText().toString();
		String pswd=etPswd.getText().toString();
		JSONObject loginJSON=new JSONObject();
		loginJSON=MyJSON.addString(loginJSON,"username",name);
		if (loginJSON==null){
			//do what?
		}
		loginJSON=MyJSON.addString(loginJSON,"password",pswd);
		if (loginJSON==null){
			//do what?
		}
		
		String url="http://www.flashyapp.com/api/user/login";
		HttpResponse httpResponse=MyJSON.sendJSONObject(loginJSON,url);
		String response=MyJSON.responseChecker(httpResponse);
		Log.d("LoginHttpResponse",response); 
		
		
		String sessionId=null;
		String error=null;
		
		try{
			
			JSONObject jresponse=new JSONObject(response);
			error=MyJSON.errorChecker(jresponse,this);
			Log.d("DEBUG", "RETURN String of errorChecker"+error);
			
			if (error == null)// no error
			{
				sessionId=jresponse.getString("session_id");
			}
			else{
				AlertDialog.Builder ald=new AlertDialog.Builder(this);
				ald.setTitle("LogIn Error");
				ald.setMessage(error);
				ald.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d("Debug", "Alert Got Clicked");
					}
				});
				
				
				
				AlertDialog other=ald.create();
				other.show();
				Log.d("Debug", "Else statement of alert");
			}
			
				
		
		}
		catch(Exception e) {
			 Log.d("Error", "Cannot turn response to JSON");
			e.printStackTrace();
	       
	    }
		
		
		
		
		
	
		if (sessionId != null){
			Log.d("SessionID",sessionId);
			
		    //String fileName=SESSION_FILE;
		    try {
		        //writing SessionId
		        DataOutputStream out = 
		                new DataOutputStream(openFileOutput(SESSION_FILE, Context.MODE_PRIVATE));
		        out.writeUTF(sessionId);
		        out.writeUTF(name);
		      
		   
		    out.close();
		    }catch (IOException e) {
		        Log.i("Data Input Sample", "I/O Error");
		    }
			
			Intent intent = new Intent(this, DecksPage.class);
			//can pass in a sessionid or something on the intent 
			intent.putExtra(INTENT_EXTRA_DATA_SESSION, sessionId);
			intent.putExtra(INTENT_EXTRA_DATA_USER, name);
			startActivity(intent);
		}
			
		else{
			//if sessionId isn't null....?
			//check for error codes etc
			Log.e("SessionId","No SessionId was found--Check the errors");
		}
		
		*/
		
		
		
	}
	
	private void registerIntent(){
		Intent intent = new Intent(this, Registration.class);
		startActivity(intent);
	}
	
	//NOW INVALID!! NEW LINE CODE IN OTHER PROJECT!!!
	private void drawingLines()
	{
		Log.d("Drawing", "Got into drawing method");
		View view=this.findViewById(R.id.outer_login_layout);
		
		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				float p1= 30;
				float p2= 30;
				float p3=60;//event.getX();
				float p4=60;//event.getY();
				Log.d("Drawing", "Got into drawing method 2");
				 Paint p = new Paint();
		         p.setColor(Color.WHITE);
		         p.setStyle(Paint.Style.STROKE);
		         Canvas canvas = new Canvas();
		         canvas.drawColor(Color.BLUE);

		        canvas.drawLine(p1,p2,p3,p4,p);
				
				
				
				
				return true;
			}
		
		
		
		});
		//setContentView(view);
		
	}

	
	private void htmlWrite(){
		Log.d("WRITING HTML FILE","Before Writing html");
		
		File path = Environment.getExternalStorageDirectory();
		    //String StrPath=path.getPath();
		String photoName="cameraFile.jpg";
		
		File f = new File(path,photoName);
	    //was takePictureIntent
	    // String mCurrentPhotoPath = "\""+f.getAbsolutePath()+"\"";
	     String photoPath="file:///android_asset/FlashCardsPhoto.jpg";
	     
	    String fileName="Test.html";
	    String htmlString="<html><head><title>TITLeeeEE</title></head><body><i>Middle</i> of the<b> body!</b>" +
	    		"<img src="+photoPath+" alt=\"Ninja Pic\" > </body></html>";
	   //height="42" width="42"
	   
	    try {
	        //writing SessionId
	        DataOutputStream out = 
	                new DataOutputStream(openFileOutput(fileName, Context.MODE_PRIVATE));
	        out.writeUTF(htmlString);
	        Log.d("WRITING HTML FILE","After Writing html"+htmlString);
	       
	   
	    out.close();
	    }catch (IOException e) {
	        Log.i("Data Input Sample", "I/O Error");
	    }
	}
	
	private void htmlRead()
	{
		String htmlIn=null;
		 try {
		      DataInputStream in = new DataInputStream(openFileInput("Test.html"));
		        
		    	try {
		   
		    			htmlIn=in.readUTF();
		    			
		              	Log.i("HTML Input", htmlIn);
		    			
		    		
		          
		        } catch (EOFException e) {
		            Log.i("HTML Input Sample from MAIN", "End of file reached");
		        }
		        in.close();
		    } catch (IOException e) {
		        Log.i("HTML Input Sample", "I/O Error--file isn't there!");
		        return;
		    }
		
		if(htmlIn != null){
			
			Log.d("HTML Input", htmlIn);
			
			
			Intent intent = new Intent(this, ViewHTML.class);
			//can pass in html string
			intent.putExtra("html_extra", htmlIn);
			startActivity(intent);
			
			
		}
	}
	

}



























