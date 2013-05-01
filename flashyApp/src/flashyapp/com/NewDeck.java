package flashyapp.com;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

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
			
			card=MyJSON.addString(card,"sideA","front"+index);
			if (card==null){
				//do what?
			}
			card=MyJSON.addString(card,"sideB","back"+index);
			if (card==null){
				//do what?
			}
			Log.d("DEBUG each card JSON #"+index,card.toString());
			deck[index]=card;
		}
		
		JSONObject completeDeck=new JSONObject();
				
		completeDeck=MyJSON.addString(completeDeck,"username",username);
		if (completeDeck==null){
			//do what?
		}
		completeDeck=MyJSON.addString(completeDeck,"session_id",sessionId);
		if (completeDeck==null){
			//do what?
		}
		completeDeck=MyJSON.addArray(completeDeck,"cards",deck);
		if (completeDeck==null){
			//do what?
		}
		completeDeck=MyJSON.addString(completeDeck,"deck_name",deckname);
		if (completeDeck==null){
			//do what?
		}
		completeDeck=MyJSON.addString(completeDeck,"description",descrip);
		if (completeDeck==null){
			//do what?
		}
		Log.d("DEBUG sending in CompleteDeck JSON:",completeDeck.toString());
		
		String url="http://www.flashyapp.com/api/deck/new/from_lists";
		HttpResponse httpResponse=MyJSON.sendJSONObject(completeDeck,url);
		String response=MyJSON.responseChecker(httpResponse);
		Log.d("NewDeckHttpResponse",response); 
		
	
		String deckId=null;
		String error=null;
		try{
			
			JSONObject jresponse=new JSONObject(response);
			error=MyJSON.errorChecker(jresponse,this);
			deckId=jresponse.getString("deck_id");
		
		}
		catch(Exception e) {
			 Log.d("Error", "Cannot turn response to JSON");
			e.printStackTrace();
	       
	    }
	
		if (error != null)
		{
			TextView tv=new TextView(this);
			tv.setText(error);
			PopupWindow popup=new PopupWindow(tv);
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

	
	
	
	
	
	
	
}
