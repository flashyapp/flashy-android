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
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import flashyapp.com.JSONThread.OnResponseListener;
import flashyapp.com.MIMEThread.OnResponseMIMEListener;

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
			 
			 JSONThread thread=new JSONThread(context, onResponseListener, JSONThread.GETDECKLIST);
			 if (thread.wifiOn()){
				 thread.BeforeDecksPage(username,sessionId);
				 thread.execute(new String[]{null});
			 }
			 else
				 MyJSON.WifiAlert(context);
		 }
		 public void onReturnDeckFromImage(Context context, String mError, JSONObject jresponse)
		 {
			 /*DrawLines dl=(DrawLines)context;
			 
			 Log.d("Debug", "In reponse");
			 if (mError==null){
				 Log.d("Debug", "In null error");
				 try {
					 
					 Log.d("DEBUG", "Before writing deck info file");
					 	JSONObject jdeck=jresponse.getJSONObject("deck");
					 	 JSONArray cards=jdeck.getJSONArray("cards");
					        mCards=cards;
					       
					 	String deckName=jdeck.getString("deck_id");
					 	String commonName=jdeck.getString("name");
					 	String deckDescrip=jdeck.getString("description");
					 	
					 	//Build deck info card
					 	DataOutputStream out = 
				                new DataOutputStream(openFileOutput(deckName+".txt", Context.MODE_PRIVATE));
				        out.writeUTF(commonName);
				        out.writeUTF(deckDescrip);
				        out.writeUTF(mCards.toString());
				        out.close();
					 	
				        Log.d("DEBUG", "After writing deck info file");
					 	dl.getResourceLooper(0, "sideA");
				     
					 //	DataOutputStream out = 
				     //           new DataOutputStream(openFileOutput(MainActivity_LogIn.GETDECKS_FILE, Context.MODE_PRIVATE));
				     //   out.writeUTF(deckArray.toString());
				      //   out.close();
				       
				    	
				    }catch (IOException e) {
				        Log.i("Data Input Sample", "I/O Error");
				    }catch (Exception e){
				    	 Log.d("Error", "Cannot take resources from return json object");
			 				e.printStackTrace();
				    }
				
				 
				 
				 
				 Log.d("THREAD Response", "Need to correct later but finished json submission");
				 
				//dl.updateDeckList();
				
				 */
			 }
	
		 public void onReturnDecksPage(String error, JSONObject jresponse, Context context){
			 JSONArray deckArray=null;
				try{
					
					if (error==null)
						deckArray=jresponse.getJSONArray("decks");
					
				
				
				}
				catch(Exception e) {
					 Log.d("Error", "Cannot turn response to JSON");
					e.printStackTrace();
			       
			    }
				
				//Log.d("DEBUG DeckList", "Before if statement");
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
	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
	     return true;
	     }
	     return super.onKeyDown(keyCode, event);    
	}
	
	protected OnResponseMIMEListener onResponseMIMEListener = new OnResponseMIMEListener() {
		public void onReturnMIMEIn(String error, JSONObject jresponse, String name, Context context)
		{
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
		// Show the Up button in the action bar.
		
		
	
		
		Intent intent =getIntent();
		sessionId = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION);
		username=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER);
		/*//String deckString=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_DECKLIST);*/
		
		String deckString=null;
		try {
	        //reading SessionId
	       
	    
	    	DataInputStream in = new DataInputStream(openFileInput(MainActivity_LogIn.GETDECKS_FILE));
	       //InputStream is=new InputStream(openFileInput(SESSION_FILE));
	        
	    	try {
	    //		username=in.readUTF();
	    		
	    			
	    		
	    			deckString=in.readUTF();
	    			//username=in.readUTF();
	    		
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
    	 
			
          	
				jarray=new JSONArray(deckString);
				
			
		} catch (Exception e){
			Log.i("Data Input Sample", "Could not parse list into a JSONArray");
			return;
		}
	
		handleDecks(jarray);
	
		/*File f=new File(MainActivity_LogIn.GETDECKS_FILE);
		f.delete();*/
	}

	
	private void handleDecks(JSONArray deckArray)
	{
		
		
		
		
		ArrayList<String> deckIds=new ArrayList<String>();
		ArrayList<String> deckNames=new ArrayList<String>();
		DeckLongClickOn=false;
		
		ScrollView scroll=new ScrollView(this);
		
		LinearLayout layoutOfLayouts=new LinearLayout(this);
		layoutOfLayouts.setOrientation(LinearLayout.VERTICAL);
		
		int index=0;
		Log.d("DEBUG NUMBERS", "ArrayLength: " + deckArray.length() + " 1/3: " + (deckArray.length()/3));
		for (index=0; index<(deckArray.length()/3); index++)
		{
			int indexBase=3*index;
			
			LinearLayout innerLayout=new LinearLayout(this);
			innerLayout.setOrientation(LinearLayout.HORIZONTAL);
			TextView b0=makeListButtonHelper(deckIds,deckNames,deckArray,indexBase);
			TextView b1=makeListButtonHelper(deckIds,deckNames,deckArray,indexBase+1);
			TextView b2=makeListButtonHelper(deckIds,deckNames,deckArray,indexBase+2);
			innerLayout.addView(b0);
			innerLayout.addView(b1);
			innerLayout.addView(b2);
			layoutOfLayouts.addView(innerLayout);
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
		Log.d("DEBUG NUMBERS: ", "Mod 3: "+ deckArray.length() + "Index Start: "+index);
		for (;index<(deckArray.length());index++)
		{
			Log.d("Debug", "In Remainder List of ListMaker");
			TextView b=makeListButtonHelper(deckIds,deckNames,deckArray,index);
			lastRow.addView(b);
		
		}
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
		
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        @SuppressWarnings("deprecation")
		int orientation = display.getOrientation();
         switch(orientation) {
         
         	//displays this on landscape!! (sideways)
            case Configuration.ORIENTATION_PORTRAIT:
                Log.d("ORIENTATION:", " Screen is portrait!");
                break;
            //never has displayed this yet
            case Configuration.ORIENTATION_LANDSCAPE:
            	Log.d("ORIENTATION:", " Screen is landscape!");
            	break;
            //displays this on PORTRAIT
            case Configuration.ORIENTATION_UNDEFINED:
            	Log.d("ORIENTATION:", " Screen is undefined!");
        		break;
        }
		
	
	}

	
	
	
	public void makePicture(View view)
	{
		
		 Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		 startActivityForResult(takePictureIntent, 4); 
		
	}
	
	
	
	
	
	private TextView makeListButtonHelper(ArrayList<String> deckIds, ArrayList<String> deckNames,JSONArray deckArray, int index)
	{
		
		try{
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
			
			
			/*tv.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					Log.d("LONG CLICK", "Should pop up menu!");
					DeckListMaker.DeckLongClickOn=true;
					
					
					
					
					return false;
				}
			});*/
			
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
			
			registerForContextMenu(tv);
			
			return tv;
			}
		catch(Exception e) {
			 Log.d("Error", "Cannot turn parse JSONArray of decks_list in deckListButtonHelper maker");
			e.printStackTrace();
	       
	    }
		return null;
	}
	
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
			cameraHelper(intent);
	}

	
	private void cameraHelper(Intent intent)
	{
		Bundle extras = intent.getExtras();
	    if (extras == null){
	    	Log.d("PhotoIntent", "Photo was cancelled");
	    	return;
	    }
		Bitmap bm = (Bitmap) extras.get("data");
	   
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
        
	   // galleryAddPic();
	    
	    Log.d("BEFORE POST", "RIGHT BEFORE MIMEPOST");
	    MIMEThread thread=new MIMEThread((Context)this, onResponseMIMEListener);
		if (thread.wifiOn()){
	    thread.BeforeMakePic(username,sessionId, mCurrentPhotoPath);
		thread.execute(new String[]{null});
		}
		else
			MyJSON.WifiAlert(this);
	}
	

	/*private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
	*/
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.deck_list_maker, menu);
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
		case R.id.listMakerNewPhotoDeck:
			 Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			 startActivityForResult(takePictureIntent, 4);
			return true;
		case R.id.listMakerLogout:
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
	
	public void callWifiAlert()
	{
		MyJSON.WifiAlert(this);
	}
	
	
	 @Override  
	   public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
		 super.onCreateContextMenu(menu, v, menuInfo);  
		 TextView tv=(TextView)v;
	    menu.setHeaderTitle(tv.getText());  
	    menu.add(0,v.getId(), 0, "Delete Deck");  
	    menu.add(0, v.getId(), 0, "Append to Deck");  
	}  
	 
	 @Override  
	 public boolean onContextItemSelected(MenuItem item) {  
	         if(item.getTitle().equals("Delete Deck")){deleteDeck(item.getItemId());}  
	     else if(item.getTitle().equals("Append to Deck")){appendDeck(item.getItemId());}  
	     else {return false;}  
	 return true;  
	 }  

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
	 private void appendDeck(int id)
	 {
		 TextView tv=(TextView)findViewById(id);
			Log.d("APPENDDECK", tv.getText()+"  "+id);
			//makePic(tv)
	 }
}
