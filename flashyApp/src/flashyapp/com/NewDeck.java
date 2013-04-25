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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class NewDeck extends Activity {

	private String sessionId;
	private String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_deck);
		// Show the Up button in the action bar.
		
		
		
		
		Intent intent =getIntent();
		sessionId = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION);
		username=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER);
	}

	
	
	
	public void fromListSubmit(View view)
	{
		EditText etname=(EditText) findViewById(R.id.new_deck_name);
		EditText etdescrip=(EditText) findViewById(R.id.new_deck_description);
		String deckname=etname.getText().toString();
		String descrip=etdescrip.getText().toString();
		
		Log.d("DEBUG inputted name",deckname);
		Log.d("DEBUG inputted descrip",descrip);
		
		JSONObject[] deck=new JSONObject[3];
		
		for (int index=0; index<3; index++)
		{
			JSONObject card = new JSONObject();
			
			card=addString(card,"sideA","front"+index);
			if (card==null){
				//do what?
			}
		
			card=addString(card,"sideB","back"+index);
			if (card==null){
				//do what?
			}
			
			Log.d("DEBUG each card JSON #"+index,card.toString());
			deck[index]=card;
		
		}
		
		JSONObject completeDeck=new JSONObject();
		
		
		completeDeck=addString(completeDeck,"username",username);
		if (completeDeck==null){
			//do what?
		}
		
		completeDeck=addString(completeDeck,"session_id",sessionId);
		if (completeDeck==null){
			//do what?
		}
		
		completeDeck=addArray(completeDeck,"cards",deck);
		if (completeDeck==null){
			//do what?
		}
		completeDeck=addString(completeDeck,"deck_name",deckname);
		if (completeDeck==null){
			//do what?
		}
		completeDeck=addString(completeDeck,"description",descrip);
		if (completeDeck==null){
			//do what?
		}
		
		Log.d("DEBUG sending in CompleteDeck JSON:",completeDeck.toString());
		
		String url="http://www.flashyapp.com/api/deck/new/from_lists";
		HttpResponse httpResponse=sendJSONObject(completeDeck,url);
		String response=responseChecker(httpResponse);
		Log.d("NewDeckHttpResponse",response); 
		
	
		String deckId=null;
		try{
			
			JSONObject jresponse=new JSONObject(response);
			deckId=jresponse.getString("deck_id");
			
		}
		catch(Exception e) {
			 Log.d("Error", "Cannot turn response to JSON");
			e.printStackTrace();
	       
	    }
	
		if (deckId != null){
			Log.d("DeckID",deckId+"");
		}

		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_deck, menu);
		return true;
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
	
	
	private JSONObject addArray(JSONObject json, String key, JSONObject[] value){
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
