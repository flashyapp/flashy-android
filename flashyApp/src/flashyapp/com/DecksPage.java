package flashyapp.com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import flashyapp.com.JSONThread.OnResponseListener;

public class DecksPage extends Activity {
 
	private String sessionId;
	private String username;

	protected OnResponseListener onResponseListener = new OnResponseListener() {
		 public void onReturnRegister(String error, JSONObject jresponse){}
		 public void onReturnLogout(String error){}
		 public void onReturnSaveDeck(Context context, String mError, JSONObject jresponse){}
		 public void onReturnDeleteDeck(String error, Context context){}
		 public void onReturnLogin(String error, JSONObject jresponse, String name){}
		 public void onReturnDeckFromImage(Context context, String mError, JSONObject jresponse){}
		 public void onReturnDecksPage(String error, JSONObject jresponse, Context context){
			 JSONArray deckArray=null;
				try{
					
					if (error==null)
						deckArray=jresponse.getJSONArray("decks");
					
					
				
				}
				catch(Exception e) {
					 Log.d("Error", "Cannot turn response of getDecks to JSON");
					e.printStackTrace();
			       
			    }
				
				if (deckArray != null)
				{
			
					try {
				        //writing list of decks 
				        DataOutputStream out = 
				                new DataOutputStream(openFileOutput(MainActivity_LogIn.GETDECKS_FILE, Context.MODE_PRIVATE));
				        out.writeUTF(deckArray.toString());
				        out.close();
				        
				    	
				    }catch (IOException e) {
				        Log.i("Data Input Sample", "I/O Error");
				    }
					
					Intent intent =new Intent(context,DeckListMaker.class);
					intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION, sessionId);
					intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER, username);
					startActivity(intent);
				}
			 
			 
		 }
	};
	
	
	
	
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decks_page);
		
		Log.d("DEBUG", "in onCreate() in DecksPage");
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
		intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION, sessionId);
		intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER, username);
		startActivity(intent);
	}
	
	
	

	
	
	private void getDecks()
	{
		JSONArray jarray=null;
		
		 try {
			DataInputStream in = new DataInputStream(openFileInput(MainActivity_LogIn.GETDECKS_FILE));
		    try {
		    	String list=in.readUTF();
		    	if (list != null){
		    		Log.i("Data Input Decks in json list", list);
		            jarray=new JSONArray(list);
		    	}
		    			
		    		
		          
		        } catch (EOFException e) {
		            Log.i("Data Input Sample from MAIN", "End of getDecks_file reached");
		        }
		        in.close();
		    } catch (IOException e) {
		        Log.i("Data Input Sample", "I/O Error--getDecks_file isn't there!");
		        JSONThread thread=new JSONThread((Context)this, onResponseListener, JSONThread.GETDECKLIST);
		        if (thread.wifiOn()){
		        	thread.BeforeDecksPage(username,sessionId);
					thread.execute(new String[]{null});
		        }
		        else
		        	MyJSON.WifiAlert(this);
				return;
		    } catch (Exception e){
		    	Log.i("Data Input Sample", "Could not parse list into a JSONArray");
		    	return;
		    }
		
		if (jarray != null)
		{

			Log.d("BEFORE INTENT", "Before I call DeckListMakerIntent");
			Intent intent =new Intent(this,DeckListMaker.class);
			intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION, sessionId);
			intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER, username);
			intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_DECKLIST, jarray.toString());
			startActivity(intent);
			
		}
	
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

	
	/*public void newDeck(View view)
	{
		Intent intent=new Intent(this,NewDeck.class);
		intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION, sessionId);
	    intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER,username);
		startActivity(intent);
		
		
	}*/
	
	
	
}
