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
	//private String nickResource;
 
	
	
protected OnResponseSaveResourceListener onSaveResourceListener = new OnResponseSaveResourceListener() {
		
		public void onReturnSaveResource(Context context, Bitmap bitmap, String mSide, int counter, String name){
			Log.d("RESOURCE WAS OBTAINED", "Resource gotten after httpget command");
			/*DrawLines dl=(DrawLines)context;*/
			/*File path = Environment.getExternalStorageDirectory();
		    //String StrPath=path.getPath();
		    
		    File f = new File(path,fileName);
		    //was takePictureIntent
*/		    
		    
		    
		    File path = Environment.getExternalStorageDirectory();
		    File dir=new File(path,MainActivity_LogIn.FILE_DIR);
		   // dir.mkdir();
		    String fileName=name+".jpg";
		    File f = new File(dir,fileName);
		    
		    
		   
		 
		    //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		    try {
		    	Log.d("WRITING","writing to : "+f.getAbsolutePath());
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
			 //copy drawLines onrl.returndeckfromimage
			 
			 
			 ViewDeck vd=(ViewDeck)context;

			 
			 
			 Log.d("Debug", "In reponse");
			 if (mError==null){
				 Log.d("Debug", "In null error");
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
					 	
					 	
					 	
				       
				       /* for (int i=0; i< cards.length(); i++)
				        {*/
				        	//Log.d("Debug", "In card loop");
				        	
				        
				       vd.getResourceLooper(context, 0, "sideA");
				       // }
				       
					 
					 
					 
					 /*	DataOutputStream out = 
				                new DataOutputStream(openFileOutput(MainActivity_LogIn.GETDECKS_FILE, Context.MODE_PRIVATE));
				        out.writeUTF(deckArray.toString());
				        
				     
				  
				        out.close();
				        */
				    	
				   /* }catch (IOException e) {
				        Log.i("Data Input Sample", "I/O Error");*/
				    }catch (Exception e){
				    	 Log.d("Error", "Cannot take resources from return json object");
			 				e.printStackTrace();
				    }
				
				 
				 
				 
				 Log.d("THREAD Response", "Need to correct later but finished json submission");
				 
				//dl.updateDeckList();
				
				 
			 }
			 
			 
			
			 
			 
			 
		 }
	};
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_deck);
		// Show the Up button in the action bar.
		
		
		RelativeLayout rloutter=(RelativeLayout)findViewById(R.id.outerWebLayout);
		
		RelativeLayout rl=new RelativeLayout(this);
		MyWebView myweb=new MyWebView(this);
		
	
		
		
		rl.addView(myweb);
		
		rloutter.addView(rl);
		
		rl.getLayoutParams().height=265;
		rl.getLayoutParams().width=265;
		
		
		
		
		
		Log.d("DEBUG", "right before layoutparams");
		
		
		 /*<WebView
         android:id="@+id/cardViewer"
         android:layout_width="265dp"
         android:visibility="gone"
         android:layout_height="wrap_content"
         android:layout_centerInParent="true"
         />*/
	
		
		
		RelativeLayout.LayoutParams layoutParams = 
			    (RelativeLayout.LayoutParams)rl.getLayoutParams();
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl.setLayoutParams(layoutParams);
		
		
		
		Log.d("DEBUG", "right after layoutparams before invalidate");
		myweb.loadDataWithBaseURL("fake", "HIIIII", "text/html", "utf-8", null);
		myweb.invalidate();
		
		Intent intent =getIntent();
		deckId = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_DECKID);
		sessionId = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION);
		username=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER);
	
			Log.d("deckId in DeckView:", deckId+ "    "+username + sessionId);
		deckArray=null;
		index=0;
		/*cardView=(TextView)findViewById(R.id.deckView_textView);
		cardView.setTag("A");*/
	
		LinearLayout ll=(LinearLayout)findViewById(R.id.ViewDeck_layout);
		ll.setBackgroundColor(Color.BLACK);
		//RelativeLayout rl=(RelativeLayout)findViewById(R.id.)
		
		/*WebView webview=new WebView(this);//(WebView)findViewById(R.id.cardViewer);
		webview.setPadding(0, 0, 0, 0);*/
		
		//webview.getSettings().setUseWideViewPort(true);
		/**/
		
		
		
		webView=myweb;
		webView.setTag("A");
		//ll.addView(webview);
		
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

	
	
	
	private void loadDeck(){
		
		
		
		String deckString=null;
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
		        
		        
		        webView.loadDataWithBaseURL("file://","SORRY!!", "text/html", "utf-8", null);
		        
		        WifiManager wifi2 = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		    	
		        
		        
		        Log.d("DECKID", deckId);
		        
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
				
				String regex="(<img src=\")(\\[FLASHYRESOURCE:)(\\w{8,})(\\])(\" />)";
				String regex2="\\[FLASHYRESOURCE:0{8}\\]";
				String replacement="$1file:///sdcard/flashyapp/$3.jpg\" alt=\"Pic\" HEIGHT=\"250\" WIDTH=\"250\" BORDER=\"0\" >";
		    	String resourceA=Aside.replaceAll(regex, replacement );
		    	String resource2A=resourceA.replaceAll(regex2, "No Back to this card");
				String Bside=tempDeck.getString("sideB");
				
		    	String resourceB=Bside.replaceAll(regex, replacement);
		    	String resource2B=resourceB.replaceAll(regex2, "No Back to this card");

			
				String cardIndex=tempDeck.getString("index");
				
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
	public void flipOver(View view)
	{
		String tag=(String)webView.getTag();
		
		if (tag.equals("A"))
			webView.setTag("B");
		else
			webView.setTag("A");
		presentCardAction();
	}
	
	public void nextCard(View view)
	{
		webView.setTag("A");
		
		if (index < deckArray.length()-1)
			index++;
		else
			index=0;
		presentCardAction();
	}
	
	public void prevCard(View view)
	{
		webView.setTag("A");
		
		if (index >0)
			index--;
		else
			index=deckArray.length()-1;
		presentCardAction();
	}
	
	private void presentCardAction()
	{
		
		String tag=(String)webView.getTag();
		String text;
	
		
	
		File f=Environment.getExternalStorageDirectory();
		String resource;
		if (webView.getTag().equals("A"))
			resource=sideA.get(index);
		else
			resource=sideB.get(index);
		
		
		//String imgTag="<img src=\"file:///sdcard/"+resource+".jpg\" alt=\"Pic\" HEIGHT=\"250\" WIDTH=\"250\" BORDER=\"0\" >";
		
		Log.d("RESOURCEEEEEEEEE", resource + " baseeeeee "+ f.getAbsolutePath());
		//String html="<html><head></head><body>"+resource+"</body></html>";
		
		if (tag.equals("A")){
			//webView.loadUrl("file://"+f.getAbsolutePath() +"/"+ deckId+index+"A"+".html");
			webView.loadDataWithBaseURL("file://"+f.getAbsolutePath(), resource, "text/html", "utf-8", null);
			webView.invalidate();
		}
		else {
			webView.loadDataWithBaseURL("file://"+f.getAbsolutePath(), resource, "text/html", "utf-8", null);
			webView.invalidate();
		}	
		
		
		
		//webView.setText(text);
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

	
	
	
	
	
	
	
	
	public void getResourceLooper(Context context, int i, String paramSide)
	{
		int Length=mCards.length();
		if (i<Length){
			Log.d("DEBUG Looper", "int: " + i+ "   side: "+paramSide);
			
			try{
				JSONObject temp=mCards.getJSONObject(i);
				Log.d("DEBUG Looper 2: ", temp.toString());
		    	int index=temp.getInt("index");
		    	String side=temp.getString(paramSide);
		    	/*String sideB=temp.getString("sideB");*/
		    
		    	Log.d("WRITING RESOURCES: ", side);
		    	
		    	String regex="(<img src=\"\\[FLASHYRESOURCE:)(\\w{8,})(\\]\" />)";
		    	//String regex2="\\[\\]";
		    	String contain="<img";
		    	if (side.contains(contain)){
			    	String resource=side.replaceAll(regex, "$2");
			    	//String resourceB=sideB.replaceAll(regex, "$2");
			    	Log.d("RESOURCES: ", resource );
			    	getAndSaveResource(context, resource, paramSide,i);
		    	}else{
		    		
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
