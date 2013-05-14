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
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import flashyapp.com.JSONThread.OnResponseListener;
import flashyapp.com.SaveResourceThread.OnResponseSaveResourceListener;


public class ViewDeck extends Activity {

	private String deckId;
	private String username;
	private String sessionId;
	private JSONArray deckArray;
	private int index;
	private ArrayList<String> sideA;
	private ArrayList<String> sideB;
	private WebView webView;
	private JSONArray mCards;
 
	
protected OnResponseSaveResourceListener onSaveResourceListener = new OnResponseSaveResourceListener() {
		
		public void onReturnSaveResource(Context context, Bitmap bitmap, String mSide, int counter, String name){
			Log.d("RESOURCE WAS OBTAINED", "Resource gotten after httpget command");		   
		    
			// save resource to phone
		    File path = Environment.getExternalStorageDirectory();
		    File dir=new File(path,MainActivity_LogIn.FILE_DIR);
		    dir.mkdir();
		    String fileName=name+".jpg";
		    File f = new File(dir,fileName);
		    
		    try {
		    	Log.d("WRITING","writing to from ViewDeck : "+f.getAbsolutePath());
		    	FileOutputStream out = new FileOutputStream(f);
		    	if (bitmap != null)
		         bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
		    	else
		    		Log.d("BITMAP IS", "NULLLLLL");
	        } catch (Exception e) {
	        	Log.d("WRITING","FAILED WRITING Resource to storage");
	            e.printStackTrace();
	        }
		    
		    
		    
		    if (mSide.equals("sideA"))
		    	getResourceLooper(context,counter,"sideB");
		    else{
		    	counter=counter+1;
		    	getResourceLooper(context,counter,"sideA");
		    }
		}
	};
	
	
	
	
	
