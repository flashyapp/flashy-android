package flashyapp.com;

import java.io.BufferedReader;
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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewDeck extends Activity {

	private String deckId;
	private String username;
	private String sessionId;
	private JSONArray deckArray;
	private int index;
	private ArrayList<String> sideA;
	private ArrayList<String> sideB;
	private TextView cardView;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_deck);
		// Show the Up button in the action bar.
		Intent intent =getIntent();
		deckId = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_DECKID);
		sessionId = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION);
		username=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER);
		
		
		Log.d("deckId in DeckView:", deckId+ "    "+username + sessionId);
		deckArray=null;
		index=0;
		cardView=(TextView)findViewById(R.id.deckView_textView);
		cardView.setTag("A");
		
		
		loadDeck();
		
	}

	
	
	
	private void loadDeck(){
		
		JSONObject deckJSON=new JSONObject();
		deckJSON=MyJSON.addString(deckJSON,"username",username);
		if (deckJSON==null){
			//do what?
		}
	
		deckJSON=MyJSON.addString(deckJSON,"session_id",sessionId);
		if (deckJSON==null){
			//do what?
		}
		
		
		
		
	 
		String url="http://www.flashyapp.com/api/deck/"+deckId+"/get";
		HttpResponse httpResponse=MyJSON.sendJSONObject(deckJSON,url);
		String response=MyJSON.responseChecker(httpResponse);
		Log.d("GET_DECKHttpResponse",response); 
		
		
		
		try{
			
			JSONObject jresponse=new JSONObject(response);
			deckArray=jresponse.getJSONArray("cards");
			
			
		
		}
		catch(Exception e) {
			 Log.d("Error", "Cannot get response to JSON from Deck_get");
			e.printStackTrace();
	       
	    }
		
		makeIndividualCards();
		cardView.setWidth(200);
		cardView.setHeight(100);
		cardView.setTextColor(Color.RED);
		cardView.setTextSize(50);
		
		presentCardAction();
		
		
		
	}
	
	
	private void makeIndividualCards()
	{
		sideA=new ArrayList<String>();
		sideB=new ArrayList<String>();
		for (int i=0; i<deckArray.length(); i++)
		{
			try{
				JSONObject tempDeck=deckArray.getJSONObject(i);
				String Aside=tempDeck.getString("sideA");
				String Bside=tempDeck.getString("sideB");
				String cardIndex=tempDeck.getString("index");
				//int pos=Integer.parseInt(cardIndex);
				
				
				sideA.add(i, Aside);
				sideB.add(i,Bside);
				Log.d("DEBUG sideA",Aside);
				Log.d("DEBUG sideB",Bside);
				
			
			}
			catch(Exception e) {
				 Log.d("Error", "Cannot turn parse JSONArray of decks_list");
				e.printStackTrace();
		       
		    }
		}
		
		
		
	}
	public void flipOver(View view)
	{
		String tag=(String)cardView.getTag();
		
		if (tag.equals("A"))
			cardView.setTag("B");
		else
			cardView.setTag("A");
		presentCardAction();
	}
	
	public void nextCard(View view)
	{
		if (index < sideA.size()-1)
			index++;
		else
			index=0;
		presentCardAction();
	}
	private void presentCardAction()
	{
		
		String tag=(String)cardView.getTag();
		String text;
		if (tag.equals("A"))
			text=sideA.get(index);
		else
			text=sideB.get(index);
		cardView.setText(text);
		LinearLayout layout=(LinearLayout)findViewById(R.id.ViewDeck_layout);
		layout.invalidate();
		
	}
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_deck, menu);
		return true;
	}

	
	
	

}
