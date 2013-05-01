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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
				string="username is incorrect";
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
	         
		} catch(Exception e) {
	        e.printStackTrace();
	        Log.e("Error", "Cannot Estabilish Connection");
	        return null;
	    }
		
		
		
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
	
	
	
	
	
}





