package flashyapp.com;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
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
	private String nickResource;
 
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
		/*cardView=(TextView)findViewById(R.id.deckView_textView);
		cardView.setTag("A");*/
	
		LinearLayout ll=(LinearLayout)findViewById(R.id.ViewDeck_layout);
		ll.setBackgroundColor(Color.BLACK);
		//RelativeLayout rl=(RelativeLayout)findViewById(R.id.)
		
		WebView webview=(WebView)findViewById(R.id.cardViewer);
		
		/*RelativeLayout.LayoutParams layoutParams = 
		    (RelativeLayout.LayoutParams)webview.getLayoutParams();
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
		webview.setLayoutParams(layoutParams);*/
		
		
		
		webView=webview;
		webView.setTag("A");
		//ll.addView(webview);
		
		
		
		loadDeck();
		
	}

	
	
	
	private void loadDeck(){
		
		/*JSONObject deckJSON=new JSONObject();
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
		*/
		
		String deckString=null;
		try{
			
			/*JSONObject jresponse=new JSONObject(response);
			deckArray=jresponse.getJSONArray("cards");*/
			
			
			
		        //reading SessionId
		       
		    
		    	DataInputStream in = new DataInputStream(openFileInput(deckId+".txt"));
		       //InputStream is=new InputStream(openFileInput(SESSION_FILE));
		        
		    	try {
		    //		username=in.readUTF();
		    		
		    		
		    			String commonName=in.readUTF();
		    			String descrip=in.readUTF();
		    			String deckArrayString=in.readUTF();
		    			//username=in.readUTF();
		    		
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
		/*cardView.setWidth(200);
		cardView.setHeight(100);
		cardView.setTextColor(Color.RED);
		cardView.setTextSize(50);
		
		*/
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
		    	//String regex2="\\[\\]";
		    	String resourceA=Aside.replaceAll(regex, "$3");
		    	nickResource=resourceA;
				
				String Bside=tempDeck.getString("sideB");
				String resourceB=Bside.replaceAll(regex, "$3");
				
				String cardIndex=tempDeck.getString("index");
				//int pos=Integer.parseInt(cardIndex);
				
				
				//sideA.add(i, Aside);
				//sideB.add(i,Bside);
				Log.d("DEBUG resourceA",resourceA);
				Log.d("DEBUG resourceB",resourceB);
				
				sideA.add(i,resourceA);
				sideB.add(i,resourceB);
				
				
				/*String fileNameA=deckId+i+"A"+".html";
				String fileNameB=deckId+i+"B"+".html";
				
				
				String external=Environment.getExternalStorageDirectory().getAbsolutePath(); 
				File A=new File(external,fileNameA);
				File B=new File(external,fileNameB);
				
				
				String html="<html><head></head><body><i>AUGHHH</i></body></html>";
				
				
				Log.d("DEBUG fileA",A.getAbsolutePath());
				Log.d("DEBUG fileB",B.getAbsolutePath());
				
				DataOutputStream outA = new DataOutputStream(new FileOutputStream(A));
		               // new DataOutputStream(openFileOutput(A, Context.MODE_PRIVATE));
		        outA.writeChars(html);
		        outA.flush();
		        DataOutputStream outB = new DataOutputStream(new FileOutputStream(B));
		                //new DataOutputStream(openFileOutput(B, Context.MODE_PRIVATE));
		        outB.writeChars(html);
		        Log.d("DEBUG", "done writing html mini files");
		        
		        outB.flush();
		        
		        outA.close();
		        outB.close();*/
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
			index=deckArray.length();
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
		
		
		String imgTag="<img src=\"file:///sdcard/"+resource+".jpg\" alt=\"Ninja Pic\" >";
		
		Log.d("RESOURCEEEEEEEEE", imgTag + " baseeeeee "+ f.getAbsolutePath());
		String html="<html><head></head><body>"+imgTag+"</body></html>";
		
		if (tag.equals("A")){
			//webView.loadUrl("file://"+f.getAbsolutePath() +"/"+ deckId+index+"A"+".html");
			webView.loadDataWithBaseURL("file://"+f.getAbsolutePath(), html, "text/html", "utf-8", null);
			webView.invalidate();
		}
		else {
			webView.loadDataWithBaseURL("file://"+f.getAbsolutePath(), html, "text/html", "utf-8", null);
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

	
	
	

}
