package com.example.test3;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;

public class Draws extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		 DrawView drawView = new DrawView(this);
	     drawView.setBackgroundColor(Color.WHITE);
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
