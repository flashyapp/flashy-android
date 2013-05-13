package flashyapp.com;

import java.io.DataOutputStream;
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
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import flashyapp.com.JSONThread.OnResponseListener;
import flashyapp.com.SaveResourceThread.OnResponseSaveResourceListener;

public class DrawLines extends Activity {
	private String sessionId;
	private String username;
	private String imgName;
	public static int deleteId;
	public static int addLineId;
	private JSONArray mCards;
	private boolean mMenuAddRowIsChecked;
	private boolean mMenuDeleteRowIsChecked;
	private boolean mMenuAddColIsChecked;
	private boolean mMenuDeleteColIsChecked;
	private EditText etDeckName;
	private EditText etDeckDescrip;
	private Menu mmenu;
	
	
	protected OnResponseSaveResourceListener onSaveResourceListener = new OnResponseSaveResourceListener() {
		
		public void onReturnSaveResource(Context context, Bitmap bitmap, String mSide, int counter, String name){
			Log.d("RESOURCE WAS OBTAINED", "Resource gotten after httpget command");
			/*DrawLines dl=(DrawLines)context;*/
			
			
			/*File path = Environment.getExternalStorageDirectory();
		    File dir=new File(path,MainActivity_LogIn.FILE_DIR);*/
		    //dir.mkdir();
		    
		   
			
			
			
			
		    //String StrPath=path.getPath();
		    
		    File path = Environment.getExternalStorageDirectory();
		    File dir=new File(path,MainActivity_LogIn.FILE_DIR);
		    dir.mkdir();
		    String fileName=name+".jpg";
		    File f = new File(dir,fileName);
		    
		    
		   /* String fileName=name+".jpg";
		    File f = new File(dir,fileName);*/
		    //was takePictureIntent
		   
		    //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		    try {
		    	Log.d("WRITING","writing to from DRAWLINES : "+f.getCanonicalPath());
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
		public void onReturnDeleteDeck(String error, Context context){}
		public void onReturnSaveDeck(Context context, String mError, JSONObject jresponse){}
		 public void onReturnRegister(String error, JSONObject jresponse){}
		 public void onReturnLogout(String error){}
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
		 
		 public void onReturnLogin(String error, JSONObject jresponse,String name) { }
		 public void onReturnDeckFromImage(Context context, String mError, JSONObject jresponse){
			 DrawLines dl=(DrawLines)context;
			 
			 
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
					 	
					 	
					 	
				       
				       /* for (int i=0; i< cards.length(); i++)
				        {*/
				        	//Log.d("Debug", "In card loop");
				        	
				        	
				        dl.getResourceLooper(context, 0, "sideA");
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
		//setContentView(R.layout.activity_draw_lines);
		
		
		Log.d("DEBUG", "DrawLines onCreate 1");
		Intent intent =getIntent();
		sessionId = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION);
		username=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER);
		String coords=intent.getStringExtra("imageCoords");
		imgName=intent.getStringExtra("imageName");
		Log.d("DEBUG", "DrawLines onCreate 1.5");
		ArrayList<Row> rows=new ArrayList<Row>();
		/*setMenuAddRow(false);
		setMenuDeleteRow(false);*/
		mMenuAddRowIsChecked=false;
		 mMenuDeleteRowIsChecked=false;
		mMenuAddColIsChecked=false;
		 mMenuDeleteColIsChecked=false;
		
		Log.d("DEBUG", "DrawLines onCreate 1.75");
		 LinearLayout ll=new LinearLayout(this);
		 ll.setBackgroundColor(Color.BLACK);
		 ll.setOrientation(LinearLayout.VERTICAL);
		 ll.setGravity(Gravity.CENTER_HORIZONTAL);
		
		 Log.d("DEBUG", "DrawLines onCreate 2");
		 LineSubmissionButton b=new LineSubmissionButton(this,rows);
		 b.setText("Submit Deck");
		 b.setHeight(50);
		 b.setWidth(110);
		 
		 b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DrawLines dl=(DrawLines)v.getContext();
				
				LineSubmissionButton lsb=(LineSubmissionButton)v;
				if (dl.etDeckName.getText() != null)
				{
					Log.d("ETDECKNAME:", ""+dl.etDeckName.getText());
					dl.makeLineIntent(lsb.returnArray());
				}
				else
					dl.etDeckName.setError("Deck Name Required");
			}
		});
		 
		 
		 
		 /*ToggleButton deleteLine=new ToggleButton(this);
		 deleteLine.setChecked(false);
		 deleteId = 55;
		 deleteLine.setId(deleteId);
		 deleteLine.setTag("DELETELINE TAG");
		 deleteLine.setText("Delete a Line");
		 deleteLine.setTextOn("Delete Line On");
		 deleteLine.setTextOff("Delete Line Off");
		*/
		 
		/* ToggleButton addLine=new ToggleButton(this);
		 addLine.setChecked(false);
		 addLineId=56;
		 addLine.setId(addLineId);
		 addLine.setText("Add Column");
		 addLine.setTextOn("Add Column On");
		 addLine.setTextOff("Add Column Off");*/
		 
		 
		 Log.d("DEBUG", "DrawLines onCreate 3");
		 LinearLayout topRow=new LinearLayout(this);
		 topRow.setOrientation(LinearLayout.HORIZONTAL);
		 
		  etDeckName=new EditText(this);
		 etDeckName.setHint("Name of the Deck");
		 etDeckName.setHeight(40);
		 etDeckName.setWidth(200);
		  etDeckDescrip=new EditText(this);
		 etDeckDescrip.setHint("Description of Deck");
		 etDeckDescrip.setHeight(40);
		 etDeckDescrip.setWidth(200);
		 
		 
		 LinearLayout llforET=new LinearLayout(this);
		 llforET.setOrientation(LinearLayout.VERTICAL);
		 llforET.addView(etDeckName);
		 llforET.addView(etDeckDescrip);
		 topRow.addView(llforET);
		 topRow.addView(b);
		/* topRow.addView(deleteLine);
		 topRow.addView(addLine);*/
		 
		 Log.d("DEBUG", "DrawLines onCreate 4");
		 DrawView drawView = new DrawView(this,coords,rows);
		
		// drawView.getLayoutParams().height=260;
		 
		 
		 ll.addView(topRow);
		 ll.addView(drawView);
		// ll.addView(addLine);
		 
		 
		/* LinearLayout newLL=(LinearLayout)findViewById(R.id.drawLines_layout_top);
		 newLL.addView(drawView);*/
		 Log.d("DEBUG", "DrawLines onCreate 5");
	     setContentView(ll);
		
		//setContentView(R.layout.activity_draws);
	}

	
	public void makeLineIntent(ArrayList<Row> rows)
	{
		
		Log.d("ENTERED makeLineIntent  ETDECKNAME:", "..........................."+etDeckName.getText());
		
		
		String str=etDeckName.getText().toString();
		if (str.equalsIgnoreCase("")){
			
			etDeckName.setError("Deck Name Required");
			return;
		}
		else
			Log.d("ETDECKNAME:", "..........................."+etDeckName.getText());
		
		
		JSONObject complete=new JSONObject();
		JSONArray divs=new JSONArray();
		JSONArray row=null;
		try{

			for(Row r: rows)
	    	{
	    		row=r.makeJSONcoords();
	    		divs.put(row);
	    	}
			
			complete.put("name", imgName);
			complete.put("divs",divs);
			complete.put("username", username);
			complete.put("session_id",sessionId);
			complete.put("deck_name", etDeckName.getText());
			complete.put("description", etDeckDescrip.getText());
			
		}
		catch(Exception e) {
			 Log.d("Error", "Can't put JSON rows together");
			e.printStackTrace();
	       
	    }
		
		
		Log.d("COMPLETE JSON OBJECT --------------------------", complete.toString());
		
		/*
		 * 
		 * FIX THIS!!!! Not stable/good
		 * need to make its own method to get back resources etc
		 */
		JSONThread thread=new JSONThread((Context)this, onResponseListener, JSONThread.DECKFROMIMAGE);
		 if (thread.wifiOn()){
			 thread.BeforeLineSubmission(complete);
			 thread.execute(new String[]{null});
		 }
		 else
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
		    	
		    /*	//String regex2="\\[\\]";
		    	String resource=side.replaceAll(regex, "$2");
		    	//String resourceB=sideB.replaceAll(regex, "$2");
		    	Log.d("RESOURCES: ", resource );
		    	getAndSaveResource(context, resource, paramSide,i);*/
		    	
		    	Log.d("REGEX", regex + "       " + side);
		    	
		    	String contains="<img";
		    	if (side.contains(contains)){
		    		
		    		Log.d("CONTAINED REGEX!!!", "REGEX WORKED!");
		    	
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
			Log.d("Finished saving all resources", "initializing updateDeckList");
			updateDeckList(onResponseListener);
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
	
	
	
	public void updateDeckList(OnResponseListener orl)
	{
		JSONThread thread2=new JSONThread(this, orl, JSONThread.GETDECKLIST);
		 if (thread2.wifiOn()){ 
			 thread2.BeforeDecksPage(username,sessionId);
			 thread2.execute(new String[]{null});
		 }
		 else
			 MyJSON.WifiAlert(this);
		 
	}
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.draw_lines, menu);
		mmenu=menu;
		return true;
	}
	
	public void setMenuAddRow(Boolean bool)
	{
		mMenuAddRowIsChecked=bool;
		
		
		MenuItem item=mmenu.findItem(R.id.menuAddRow);
		item.setChecked(bool);
		if (bool)
			item.setIcon(R.drawable.checkmark);
		else
			item.setIcon(null);
	}
	
	public boolean getMenuAddRow()
	{
		return mMenuAddRowIsChecked;
	}
	public void setMenuDeleteRow(Boolean bool)
	{
		mMenuDeleteRowIsChecked=bool;
		
		
		MenuItem item=mmenu.findItem(R.id.menuDeleteRow);
		item.setChecked(bool);
		if (bool)
			item.setIcon(R.drawable.checkmark);
		else
			item.setIcon(null);
	}
	
	public boolean getMenuDeleteRow()
	{
		return mMenuDeleteRowIsChecked;
	}
	
	
	
	
	
	
	
	public void setMenuAddCol(Boolean bool)
	{
		Log.d("Entered add col ", "entered and confused");
		mMenuAddColIsChecked=bool;
		
		MenuItem item=mmenu.findItem(R.id.menuAddCol);
		
		item.setChecked(bool);
		if (bool)
			item.setIcon(R.drawable.checkmark);
		else
			item.setIcon(null);
	}
	
	public boolean getMenuAddCol()
	{
		return mMenuAddColIsChecked;
	}
	public void setMenuDeleteCol(Boolean bool)
	{
		mMenuDeleteColIsChecked=bool;
		
		MenuItem item=mmenu.findItem(R.id.menuDeleteCol);
		item.setChecked(bool);
		if (bool)
			item.setIcon(R.drawable.checkmark);
		else
			item.setIcon(null);
	}
	
	public boolean getMenuDeleteCol()
	{
		return mMenuDeleteColIsChecked;
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
		case R.id.menuAddRow:
			Log.d("Menu Status:", ""+item.isChecked());
			if (item.isChecked())
			{
				//item.setIcon(R.drawable.redx);
				/*item.setTitle("Add Row");
				item.setIcon(R.drawable.redx);
				item.setChecked(false);*/
				setMenuAddRow(false);
			}
			else
			{
				//item.setIcon(R.drawable.checkmark);
				/*item.setTitle("Add Row");
				item.setIcon(R.drawable.checkmark);
				item.setChecked(true);*/
				setMenuAddRow(true);
			}
			return true;
		case R.id.menuAddCol:
			Log.d("Menu Status:", ""+item.isChecked());
			if (item.isChecked())
			{
				//item.setIcon(R.drawable.redx);
				/*item.setTitle("Add Column");
				item.setIcon(R.drawable.redx);
				item.setChecked(false);*/
				setMenuAddCol(false);
			}
			else
			{
				//item.setIcon(R.drawable.checkmark);
				/*item.setTitle("Add Column");
				item.setIcon(R.drawable.checkmark);
				item.setChecked(true);*/
				setMenuAddCol(true);
			}
			return true;
		case R.id.menuDeleteRow:
			Log.d("Menu Status:", ""+item.isChecked());
			if (item.isChecked())
			{
				//item.setIcon(R.drawable.redx);
				/*item.setTitle("Delete Row");
				item.setIcon(R.drawable.redx);
				item.setChecked(false);*/
				setMenuDeleteRow(false);
			}
			else
			{
				//item.setIcon(R.drawable.checkmark);
				item.setTitle("Delete Row");
				item.setIcon(R.drawable.checkmark);
				item.setChecked(true);
				setMenuDeleteRow(true);
			}
			return true;
		case R.id.menuDeleteCol:
			Log.d("Menu Status:", ""+item.isChecked());
			if (item.isChecked())
			{
				//item.setIcon(R.drawable.redx);
				item.setTitle("Delete Column");
				item.setIcon(R.drawable.redx);
				item.setChecked(false);
				setMenuDeleteCol(false);
			}
			else
			{
				//item.setIcon(R.drawable.checkmark);
				item.setTitle("Delete Column");
				item.setIcon(R.drawable.checkmark);
				item.setChecked(true);
				setMenuDeleteCol(true);
			}
			return true;
			
			/*case R.id.MenuIdTest:
			TextView tv=(TextView)findViewById(R.id.testPageTextView);
			tv.setText("set from menu? button");
			return true;
		case R.id.action_settings:
			TextView tv2=(TextView)findViewById(R.id.testPageTextView);
			tv2.setText("set from setting button");
			return true;*/
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
	

}
