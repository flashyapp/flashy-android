package flashyapp.com;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;

public class ViewHTML extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent=getIntent();
		String htmlStr=intent.getStringExtra("html_extra");
		
		WebView view=new WebView(this);
		
		setContentView(view);
		// Show the Up button in the action bar.
		
		//String summary = "<html><body>You scored <b>192</b> points.</body></html>";
		/*Html.ImageGetter imgget=new Html.ImageGetter() {
			
			@Override
			public Drawable getDrawable(String source) {
				if (source.equals("/sdcard/cameraFile.jpg"))
					Log.d("IMAGE Getter","Parsed the image correctly to:"+source);
				
				
				int id=R.drawable.ic_launcher;
				Drawable d=getResources().getDrawable(id);
				return d;
				
				//return null;
			}
		};*/
		/*should handle other tags eventually instead of the null*/
		//Spanned withImage=Html.fromHtml(htmlStr, imgget, null);
		view.loadUrl("file:///android_asset/htmlTest.html");
		

		Log.d("VIEWING", "HTML Should now be visable");
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_html, menu);
		return true;
	}

	
}