	protected OnResponseListener onResponseListener = new OnResponseListener() {
		 public void onReturnRegister(String error, JSONObject jresponse){}
		 public void onReturnDeleteDeck(String error, Context context){}
		 public void onReturnLogin(String error, JSONObject jresponse, String name){}
		 public void onReturnDeckFromImage(Context context, String mError, JSONObject jresponse){}
		 public void onReturnDecksPage(String error, JSONObject jresponse, Context context){}
		 public void onReturnLogout(String error){}
		 public void onReturnSaveDeck(Context context, String mError, JSONObject jresponse){
			 Log.d("RETURNED FROM GETTING DECK", "In response listener, Please work");
			
			 ViewDeck vd=(ViewDeck)context;
			 
			 // save the deck Data (name/description/etc...) to phone memory
			 // starts looper to get all the resources as well
			 if (mError==null){
				 try {
					 
					 Log.d("DEBUG", "Before writing deck info file");
					 	
					 	 JSONArray cards=jresponse.getJSONArray("cards");
					        mCards=cards;
					        
					        
					 	String deckName=jresponse.getString("deck_id");
					 	String commonName=jresponse.getString("name");
					 	String deckDescrip=jresponse.getString("description");
					 	
					 	//Build deck info card
					 	DataOutputStream out = 
				                new DataOutputStream(openFileOutput(deckName+".txt", Context.MODE_PRIVATE));
				        out.writeUTF(commonName);
				        out.writeUTF(deckDescrip);
				        out.writeUTF(mCards.toString());
				        
				      
				   
				        out.close();
					 	
				        Log.d("DEBUG", "After writing deck info file" + mCards.toString());
					 	
				       vd.getResourceLooper(context, 0, "sideA");
				      
				    }catch (Exception e){
				    	 Log.d("Error", "Cannot take resources from return json object");
			 				e.printStackTrace();
				    }
			 }
			 
		 }
	};
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_deck);
		// Show the Up button in the action bar.
		
		
		RelativeLayout rloutter=(RelativeLayout)findViewById(R.id.outerWebLayout);
		
		//this is just a hack to make the layouts center relative to each other
		RelativeLayout rl=new RelativeLayout(this);
		MyWebView myweb=new MyWebView(this);
				
		rl.addView(myweb);
		
		rloutter.addView(rl);
		
		//set max dimensions
		rl.getLayoutParams().height=265;
		rl.getLayoutParams().width=265;
		
		//center one layout within the other
		RelativeLayout.LayoutParams layoutParams = 
			    (RelativeLayout.LayoutParams)rl.getLayoutParams();
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl.setLayoutParams(layoutParams);
		
		
		
		Log.d("DEBUG", "right after layoutparams before invalidate");
		myweb.loadDataWithBaseURL("fake", "test", "text/html", "utf-8", null);
		myweb.invalidate();
		
		Intent intent =getIntent();
		deckId = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_DECKID);
		sessionId = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION);
		username=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER);
	
		Log.d("deckId in DeckView:", deckId+ "    "+username + sessionId);
		deckArray=null;
		index=0;
		
		//do some more design work
		LinearLayout ll=(LinearLayout)findViewById(R.id.ViewDeck_layout);
		ll.setBackgroundColor(Color.BLACK);
		
		webView=myweb;
		webView.setTag("A");
		
		//find ids so that based on orientation the views can be updated/shown
		RelativeLayout rltop=(RelativeLayout)findViewById(R.id.ViewDeck_top_buttons);
        ImageButton ib1=(ImageButton)findViewById(R.id.flip_next_button_landscape);
        ImageButton ib2=(ImageButton)findViewById(R.id.flip_previous_button_landscape);
        
		
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        @SuppressWarnings("deprecation")
		int orientation = display.getOrientation();
         switch(orientation) {
         
         	//displays this on landscape!! (sideways)
            case Configuration.ORIENTATION_PORTRAIT:
                
            	Log.d("ORIENTATION:", " Screen is portrait!");
            	
                rltop.setVisibility(View.GONE);
                ib1.setVisibility(View.VISIBLE);
                ib2.setVisibility(View.VISIBLE);
                
                
                break;
            //never has displayed this yet
            case Configuration.ORIENTATION_LANDSCAPE:
            	Log.d("ORIENTATION:", " Screen is landscape!");
            	break;
            //displays this on PORTRAIT
            case Configuration.ORIENTATION_UNDEFINED:
            	Log.d("ORIENTATION:", " Screen is undefined!");
            	rltop.setVisibility(View.VISIBLE);
                ib1.setVisibility(View.GONE);
                ib2.setVisibility(View.GONE);
        		break;
        }
		
		loadDeck();
		
	}

	
	
	// this actually loads the information about the deck for viewing use
	private void loadDeck(){
	
		try{
			DataInputStream in = new DataInputStream(openFileInput(deckId+".txt"));
		    try {
		    		String commonName=in.readUTF();
		    		String descrip=in.readUTF();
		    		String deckArrayString=in.readUTF();
		    		Log.i("Deck info!", "name: "+commonName + " \ndescrip: "+ descrip + " \n "+ deckArray);
		              	
		    		deckArray=new JSONArray(deckArrayString);
		         } catch (EOFException e) {
		            Log.i("Data Input Sample from MAIN", "End of file reached");
		        }
		        in.close();
		} catch (IOException e) {
		        Log.i("Data Input Sample", "I/O Error--file isn't there!");
		        
		        //File doesn't exist and needs to be retrieved
		        webView.loadDataWithBaseURL("file://","Loading", "text/html", "utf-8", null);
		        WifiManager wifi2 = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		    	
		        
		        
		        Log.d("DECKID", deckId);
		        // call thread to load the data about the deck
				if (wifi2.isWifiEnabled()){
					JSONThread thread=new JSONThread(this, onResponseListener, JSONThread.SAVEDECK);
					thread.BeforeSaveDeck(username,sessionId,deckId);
					thread.execute(new String[]{null});
				}
				else
					callWifiAlert();
		     
		       
		    return;
	
		}
		catch(Exception e) {
			 Log.d("Error", "Cannot read in deck data");
			e.printStackTrace();
	       
	    }
		
		makeIndividualCards();
		
		presentCardAction();
	}
	
	//takes the deck info and makes it into html for viewing
	//stores results in arrays
	private void makeIndividualCards()
	{
		sideA=new ArrayList<String>();
		sideB=new ArrayList<String>();
		for (int i=0; i<deckArray.length(); i++)
		{
			
			try{
				Log.d("Debug array", deckArray.toString());
				JSONObject tempDeck=deckArray.getJSONObject(i);
				String Aside=tempDeck.getString("sideA");
				
				//string for getting resource number
				String regex="(<img src=\")(\\[FLASHYRESOURCE:)(\\w{8,})(\\])(\" />)";
				//string to find when there is no resource
				String regex2="\\[FLASHYRESOURCE:0{8}\\]";
				String replacement="$1file:///sdcard/flashyapp/$3.jpg\" alt=\"Pic\" HEIGHT=\"250\" WIDTH=\"250\" BORDER=\"0\" >";
		    	String resourceA=Aside.replaceAll(regex, replacement );
		    	// tell the user that there is no back
		    	String resource2A=resourceA.replaceAll(regex2, "No Back to this card");
		    	//do the same for sideB
				String Bside=tempDeck.getString("sideB");
		    	String resourceB=Bside.replaceAll(regex, replacement);
		    	String resource2B=resourceB.replaceAll(regex2, "No Back to this card");
				
				Log.d("DEBUG resource2A",resource2A);
				Log.d("DEBUG resource2B",resource2B);
				
				sideA.add(i,resource2A);
				sideB.add(i,resource2B);
				
			}
			catch(Exception e) {
				 Log.d("Error", "Cannot turn parse JSONArray of decks_list");
				e.printStackTrace();
		       
		    }
		}
		
		
		
	}
	
	//method responsible for flipping over the card
	public void flipOver(View view)
	{
		String tag=(String)webView.getTag();
		
		if (tag.equals("A"))
			webView.setTag("B");
		else
			webView.setTag("A");
		presentCardAction();
	}
	
	//method for going on to the next card
	public void nextCard(View view)
	{
		//always show side A when you move on to the next card
		webView.setTag("A");
		
		if (index < deckArray.length()-1)
			index++;
		else
			index=0;
		presentCardAction();
	}
	//method for going to previous card
	public void prevCard(View view)
	{
		//always show Side A when you move to a previous card
		webView.setTag("A");
		
		if (index >0)
			index--;
		else
			index=deckArray.length()-1;
		presentCardAction();
	}
	
	
	//This is responsible for actually displaying a card
	private void presentCardAction()
	{
		
		String tag=(String)webView.getTag();
		
	
		File f=Environment.getExternalStorageDirectory();
		String resource;
		//find the html to display
		if (webView.getTag().equals("A"))
			resource=sideA.get(index);
		else
			resource=sideB.get(index);
		
		
		if (tag.equals("A")){
			webView.loadDataWithBaseURL("file://"+f.getAbsolutePath(), resource, "text/html", "utf-8", null);
			webView.invalidate();
		}
		else {
			webView.loadDataWithBaseURL("file://"+f.getAbsolutePath(), resource, "text/html", "utf-8", null);
			webView.invalidate();
		}	
		
		
		
		//redraw the view
		LinearLayout layout=(LinearLayout)findViewById(R.id.ViewDeck_layout);
		layout.invalidate();
		
	}
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_deck, menu);
		return true;
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
		
		case R.id.ViewDeckLogout:
			// allows for logout from this page
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
			
			return true;
			
		case R.id.SyncDeckId:
			//Overwrites all the data about the deck so that it can reflect the
			//deck that might have been edited on the website
			WifiManager wifi2 = (WifiManager)getSystemService(Context.WIFI_SERVICE);
	    	
			if (wifi2.isWifiEnabled()){
				JSONThread thread=new JSONThread(this, onResponseListener, JSONThread.SAVEDECK);
				thread.BeforeSaveDeck(username,sessionId,deckId);
				thread.execute(new String[]{null});
				return true;
			}
			else
				callWifiAlert();
			
			return true;
		
		}
		return super.onOptionsItemSelected(item);
	
	}

	public void callWifiAlert()
	{
		MyJSON.WifiAlert(this);
	}

	
	

	//function to be in charge of looping through all resources and downloading them
	//to save on the phone
	public void getResourceLooper(Context context, int i, String paramSide)
	{
		int Length=mCards.length();
		if (i<Length){
			Log.d("DEBUG Looper", "int: " + i+ "   side: "+paramSide);
			
			try{
				JSONObject temp=mCards.getJSONObject(i);
				Log.d("DEBUG Looper 2: ", temp.toString());
		    	
		    	String side=temp.getString(paramSide);
		    	
		    
		    	Log.d("WRITING RESOURCES: ", side);
		    	
		    	String regex="(<img src=\"\\[FLASHYRESOURCE:)(\\w{8,})(\\]\" />)";
		    	String contain="<img";
		    	//just try to replace a resource if it has a resource
		    	if (side.contains(contain)){
		    		Log.d("CONTAINS", "Contains the regexed stuff");
			    	String resource=side.replaceAll(regex, "$2");
			    	Log.d("RESOURCES: ", resource );
			    	getAndSaveResource(context, resource, paramSide,i);
		    	}
		    	//otherwise download the next appropriate resource
		    	else{
		    		
		    		 if (paramSide.equals("sideA"))
		 		    	getResourceLooper(context,i,"sideB");
		 		    else{
		 		    	i=i+1;
		 		    	getResourceLooper(context,i,"sideA");
		 		    }
		    	}
		    		
		    	
		    	
			}catch (Exception e){
		   	 Log.d("Error", "Cannot take resources from return json object");
					e.printStackTrace();
		   }
		}
		else{
			Log.d("Finished saving all resources", "Normally would call updateDeckList but yeah");
			//updateDeckList(onResponseListener);
			loadDeck();
		}
		
	}
	
	//actually call the thread to get the resource
	public void getAndSaveResource(Context context, String resourceName, String side, int counter)
	{
		SaveResourceThread thread2=new SaveResourceThread(context, onSaveResourceListener,side, counter );
		if (thread2.wifiOn()) {
		thread2.BeforeSaveResource(resourceName);
		 thread2.execute(new String[]{null});
		}
		else
			MyJSON.WifiAlert(this);
	}


}
