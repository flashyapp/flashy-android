package flashyapp.com;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ViewDeck extends Activity {

	private String deckId;
	private String username;
	private String sessionId;
	private JSONArray deckArray;
	private int index;
	private ArrayList<String> sideA;
	private ArrayList<String> sideB;
	private WebView webView;
	//private String nickResource;
 
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
		   /* } catch (IOException e) {
		        Log.i("Data Input Sample", "I/O Error--file isn't there!");
		        return;
		    }
		    */
	
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
				String replacement="$1file:///sdcard/$3.jpg\" alt=\"Pic\" HEIGHT=\"250\" WIDTH=\"250\" BORDER=\"0\" >";
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
		if (index < deckArray.length()-1)
			index++;
		else
			index=0;
		presentCardAction();
	}
	
	public void prevCard(View view)
	{
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
		
		}
		return super.onOptionsItemSelected(item);
	
	}

	public void callWifiAlert()
	{
		MyJSON.WifiAlert(this);
	}

}
