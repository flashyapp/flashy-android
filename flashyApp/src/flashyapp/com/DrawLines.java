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
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
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
	
	
	protected OnResponseSaveResourceListener onSaveResourceListener = new OnResponseSaveResourceListener() {
		public void onReturnSaveResource(Context context, Bitmap bitmap, String mSide, int counter, String name){
			Log.d("RESOURCE WAS OBTAINED", "Resource gotten after httpget command");
			/*DrawLines dl=(DrawLines)context;*/
			File path = Environment.getExternalStorageDirectory();
		    //String StrPath=path.getPath();
		    String fileName=name+".jpg";
		    File f = new File(path,fileName);
		    //was takePictureIntent
		   
		    //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		    try {
		    	Log.d("WRITING","writing to : "+fileName);
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
		    	getResourceLooper(counter,"sideB");
		    else{
		    	counter=counter+1;
		    	getResourceLooper(counter,"sideA");
		    }
		}
	};
	
	
	protected OnResponseListener onResponseListener = new OnResponseListener() {
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
				        	
				        	
				        dl.getResourceLooper(0, "sideA");
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
		
		
		Intent intent =getIntent();
		sessionId = intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION);
		username=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER);
		String coords=intent.getStringExtra("imageCoords");
		imgName=intent.getStringExtra("imageName");
		
		ArrayList<Row> rows=new ArrayList<Row>();
		setMenuAddRow(false);
		setMenuDeleteRow(false);
				
		 LinearLayout ll=new LinearLayout(this);
		 ll.setOrientation(LinearLayout.VERTICAL);
		 ll.setGravity(Gravity.CENTER_HORIZONTAL);
		
		 
		 LineSubmissionButton b=new LineSubmissionButton(this,rows);
		 b.setText("FOR SUBMISSION");
		 b.setHeight(50);
		 b.setWidth(110);
		 
		 b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DrawLines dl=(DrawLines)v.getContext();
				LineSubmissionButton lsb=(LineSubmissionButton)v;
				dl.makeLineIntent(lsb.returnArray());
			}
		});
		 
		 ToggleButton deleteLine=new ToggleButton(this);
		 deleteLine.setChecked(false);
		 deleteId = 55;
		 deleteLine.setId(deleteId);
		 deleteLine.setTag("DELETELINE TAG");
		 deleteLine.setText("Delete a Line");
		 deleteLine.setTextOn("Delete Line On");
		 deleteLine.setTextOff("Delete Line Off");
		
		 
		 ToggleButton addLine=new ToggleButton(this);
		 addLine.setChecked(false);
		 addLineId=56;
		 addLine.setId(addLineId);
		 addLine.setText("Add Column");
		 addLine.setTextOn("Add Column On");
		 addLine.setTextOff("Add Column Off");
		 
		 LinearLayout topRow=new LinearLayout(this);
		 topRow.setOrientation(LinearLayout.HORIZONTAL);
		 topRow.addView(b);
		 topRow.addView(deleteLine);
		 topRow.addView(addLine);
		 
		 
		 DrawView drawView = new DrawView(this,coords,rows, deleteLine,addLine);
		 
		 
		 
		 ll.addView(topRow);
		 ll.addView(drawView);
		 
		 
		 
	     setContentView(ll);
		
		//setContentView(R.layout.activity_draws);
	}

	
	public void makeLineIntent(ArrayList<Row> rows)
	{
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
			complete.put("deck_name", "TESTDECK");
			complete.put("description", "descrip");
			
		}
		catch(Exception e) {
			 Log.d("Error", "Can't put JSON rows together");
			e.printStackTrace();
	       
	    }
		
		
		Log.d("COMPLETE JSON OBJECT", complete.toString());
		
		/*
		 * 
		 * FIX THIS!!!! Not stable/good
		 * need to make its own method to get back resources etc
		 */
		JSONThread thread=new JSONThread((Context)this, onResponseListener, JSONThread.DECKFROMIMAGE);
		thread.BeforeLineSubmission(complete);
		thread.execute(new String[]{null});
		
		
		
		
		
	}
	
	public void getResourceLooper(int i, String paramSide)
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
		    	String resource=side.replaceAll(regex, "$2");
		    	//String resourceB=sideB.replaceAll(regex, "$2");
		    	Log.d("RESOURCES: ", resource );
		    	getAndSaveResource(resource, paramSide,i);
		    
		    	
			}catch (Exception e){
		   	 Log.d("Error", "Cannot take resources from return json object");
					e.printStackTrace();
		   }
		}
		else{
			Log.d("Exceeded array length", "initializing updateDeckList");
			updateDeckList();
		}
		
	}
	
	
	public void getAndSaveResource(String resourceName, String side, int counter)
	{
		SaveResourceThread thread2=new SaveResourceThread(this, onSaveResourceListener,side, counter );
		 thread2.BeforeSaveResource(resourceName);
		 thread2.execute(new String[]{null});
	}
	
	
	
	public void updateDeckList()
	{
		JSONThread thread2=new JSONThread(this, onResponseListener, JSONThread.GETDECKLIST);
		 thread2.BeforeDecksPage(username,sessionId);
		 thread2.execute(new String[]{null});
	}
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.draw_lines, menu);
		return true;
	}
	
	private void setMenuAddRow(Boolean bool)
	{
		mMenuAddRowIsChecked=bool;
	}
	
	public boolean getMenuAddRow()
	{
		return mMenuAddRowIsChecked;
	}
	private void setMenuDeleteRow(Boolean bool)
	{
		mMenuDeleteRowIsChecked=bool;
	}
	
	public boolean getMenuDeleteRow()
	{
		return mMenuDeleteRowIsChecked;
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
				item.setTitle("New Row-false");
				item.setChecked(false);
				setMenuAddRow(false);
			}
			else
			{
				//item.setIcon(R.drawable.checkmark);
				item.setTitle("New Row-true");
				item.setChecked(true);
				setMenuAddRow(true);
			}
			return true;
		case R.id.menuDeleteRow:
			Log.d("Menu Status:", ""+item.isChecked());
			if (item.isChecked())
			{
				//item.setIcon(R.drawable.redx);
				item.setTitle("Delete Row-false");
				item.setChecked(false);
				setMenuDeleteRow(false);
			}
			else
			{
				//item.setIcon(R.drawable.checkmark);
				item.setTitle("Delete Row-true");
				item.setChecked(true);
				setMenuDeleteRow(true);
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
