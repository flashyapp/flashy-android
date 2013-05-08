package flashyapp.com;

import java.io.DataInputStream;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import flashyapp.com.MIMEThread.OnResponseMIMEListener;

public class DeckListMaker extends Activity {
	private String username;
	private String sessionId;
	private JSONArray jarray;
	private String mCurrentPhotoPath;
	

	
	
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
				
				/*for (int index =0; index < divArray.length(); index++  )
				{
					try{
						JSONArray rowArray=divArray.getJSONArray(index);
						JSONArray colNums=rowArray.getJSONArray(1);
						int rownum=rowArray.getInt(0);
						Log.d("DEBUG Lines", "Has a row at height: "+rownum);
						for (int j=0; j<colNums.length(); j++)
							Log.d("DEBUG Lines", "Row " + rownum+ " Has a col at : "+colNums.getInt(j));
						
					}
					catch(Exception e) {
						 Log.d("Error", "Cannot make the mini arrays");
						e.printStackTrace();
				       
				    }
				}
				*/
				
				
				
			}
			
			
					
		}
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("DEBUGGG", "DeckListMaker was started");
		
		
		
		setContentView(R.layout.activity_deck_list_maker);
		// Show the Up button in the action bar.
		LinearLayout ll=(LinearLayout)findViewById(R.id.DeckList_layout);
		//ll.setBackgroundColor(Color.WHITE);
		
		LinearLayout ll2=(LinearLayout)findViewById(R.id.DeckList_layout_top);
		//ll2.setBackgroundColor(Color.BLACK);
		
	
		
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
		
		/*
		 * Get the size and then do a modified for loop
		 * doing as many full loops as possible, then doing a modified remainder loop
		 * 
		 * 
		 * 
		 * 
		 */
		ArrayList<String> deckIds=new ArrayList<String>();
		ArrayList<String> deckNames=new ArrayList<String>();
		
		LinearLayout layout=(LinearLayout)findViewById(R.id.DeckList_layout_bottom);
		
		layout.setBackgroundColor(Color.DKGRAY);
		//layout.setBackgroundResource(R.drawable.blue_background);
		ScrollView scroll=new ScrollView(this);
		
		LinearLayout layoutOfLayouts=new LinearLayout(this);
		layoutOfLayouts.setOrientation(LinearLayout.VERTICAL);
		
		//innerLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		int index=0;
		Log.d("DEBUG NUMBERS", "ArrayLength: " + deckArray.length() + " 1/3: " + (deckArray.length()/3));
		for (index=0; index<(deckArray.length()/3); index++)
		{
			/*int index0=0;
			int index1=1;
			int index2=2;*/
			int indexBase=3*index;
			
			LinearLayout innerLayout=new LinearLayout(this);
			innerLayout.setOrientation(LinearLayout.HORIZONTAL);
			Button b0=makeListButtonHelper(deckIds,deckNames,deckArray,indexBase);
			Button b1=makeListButtonHelper(deckIds,deckNames,deckArray,indexBase+1);
			Button b2=makeListButtonHelper(deckIds,deckNames,deckArray,indexBase+2);
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
			Button b=makeListButtonHelper(deckIds,deckNames,deckArray,index);
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
		layout.addView(scroll);
		layout.invalidate();
	}
	
	
	
	
	public void makePicture(View view)
	{
		 Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		 startActivityForResult(takePictureIntent, 4);
		 
		
	}
	
	
	
	
	
	private Button makeListButtonHelper(ArrayList<String> deckIds, ArrayList<String> deckNames,JSONArray deckArray, int index)
	{
		
		try{
			JSONObject tempDeck=deckArray.getJSONObject(index);
			String deckid=tempDeck.getString("deck_id");
			String name=tempDeck.getString("name");
					
			
			deckIds.add(index, deckid);
			deckNames.add(index,name);
			Log.d("DEBUG name",name);
			Log.d("DEBUG id",deckid);
			
		
			Button b=new Button(this);
			//b.setBackgroundResource(R.drawable.gray_button_back);
			b.setText(name);
			b.setTag(deckid);
			b.setWidth(100);
			b.setHeight(50);
			b.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View view) {
	                 // Perform action on click
	            	 Log.d("DEBUG clicked and got tag:", view.getTag().toString());
	            	 Intent intent =new Intent(view.getContext() , ViewDeck.class);
	            	 intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_DECKID,view.getTag().toString());
	            	 intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION, sessionId);
	         	    intent.putExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER,username);
	         		startActivity(intent);
	             }
	         });
			Log.d("DEBUG", "Before adding button to scrollview");
			return b;
			
			}
			
			
		
		catch(Exception e) {
			 Log.d("Error", "Cannot turn parse JSONArray of decks_list");
			e.printStackTrace();
	       
	    }
		return null;
	}
	
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
		/*if (requestCode == 0) // the pictureIntent returned it
		{*/
			cameraHelper(intent);
	
		/*}*/
	}

	
	private void cameraHelper(Intent intent)
	{
		Bundle extras = intent.getExtras();
	    Bitmap bm = (Bitmap) extras.get("data");
	    LinearLayout ll=(LinearLayout)findViewById(R.id.DeckList_layout_bottom);
		//ImageView im = new ImageView(this);
		//im.setImageBitmap(bm);
		//ll.addView(im);
		//ll.invalidate();
	    
	    File path = Environment.getExternalStorageDirectory();
	    //String StrPath=path.getPath();
	    String fileName="cameraFile.jpg";
	    File f = new File(path,fileName);
	    //was takePictureIntent
	    mCurrentPhotoPath = f.getAbsolutePath();
	    //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
	    try {
	    	Log.d("WRITING","GOT INTO THE TRY STATEMENT");
	    	FileOutputStream out = new FileOutputStream(f);
	         bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
        } catch (Exception e) {
        	Log.d("WRITING","FAILED WRITING");
            e.printStackTrace();
        }
        
	    
	    //File f2=new File(mCurrentPhotoPath);
	   /* Bitmap bm2 = BitmapFactory.decodeFile(mCurrentPhotoPath);
	    ImageView im2 = new ImageView(this);
		im2.setImageBitmap(bm);
		ll.addView(im2);
        ll.invalidate();*/
        
	    galleryAddPic();
	    
	    Log.d("BEFORE POST", "RIGHT BEFORE HTTPPOST");
	    
	    String postUrl="http://www.flashyapp.com/api/add_image";
	    
		//HttpPost(postUrl,bm);
		
		//handleSmallCameraPhoto(bm);
		//handleSmallPhoto();
		
	    MIMEThread thread=new MIMEThread((Context)this, onResponseMIMEListener);
		thread.BeforeMakePic(username,sessionId, mCurrentPhotoPath);
		thread.execute(new String[]{null});
	    
	    Log.d("AFTER GALLERY CALL", "FINISHED GALLERY CALL");
	}
	
	
	private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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

}
