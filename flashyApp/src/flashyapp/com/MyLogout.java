package flashyapp.com;

import java.io.DataOutputStream;
import java.io.IOException;

import org.json.JSONObject;

import flashyapp.com.JSONThread.OnResponseListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MyLogout extends Activity {

	
	
	
	
	protected OnResponseListener onResponseListener = new OnResponseListener() {
		 public void onReturnRegister(String error, JSONObject jresponse){}
		 public void onReturnLogin(String error, JSONObject jresponse, String name){}
		 public void onReturnDeckFromImage(Context context){}
		 public void onReturnDecksPage(String error, JSONObject jresponse, Context context){}
		 public void onReturnLogout(String error){
			 
			 if (error == null){
				 String file=MainActivity_LogIn.SESSION_FILE;
					try{
						DataOutputStream out = 
			                new DataOutputStream(openFileOutput(file, Context.MODE_PRIVATE));
						/*
						 * 
						 * 
						 * Overwrite all files!!! including getlists and resources? that seems a waste....
						 * maybe make a folder user, and build all my structure inside of it?
						 * 
						 * 
						 * 
						 */
						// out.writeUTF(sessionId);
						Log.d("LOGOUT","WRITING EMPTY FILE");
					}catch (IOException e) {
					        Log.i("Data Input Sample", "I/O Error");
					}
				
				 
				 
				 
				Intent intent = new Intent(MyLogout.this, MainActivity_LogIn.class);
				startActivity(intent);
			 }
			 
		 }
	};
	
	
	
	
	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_logout);
	
		Intent intent=getIntent();
		String user=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER);
		String session=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION);
		
		JSONThread thread=new JSONThread((Context)this, onResponseListener, JSONThread.LOGOUT);
		thread.BeforeLogout(user,session);
		thread.execute(new String[]{null});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_logout, menu);
		return true;
	}

	
	
	
}
