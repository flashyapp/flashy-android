package flashyapp.com;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import flashyapp.com.JSONThread.OnResponseListener;

public class DrawLines extends Activity {
	private String sessionId;
	private String username;
	private String imgName;
	public static final int deleteId=12345;
	public static final int addLineId=23456;
	
	
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
					intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_DECKLIST, deckArray.toString());
					startActivity(intent);
				}
			 
			 
		 }
		 
		 public void onReturnLogin(String error, JSONObject jresponse,String name) { }
		 public void onReturnDeckFromImage(Context context){
			 Log.d("THREAD Response", "Need to correct later but finished json submission");
			 DrawLines dl=(DrawLines)context;
			 dl.updateDeckList();
			 
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
		
		 DrawView drawView = new DrawView(this,coords,rows);
		
		
		 LinearLayout ll=new LinearLayout(this);
		 ll.setOrientation(LinearLayout.VERTICAL);
		 ll.setGravity(Gravity.CENTER_HORIZONTAL);
		
		 
		 LineSubmissionButton b=new LineSubmissionButton(this,rows);
		 b.setText("FOR SUBMISSION");
		 b.setHeight(50);
		 b.setWidth(100);
		 
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
		 deleteLine.setId(deleteId);
		 deleteLine.setText("Delete a Line");
		 deleteLine.setTextOn("Delete Line On");
		 deleteLine.setTextOff("Delete Line Off");
		 
		 ToggleButton addLine=new ToggleButton(this);
		 addLine.setChecked(false);
		 addLine.setId(addLineId);
		 addLine.setText("Add Column");
		 addLine.setTextOn("Add Column On");
		 addLine.setTextOff("Add Column Off");
		 
		 LinearLayout topRow=new LinearLayout(this);
		 topRow.setOrientation(LinearLayout.HORIZONTAL);
		 topRow.addView(b);
		 topRow.addView(deleteLine);
		 topRow.addView(addLine);
		 
		 
		 ll.addView(topRow);
		 ll.addView(drawView);
		 
		 
		 
	     setContentView(ll);
		
		//setContentView(R.layout.activity_draws);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.drawlines, menu);
		return true;
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
	
	
	public void updateDeckList()
	{
		JSONThread thread2=new JSONThread(this, onResponseListener, JSONThread.GETDECKLIST);
		 thread2.BeforeDecksPage(username,sessionId);
		 thread2.execute(new String[]{null});
	}
}
