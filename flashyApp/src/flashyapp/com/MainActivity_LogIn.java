package flashyapp.com;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity_LogIn extends Activity {

	private EditText etName;
	private EditText etPswd;
	public final static String SESSION_FILE= "sessionId.txt";
	public final static String INTENT_EXTRA_DATA_SESSION = "sessionId";
	public final static String INTENT_EXTRA_DATA_REGISTER = "sessionId";
	public final static String INTENT_EXTRA_DATA_USER = "username";
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity__log_in);
		
		Intent intent=getIntent();
		String message = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_REGISTER);
		if (message != null)
		{
			TextView view=(TextView) findViewById(R.id.login_text);
			view.setText(message);
		}
		
		
		checkForSessionId();
		
		
		
		Log.d("DEGUG","Got into create method....");
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
		
		String name=etName.getText().toString();
		String pswd=etPswd.getText().toString();
		JSONObject loginJSON=new JSONObject();
		loginJSON=addString(loginJSON,"username",name);
		if (loginJSON==null){
			//do what?
		}
		loginJSON=addString(loginJSON,"password",pswd);
		if (loginJSON==null){
			//do what?
		}
		
		String url="http://www.flashyapp.com/api/user/login";
		HttpResponse httpResponse=sendJSONObject(loginJSON,url);
		String response=responseChecker(httpResponse);
		Log.d("LoginHttpResponse",response); 
		
		
		String sessionId=null;
		try{
			
			JSONObject jresponse=new JSONObject(response);
			sessionId=jresponse.getString("session_id");
			
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
		
		
		
		
		
	}
	
	
	private JSONObject addString(JSONObject json, String key, String value){
		try{
			json.put(key, value);
			return json;
		}
		catch(Exception e)
		{
			Log.e("JSON FAILURE!","JSON couldn't add the data");
			e.printStackTrace();
			return null;
			
		}
		
	}
	
	private String responseChecker(HttpResponse httpResponse){
		try{
			if(httpResponse!=null){
				InputStream instream = httpResponse.getEntity().getContent(); //Get the data in the entity
	            String result= convertStreamToString(instream);
	            // now you have the string representation of the HTML request        
	            instream.close();
	            return result;
			}
			else{
				Log.d("HttpResponse", "Response was NULL");
			}
		}
		catch(Exception e) {
	        e.printStackTrace();
	        Log.e("Error", "Cannot get response information");
	    }
		return null;
	}
	private HttpResponse sendJSONObject(JSONObject json, String url){
		Log.d("DEBUGGING JSON", json.toString());
		
		
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse;
			
			HttpPost httpPost = new HttpPost(url); 
			 StringEntity se = new StringEntity(json.toString());  
	         se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	         httpPost.setEntity(se);
	         Log.d("Debug","Before executing post");
	         httpResponse = httpClient.execute(httpPost);
	         return httpResponse;
	         
		} catch(Exception e) {
	        e.printStackTrace();
	        Log.e("Error", "Cannot Estabilish Connection");
	        return null;
	    }
		
		
		
	}
	
	
	
	
	
	private void registerIntent(){
		Intent intent = new Intent(this, Registration.class);
		startActivity(intent);
	}
	
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}

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


}


























