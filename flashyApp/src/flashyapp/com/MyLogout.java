package flashyapp.com;

import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import flashyapp.com.JSONThread.OnResponseListener;


// A centralized function to handle logouts from various activities
public class MyLogout extends Activity {

	
	protected OnResponseListener onResponseListener = new OnResponseListener() {
		 public void onReturnRegister(String error, JSONObject jresponse){}
		 public void onReturnDeleteDeck(String error, Context context){}
		 public void onReturnLogin(String error, JSONObject jresponse, String name){}
		 public void onReturnDeckFromImage(Context context, String mError, JSONObject jresponse){}
		 public void onReturnDecksPage(String error, JSONObject jresponse, Context context){}
		 public void onReturnSaveDeck(Context context, String mError, JSONObject jresponse){}
		
		 //User is logged out of server but needs to be logged out on the phone
		 public void onReturnLogout(String error){
			 
			 if (error == null){
				
					try{
						//delete SessionFile and deck List file
						deleteFile(MainActivity_LogIn.SESSION_FILE);
						deleteFile(MainActivity_LogIn.GETDECKS_FILE);
						
						Log.d("LOGOUT","WRITING EMPTY FILE");
					}catch (Exception e) {
					        Log.i("Logging out", "failed to delete the file");
					}
				
				 
			
				 // go on to the login page
				Intent intent = new Intent(MyLogout.this, MainActivity_LogIn.class);
				startActivity(intent);
			 }
			 
		 }
		 
	};
	
	
	
	
	
	
	
	
	
	//Creates an Activity and then immediately starts a JSONThread to logout the user from the server
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_logout);
	
		Intent intent=getIntent();
		String user=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_USER);
		String session=intent.getStringExtra(MainActivity_LogIn.INTENT_EXTRA_DATA_SESSION);
		
		JSONThread thread=new JSONThread((Context)this, onResponseListener, JSONThread.LOGOUT);
		 if (thread.wifiOn()){
			 thread.BeforeLogout(user,session);
			 thread.execute(new String[]{null});
		 }
		 else
			 MyJSON.WifiAlert(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_logout, menu);
		return true;
	}

	
	
	
}
