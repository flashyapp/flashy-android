package flashyapp.com;

import java.io.DataOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MyLogout extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_logout);
	
		String file=MainActivity_LogIn.SESSION_FILE;
		try{
			DataOutputStream out = 
                new DataOutputStream(openFileOutput(file, Context.MODE_PRIVATE));
			// out.writeUTF(sessionId);
			Log.d("LOGOUT","WRITING EMPTY FILE");
		}catch (IOException e) {
		        Log.i("Data Input Sample", "I/O Error");
		}
	
		Intent intent = new Intent(this, MainActivity_LogIn.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_logout, menu);
		return true;
	}

	
	
	
}
