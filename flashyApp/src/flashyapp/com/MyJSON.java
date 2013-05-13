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
import android.net.wifi.WifiManager;
import android.util.Log;

public class MyJSON {



	
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
				string="error from server";
				title="Server Error";
				break;
			case 101:
				string="username or password is incorrect";
				title="Login Error";
				break;
			default:
				string="other error from server";
				title="General Error";
			}
			
			
			
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
			Log.d("POPUPPP", "Popup should've appeared"+error+string);
			
		}
		
		return string;
	}
	
	
	
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
	       
	        Log.e("Error", "Other http error");
	        e.printStackTrace();
	        return null;
	    }
		
		
		
	}
	public static HttpResponse getResource( String url){
		//Log.d("DEBUGGING JSON", json.toString());
		
		
		
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
	public static HttpResponse sendMIMEPost(String url, File f, String session, String name)
	{
		 	HttpClient httpClient = new DefaultHttpClient();

		    
		    HttpPost httpPost = new HttpPost(url); 

		    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			 Log.d("BEFORE READING", "RIGHT BEFORE I READ");
		    
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
		    	
		    	// writing exception to log
		        e.printStackTrace();
		     
		    } catch (IOException e) {
		    	Log.d("DEBUGGING","FAILEDDD on exception");
		        // writing exception to log
		        e.printStackTrace();
		    }
		    return null;
	}
	
	
	
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





