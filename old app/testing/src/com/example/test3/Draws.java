package com.example.test3;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class Draws extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*LinearLayout ll=new LinearLayout(this);
		ll.setOrientation(ll.VERTICAL);
		*/
		 DrawView drawView = new DrawView(this);
	    // drawView.setBackgroundColor(Color.WHITE);
		// Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ninja);
		 /*Drawable image = getResources().getDrawable( R.drawable.ninja );
		 try{
			 drawView.setBackgroundDrawable(image);
		 }catch (Exception e)
		 {
			 Log.d("ISSUE with versions", "Didn't set the background");
		 }*/
		 
	   // Button bb=new Button(this);
	    //bb.setText("BUTTONNNNN");
	    //ll.addView(bb);
	   // ll.addView(drawView);
	    
	     setContentView(drawView);
		
		//setContentView(R.layout.activity_draws);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.draws, menu);
		return true;
	}
	
	
	

}
