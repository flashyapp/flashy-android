package flashyapp.com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import flashyapp.com.JSONThread.OnResponseListener;
import flashyapp.com.MIMEThread.OnResponseMIMEListener;



//This class is the main screen when a user has logged in before.
//It shows the list of decks and allows for the creation and deletion of decks
public class DeckListMaker extends Activity {
	private String username;
	private String sessionId;
	private JSONArray jarray;
	private String mCurrentPhotoPath;
	
	public static boolean DeckLongClickOn;
	
	
	
	
	
	
	protected OnResponseListener onResponseListener = new OnResponseListener() {
		 public void onReturnRegister(String error, JSONObject jresponse){}
		 public void onReturnSaveDeck(Context context, String mError, JSONObject jresponse){}
		 public void onReturnLogout(String error){}
		 public void onReturnDeleteDeck(String error, Context context){
			 Log.d("RETURNED", "From Delete Operation");
			 
			 //After the deck has been deleted, send a JSON request to get the new revised list of decks
			 JSONThread thread=new JSONThread(context, onResponseListener, JSONThread.GETDECKLIST);
			 if (thread.wifiOn()){
				 thread.BeforeDecksPage(username,sessionId);
				 thread.execute(new String[]{null});
			 }
			 else
				 MyJSON.WifiAlert(context);
		 }
		 public void onReturnDeckFromImage(Context context, String mError, JSONObject jresponse){ }
		 public void onReturnDecksPage(String error, JSONObject jresponse, Context context){
			 //Turn JSONResponse of the deck list into a file on the phone and then start up the 
			 //deck list page
			 JSONArray deckArray=null;
				try{
					if (error==null)
						deckArray=jresponse.getJSONArray("decks");
				}
				catch(Exception e) {
					 Log.d("Error", "Cannot turn response to JSON");
					e.printStackTrace();
			       
			    }
				
				if (deckArray != null)
				{
					try {
				        //writing list of decks 
						Log.d("Trying to write list of decks", "Should work?");
				        DataOutputStream out = 
				                new DataOutputStream(openFileOutput(MainActivity_LogIn.GETDECKS_FILE, Context.MODE_PRIVATE));
				        out.writeUTF(deckArray.toString());
				        out.close();
				    }catch (IOException e) {
				        Log.i("Data Input Sample", "I/O Error");
				    }catch (Exception e)
				    {
				    	Log.d("FAILING WRITING DECK LIST!!", "failed to write to the list of decks");
				    	e.printStackTrace();
				    }
					
					Intent intent =new Intent(context,DeckListMaker.class);
					intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION, sessionId);
					intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER, username);
					//intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_DECKLIST, deckArray.toString());
					startActivity(intent);
				}
			 
		 }
		 public void onReturnLogin(String error, JSONObject jresponse,String name) {}
	};
	
	@Override
	//prevents the up/back key from working
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	 return true;
	     }
	     return super.onKeyDown(keyCode, event);    
	}
	
	
	protected OnResponseMIMEListener onResponseMIMEListener = new OnResponseMIMEListener() {
		public void onReturnMIMEIn(String error, JSONObject jresponse, String name, Context context)
		{
			//handles the result of sending in a photo to be parsed
			JSONArray divArray=null;
			String imgName=null;
			try{
		
				if (error==null){
					divArray=jresponse.getJSONArray("divs");
					imgName=jresponse.getString("name");
				}
			}
			catch(Exception e) {
				 Log.d("Error", "Cannot make the div array");
				e.printStackTrace();
		    }
			
			//Start the activity to show the photo with the lines on it
			if (divArray != null){
				
				Intent intent=new Intent(context,DrawLines.class);
				intent.putExtra("imageCoords",divArray.toString());
				intent.putExtra("imageName",imgName);
				intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION, sessionId);
				intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER, username);
				startActivity(intent);
				
				
			}
			
			
					
		}
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("DEBUGGG", "DeckListMaker was started");
		setContentView(R.layout.activity_deck_list_maker);
		
		//load information from previous caller
		Intent intent =getIntent();
		sessionId = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION);
		username=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER);
		
		
		String deckString=null;
		try {
	        //read in the string of decks in deckList
			DataInputStream in = new DataInputStream(openFileInput(MainActivity_LogIn.GETDECKS_FILE));
	       
	    	try {
	    		deckString=in.readUTF();
	    		Log.i("Data Input List of decks!", deckString);
	    	} catch (EOFException e) {
	            Log.i("Data Input Sample from MAIN", "End of file reached");
	        }
	        in.close();
	    } catch (IOException e) {
	        Log.i("Data Input Sample", "I/O Error--file isn't there!");
	        return;
	    }
	    
		jarray=null;
		try {
			//convert String into an actual array (JSONArray)
			jarray=new JSONArray(deckString);
				
			
		} catch (Exception e){
			Log.i("Data Input Sample", "Could not parse list into a JSONArray");
			return;
		}
	
		//method to display the decks
		handleDecks(jarray);
	
		
	}

	//helper method that takes the array of decks and displays them appropriately on the screen
	private void handleDecks(JSONArray deckArray)
	{
		//arrays to store the data
		ArrayList<String> deckIds=new ArrayList<String>();
		ArrayList<String> deckNames=new ArrayList<String>();
		DeckLongClickOn=false;
		
		
		//expandable view to store all the decks
		ScrollView scroll=new ScrollView(this);
		//single layout that goes inside the scrollView
		LinearLayout layoutOfLayouts=new LinearLayout(this);
		layoutOfLayouts.setOrientation(LinearLayout.VERTICAL);
		
		// Show the list of decks in three columns
		int index=0;
		Log.d("DEBUG NUMBERS", "ArrayLength: " + deckArray.length() + " 1/3: " + (deckArray.length()/3));
		for (index=0; index<(deckArray.length()/3); index++)
		{
			int indexBase=3*index;
			// Action necessary to show each row of the list of decks
			LinearLayout innerLayout=new LinearLayout(this);
			innerLayout.setOrientation(LinearLayout.HORIZONTAL);
			TextView b0=makeListButtonHelper(deckIds,deckNames,deckArray,indexBase);
			TextView b1=makeListButtonHelper(deckIds,deckNames,deckArray,indexBase+1);
			TextView b2=makeListButtonHelper(deckIds,deckNames,deckArray,indexBase+2);
			innerLayout.addView(b0);
			innerLayout.addView(b1);
			innerLayout.addView(b2);
			layoutOfLayouts.addView(innerLayout);
			//Used for visual spacing on the screen. no real functional purpose
			LinearLayout filler=new LinearLayout(this);
			Button fillB=new Button(this);
			fillB.setHeight(30);
			fillB.setVisibility(View.INVISIBLE);
			filler.addView(fillB);
			layoutOfLayouts.addView(filler);
			
		}
		LinearLayout lastRow=new LinearLayout(this);
		lastRow.setOrientation(LinearLayout.HORIZONTAL);
		//lastRow.setGravity(Gravity.CENTER_HORIZONTAL);
		index=index*3;
		
		//Handle the decks the leftover decks that didn't fill up the columns perfectly
		Log.d("DEBUG NUMBERS: ", "Mod 3: "+ deckArray.length() + "Index Start: "+index);
		for (;index<(deckArray.length());index++)
		{
			Log.d("Debug", "In Remainder List of ListMaker");
			TextView b=makeListButtonHelper(deckIds,deckNames,deckArray,index);
			lastRow.addView(b);
		
		}
		//add filler at the end of the unfilled row
		if (deckArray.length()%3 != 0){
			layoutOfLayouts.addView(lastRow);
			LinearLayout filler=new LinearLayout(this);
			Button fillB=new Button(this);
			fillB.setHeight(30);
			fillB.setVisibility(View.INVISIBLE);
			filler.addView(fillB);
			layoutOfLayouts.addView(filler);
		}
		
		
	
		scroll.addView(layoutOfLayouts);
		
		LinearLayout layout=(LinearLayout)findViewById(R.id.DeckList_layout);
		layout.addView(scroll);
		layout.invalidate();
		
		
	
	}

	
	
	//called by the options menu to lead into making a deck by means of a photo
	public void makePicture(View view)
	{
		
		 Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		 startActivityForResult(takePictureIntent, 4); 
		
	}
	
	
	
	
	//helper function to set the styling of each icon of the deckList
	private TextView makeListButtonHelper(ArrayList<String> deckIds, ArrayList<String> deckNames,JSONArray deckArray, int index)
	{
		
		try{
			//load the individual information
			JSONObject tempDeck=deckArray.getJSONObject(index);
			String deckid=tempDeck.getString("deck_id");
			String name=tempDeck.getString("name");
					
			
			deckIds.add(index, deckid);
			deckNames.add(index,name);
			Log.d("DEBUG name",name);
			Log.d("DEBUG id",deckid);
			
			TextView tv=new TextView(this);
			tv.setText(name);
			tv.setTag(deckid);
			tv.setId(index);
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(15);
			tv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.deck_logo_new, 0, 0);
			tv.setWidth(100);
			tv.setHeight(70);
			
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			// view the deck when the individual deck is clicked
			tv.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View view) {
	                 // Perform action on click
	            	 
	            	 if (!DeckListMaker.DeckLongClickOn){
		            	 Log.d("DEBUG clicked and got tag:", view.getTag().toString());
		            	 Intent intent =new Intent(view.getContext() , ViewDeck.class);
		            	 intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_DECKID,view.getTag().toString());
		            	 intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION, sessionId);
		         	    intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER,username);
		         		startActivity(intent);
	            	 }
	             }
	         });
			Log.d("DEBUG", "Before adding textview to scrollview");
			
			//allow this deck to be longClicked to bring up a context menu
			registerForContextMenu(tv);
			
			return tv;
			}
		catch(Exception e) {
			 Log.d("Error", "Cannot turn parse JSONArray of decks_list in deckListButtonHelper maker");
			e.printStackTrace();
	       
	    }
		return null;
	}
	
	
	
	
	// on the return from the camera intent
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
			cameraHelper(intent);
	}

	// handle the picture that the camera just took
	private void cameraHelper(Intent intent)
	{
		// take the photo
		Bundle extras = intent.getExtras();
	    if (extras == null){
	    	Log.d("PhotoIntent", "Photo was cancelled");
	    	return;
	    }
		Bitmap bm = (Bitmap) extras.get("data");
	   
		
		//store the photo
	    File path = Environment.getExternalStorageDirectory();
	    File dir=new File(path,MainActivity_LogIn.FILE_DIR);
	    dir.mkdir();
	    
	    File f = new File(dir,MainActivity_LogIn.CAMERA_FILE);
	    mCurrentPhotoPath = f.getAbsolutePath();
	   
	    try {
	    	Log.d("WRITING","Saving PhotoIntent photo");
	    	FileOutputStream out = new FileOutputStream(f);
	         bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
        } catch (Exception e) {
        	Log.d("WRITING","FAILED saving PhotoIntent photo");
            e.printStackTrace();
        }
        
	    //Send in the photo to the server to be parsed
	    Log.d("BEFORE POST", "RIGHT BEFORE MIMEPOST");
	    MIMEThread thread=new MIMEThread((Context)this, onResponseMIMEListener);
		if (thread.wifiOn()){
		    thread.BeforeMakePic(username,sessionId, mCurrentPhotoPath);
			thread.execute(new String[]{null});
		}
		else
			MyJSON.WifiAlert(this);
	}
	

	//Handle the menu that appears when the menu button is pressed
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.deck_list_maker, menu);
		return true;
	}
	
	
	// define operations for the menu button items
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
		case R.id.listMakerNewPhotoDeck:
			//take a picture to be submitted
			 Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			 startActivityForResult(takePictureIntent, 4);
			return true;
		case R.id.listMakerLogout:
			//logout if there is wifi
			WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
	    	
			if (wifi.isWifiEnabled()){
				Intent intent =new Intent(this,MyLogout.class);
				intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION, sessionId);
				intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER, username);
				startActivity(intent);
				return true;
			}
			else
				callWifiAlert();
			break;
		case R.id.listMakerSyncList:
			
			//Sync the deck list to the server in case decks were added on the website
			WifiManager wifi2 = (WifiManager)getSystemService(Context.WIFI_SERVICE);
	    	
			if (wifi2.isWifiEnabled()){
				JSONThread thread=new JSONThread(this, onResponseListener, JSONThread.GETDECKLIST);
				thread.BeforeDecksPage(username,sessionId);
				thread.execute(new String[]{null});
			}
			else
				callWifiAlert();	
		}
		return super.onOptionsItemSelected(item);
	}
	
	//Alerts user that there is no wifi
	public void callWifiAlert()
	{
		MyJSON.WifiAlert(this);
	}
	
	// handle long presses on a deck in the list
	 @Override  
	   public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
		 super.onCreateContextMenu(menu, v, menuInfo);  
		 TextView tv=(TextView)v;
	    menu.setHeaderTitle(tv.getText());  
	    menu.add(0,v.getId(), 0, "Delete Deck");  
	      
	}  
	 
	 
	 
	 @Override  
	 public boolean onContextItemSelected(MenuItem item) {  
	         if(item.getTitle().equals("Delete Deck")){deleteDeck(item.getItemId());}  
	     //else if(item.getTitle().equals("Append to Deck")){appendDeck(item.getItemId());}  
	     else {return false;}  
	 return true;  
	 }  

	 
	 //actually delete the deck by deleting it on the server and later, updating the deckList
	 private void deleteDeck(int id)
	 {
		TextView tv=(TextView)findViewById(id);
		Log.d("DELETEDECK", tv.getText()+"  "+id); 
		JSONThread thread=new JSONThread((Context)this, onResponseListener, JSONThread.DELETEDECK);
		if (thread.wifiOn()){
			thread.BeforeDeleteDeck(username,sessionId,(String)tv.getTag());
			thread.execute(new String[]{null});
		}
		else
			MyJSON.WifiAlert(this);
		
	 }
	 
	 
}
