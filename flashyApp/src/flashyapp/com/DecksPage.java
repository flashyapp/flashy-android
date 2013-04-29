package flashyapp.com;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class DecksPage extends Activity {
 
	private String sessionId;
	private String username;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decks_page);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		
		Intent intent =getIntent();
		sessionId = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION);
		username=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER);
		
		
		Log.d("SessionId-DECK",sessionId);
		Log.d("Username-DECK",username);
		
		
		getDecks();
	
	}
	
	public void LogoutFunc(View view)
	{
		Intent intent =new Intent(this,MyLogout.class);
		startActivity(intent);
	}
	
	
	

	
	
	private void getDecks()
	{
		JSONObject deckJSON=new JSONObject();
		deckJSON=MyJSON.addString(deckJSON,"username",username);
		if (deckJSON==null){
			//do what?
		}
	
		deckJSON=MyJSON.addString(deckJSON,"session_id",sessionId);
		if (deckJSON==null){
			//do what?
		}
		
		
		
		
	 
		String url="http://www.flashyapp.com/api/deck/get_decks";
		HttpResponse httpResponse=MyJSON.sendJSONObject(deckJSON,url);
		String response=MyJSON.responseChecker(httpResponse);
		Log.d("GET_DECKHttpResponse",response); 
		
		
		JSONArray deckArray=null;
		try{
			
			JSONObject jresponse=new JSONObject(response);
			deckArray=jresponse.getJSONArray("decks");
			
			
		
		}
		catch(Exception e) {
			 Log.d("Error", "Cannot turn response to JSON");
			e.printStackTrace();
	       
	    }
		
		
		if (deckArray != null)
		{
			handleDecks(deckArray);
			
		}
	
	}
	
	
	
	
	
	private void handleDecks(JSONArray deckArray)
	{
		ArrayList<String> deckIds=new ArrayList<String>();
		ArrayList<String> deckNames=new ArrayList<String>();
		LinearLayout layout=(LinearLayout)findViewById(R.id.deck_layout);
		
		for (int index=0; index<deckArray.length(); index++)
		{
			try{
				JSONObject tempDeck=deckArray.getJSONObject(index);
				String deckid=tempDeck.getString("deck_id");
				String name=tempDeck.getString("name");
				deckIds.add(index, deckid);
				deckNames.add(index,name);
				Log.d("DEBUG name",name);
				Log.d("DEBUG id",deckid);
				
				
				Button b=new Button(this);
				b.setText(name);
				b.setTag(deckid);
				b.setWidth(100);
				b.setHeight(50);
				b.setOnClickListener(new View.OnClickListener() {
		             public void onClick(View view) {
		                 // Perform action on click
		            	 Log.d("DEBUG clicked and got tag:", view.getTag().toString());
		            	 Intent intent =new Intent(view.getContext() , ViewDeck.class);
		            	 intent.putExtra("deckId",view.getTag().toString());
		            	 intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION, sessionId);
		         	    intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER,username);
		         		startActivity(intent);
		             }
		         });
				layout.addView(b);
				
				
				
			}
			catch(Exception e) {
				 Log.d("Error", "Cannot turn parse JSONArray of decks_list");
				e.printStackTrace();
		       
		    }
		}
		
		
		layout.invalidate();
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

	
	public void newDeck(View view)
	{
		
		
		Intent intent=new Intent(this,NewDeck.class);
		intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION, sessionId);
	    intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER,username);
		startActivity(intent);
		
		
	}
}
