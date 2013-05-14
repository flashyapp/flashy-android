package flashyapp.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;


// a class that has static methods to modularize Http Requests
public class MyJSON {

	// adds the key value pair to the JSON Object
	public static  JSONObject addString(JSONObject json, String key, String value){
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

	//takes the response from the server and makes that into a string which it returns
	public static String convertStreamToString(InputStream is) {

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

	
	//check the error of the json object and create a popup stating the error to the user
	public static String errorChecker(JSONObject json, Context context)
	{
		String string = null;
		Integer error=null;
		try{
			 error=(Integer)json.getInt("error");
			 Log.d("DEBUG", "ErrorCode: "+error);
		}
		catch (Exception e)
		{
			 Log.d("Error", "Cannot find an error code");
			 e.printStackTrace();
		}
		
		if (error != null)
		{
			String title=null;
			
			switch (error){
			case 0:
				string=null;
				break;
			case 1:
				string="Username is Invalid";
				title="Registration Error";
				break;
			case 2:
				string="Password is Invalid";
				title="Registration Error";
				break;
			case 3:
				string="Email is invalid or already in use";
				title="Registration Error";
				break;
			case 101:
				string="username or password is incorrect";
				title="Login Error";
				break;
			default:
				string="Error: Please check input";
				title="General Error";
			}
		
			
			// create the popup for the user
			if (string != null)
			{	
				Log.d("DEBUG", "inside alertDialogue box if statement:   " + string);
				
				AlertDialog.Builder ald=new AlertDialog.Builder(context);
				ald.setTitle(title);
				ald.setMessage(string);
				ald.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d("Debug", "Alert Got Clicked");
					}
				});
				
				AlertDialog other=ald.create();
				other.show();
				
			}
			
		}
		
		return string;
	}
	
	
	//convert the response to a a string with helper function
	public static String responseChecker(HttpResponse httpResponse){
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
	
	//Actually execute the HttpPost
	public static HttpResponse sendJSONObject(JSONObject json, String url){
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
	    
		}catch (HttpHostConnectException e2)
		{
			 Log.e("Error", "Cannot Estabilish Connection");
			 e2.printStackTrace();
			 return null;
		}
		catch(Exception e) {
	       
	        Log.d("Error sending in JSONPost", "Other http error");
	        e.printStackTrace();
	        return null;
	    }
		
		
		
	}
	
	
	
	//method specific to getting resources
	//performs a get request rather than a post
	public static HttpResponse getResource( String url){
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse;
			
			HttpGet httpGet = new HttpGet(url); 
			
	         Log.d("Debug","Before executing post");
	         httpResponse = httpClient.execute(httpGet);
	         return httpResponse;
	         
		} catch(Exception e) {
	        e.printStackTrace();
	        Log.e("Error", "Cannot Estabilish Connection");
	        return null;
	    }
		
		
		
	}
	
	
	// method specific to sending in an image to the server
	// handles HttpPost with image
	public static HttpResponse sendMIMEPost(String url, File f, String session, String name)
	{
		 	HttpClient httpClient = new DefaultHttpClient();
		 	HttpPost httpPost = new HttpPost(url); 

		    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

		    try{
		    	entity.addPart("file", new FileBody(f));
		    	entity.addPart("username", new StringBody(name));
		    	entity.addPart("session_id",new StringBody(session));
		    	httpPost.setEntity(entity);
		    
		    } catch (Exception e)
		    {
		    	Log.d("DEBUG","Error writing multipart");
		    }
		    
		    Log.d("DEBUGGING","before execute!");
		    HttpResponse response=null;
		    try {
		    	response = httpClient.execute(httpPost);
		        if (response != null)
		        	return response;
		     
		    } catch (ClientProtocolException e) {
		    	 Log.d("DEBUGGING","FAILEDDD on exception");
		        e.printStackTrace();
		     
		    } catch (IOException e) {
		    	Log.d("DEBUGGING","FAILEDDD on exception");
		        e.printStackTrace();
		    }
		    return null;
	}
	
	
	
	//Adds an Array of objects to a JSONObject
	public static JSONObject addArray(JSONObject json, String key, JSONObject[] value){
		JSONArray array=new JSONArray();
		try{
			for (int index =0; index<value.length; index++){
				array.put(value[index]);
			}
	
		}
		catch(Exception e)
		{
			Log.e("JSON FAILURE!","JSON couldn't add the data");
			e.printStackTrace();
			return null;
			
		}
		Log.d("JSONArray: ",array.toString());
		try{
			json.put(key, array);
			return json;
		}
		catch(Exception e)
		{
			Log.e("JSON FAILURE!","JSON couldn't add the data");
			e.printStackTrace();
			return null;
			
		}
		
	}
	
	//create a pop up for user that notifies that the wifi is off
	public static void WifiAlert(Context context)
	{
		try{
		Log.d("Wifi debugging","Got into No Wifi part");
		AlertDialog.Builder ald=new AlertDialog.Builder(context);
		String title="WIFI";
		String string="Turn on the Wifi";
		ald.setTitle(title);
		ald.setMessage(string);
		ald.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("Debug", "Alert Got Clicked");
			}
		});
		AlertDialog other=ald.create();
		other.show();
		}
		catch (Exception e)
		{
			Log.d("WIFI ISSUE", "What is going on?");
			e.printStackTrace();
		}
	}
	
	
}





