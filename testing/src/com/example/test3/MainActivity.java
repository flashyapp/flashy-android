package com.example.test3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.test3.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);

	}
	
	
	public void displayMessageFromHttp(String message)
	{
		
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		//EditText editText = (EditText) findViewById(R.id.edit_message);
		//String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);

		
	}
	
	public void pictureFunc(View view){
		
		dispatchTakePictureIntent(0);
		
	}
	
	
	private void dispatchTakePictureIntent(int actionCode) {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    /*File f=null;
		try {
			Log.d("DEBUG", "TESTTTTTTTTTTTTTTTTT1");
			f = createImageFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (f == null)
			Log.d("DEBUG","THIS IS NULL AUGH");
		else{
			String s=(String)f.getPath();
			String s2="TESTING";
			Log.d("DEBUG", s2);
			Log.d("DEBUG","AFTER");
			Log.d("DEBUG", "TESTTTTTTTTTTTTTTTTT2");
		
		
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		}*/
		startActivityForResult(takePictureIntent, actionCode);
	   /* int result;
	    Intent returnedIntent;*/
	   // onActivityResult(actionCode,result,returnedIntent);
	    
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 0) // the pictureIntent returned it
		{
			Bundle extras = data.getExtras();
		    Bitmap bm = (Bitmap) extras.get("data");
		   // File fm=new File(extras.get("data"));
		    String postUrl="http://www.flashyapp.com/api/add_image";
			HttpPost(postUrl,bm);
		    handleSmallCameraPhoto(bm);
			
		}
	}
	
	private void handleSmallCameraPhoto(Bitmap bm) {
	    
		LinearLayout Previews = (LinearLayout)findViewById(R.id.layoutID);

	   /* ImageView img = new ImageView(this);
		img.setImageResource(R.drawable.ninja);
		img.setId(100);
		Previews.addView(img); 
		img.invalidate(); */
		
	    
	    
		ImageView img2 = new ImageView(this);
		img2.setImageBitmap(bm);
		img2.setId(101);
		Previews.addView(img2); 
		img2.invalidate(); 
		Previews.invalidate();
	    	    
	}
	
	
	
	

	
	
	
	
	public void httpReq(View view)
	{
		String getUrl="http://www.flashyapp.com/api/get_images";
		String postUrl="http://www.flashyapp.com/api/add_image";
		//HttpPost(postUrl,bm);
		HttpGet(getUrl);
	}

	
	
	
	public void HttpPost(String url, Bitmap bm)
	{
		 HttpClient httpClient = new DefaultHttpClient();

		    // Prepare a request object
		    HttpPost httpPost = new HttpPost(url); 
		 // Building post parameters, key and value pair
		   
		  // List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		    //nameValuePair.add(new BasicNameValuePair("file", bm));
		   
		    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		    
		    //File file = new File("/data/data/test3/ic_launcher-web.png");
		    
		    
		    File path = Environment.getExternalStorageDirectory();
		    
		    
		    //File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		    
		    
			 createExternalStoragePrivatePicture();
			    
			   
			   
			 Log.w("BEFORE READING", "RIGHT BEFORE I READ");
		    
		    
		    
		    if (path != null) {
		    	 File file = new File(path, "TestStoredNinja.jpg");
		    
		    entity.addPart("file", new FileBody(file));
		// entity.addPart("file", bm);
		  
		    
		 // Url Encoding the POST parameters
		   // try {
		        httpPost.setEntity(entity);
		    
		    /*catch (UnsupportedEncodingException e) {
		        // writing error to Log
		        e.printStackTrace();
		        
		        
		    }*/
		        Log.d("DEBUGGING","before execute!");
		    }
		 // Making HTTP Request
		    try {
		        HttpResponse response = httpClient.execute(httpPost);
		     
		        // writing response to log
		        Log.d("Http Response:", response.toString());
		     
		    } catch (ClientProtocolException e) {
		    	 Log.d("DEBUGGING","FAILEDDD on exception");
		    	
		    	// writing exception to log
		        e.printStackTrace();
		     
		    } catch (IOException e) {
		    	Log.d("DEBUGGING","FAILEDDD on exception");
		        // writing exception to log
		        e.printStackTrace();
		    }
	}
	
	
	
	
	
	
	
	
	
	//doing http request
	public /*static*/ void HttpGet(String url)
	{

	    HttpClient httpclient = new DefaultHttpClient();

	    // Prepare a request object
	    HttpGet httpget = new HttpGet(url); 

	    // Execute the request
	    HttpResponse response;
	    try {
	        response = httpclient.execute(httpget);
	        // Examine the response status
	        Log.i("Praeda",response.getStatusLine().toString());

	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();
	        // If the response does not enclose an entity, there is no need
	        // to worry about connection release

	        if (entity != null) {

	            // A Simple JSON Response Read
	            InputStream instream = entity.getContent();
	            
	            
	            //need to create a converter for json!!!!!!
	            
	            
	            String result= convertStreamToString(instream);
	            // now you have the string representation of the HTML request
	            
	            
	            instream.close();
	            

	            displayMessageFromHttp(result);

	        }
	        else{
	        	String failede="failedentity";
	        	displayMessageFromHttp(failede);
	        }
	        /*String failed="failed";
	        displayMessageFromHttp(failed);*/


	    } catch (Exception e) {
	    	String failed2="failed2";
	        displayMessageFromHttp(failed2);
	    }
	}
	
	
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	
	//Saving the photo
	private File getMYAlbumDir()
	{
		File storageDir = new File (
	
		    Environment.getExternalStorageDirectory()
		        + "Pictures/"
		        + "testDir/"
		);
		return storageDir;
	}
	
	private File createImageFile() throws IOException 
	{
		Log.d("DEBUG", "TESTTTTTTTTTTTTTTTTT3");
	    // Create an image file name
	    String timeStamp = 
	        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "ninjaTest" + timeStamp + "_";
	    Log.d("DEBUG", "TESTTTTTTTTTTTTTTTTT4");
	    File image = File.createTempFile(
	        imageFileName, 
	        "ninjaTest", 
	        getMYAlbumDir()
	    ); 
	    Log.d("DEBUG", image.getAbsolutePath());
	    Log.d("DEBUG", "INFUNC afterrrrrr");
	    //mCurrentPhotoPath = image.getAbsolutePath();
	    return image;
	}
	
	
	
	*/
	
	
	
	
	
	
	
	
	
	
	
	void createExternalStoragePrivatePicture() {
	    // Create a path where we will place our picture in our own private
	    // pictures directory.  Note that we don't really need to place a
	    // picture in DIRECTORY_PICTURES, since the media scanner will see
	    // all media in these directories; this may be useful with other
	    // media types such as DIRECTORY_MUSIC however to help it classify
	    // your media for display to the user.
	   // File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		
		File path = Environment.getExternalStorageDirectory();
	    File file = new File(path, "TestStoredNinja.jpg");

	    try {
	    	//make sure directory exists
	    	path.mkdirs();

	    	
	        // Very simple code to copy a picture from the application's
	        // resource into the external file.  Note that this code does
	        // no error checking, and assumes the picture is small (does not
	        // try to copy it in chunks).  Note that if external storage is
	        // not currently mounted this will silently fail.
	        InputStream is = getResources().openRawResource(R.drawable.ninja);
	        OutputStream os = new FileOutputStream(file);
	        byte[] data = new byte[is.available()];
	        is.read(data);
	        os.write(data);
	        is.close();
	        os.close();

	        /*// Tell the media scanner about the new file so that it is
	        // immediately available to the user.
	        MediaScannerConnection.scanFile(this,
	                new String[] { file.toString() }, null,
	                new MediaScannerConnection.OnScanCompletedListener() {
	            public void onScanCompleted(String path, Uri uri) {
	                Log.i("ExternalStorage", "Scanned " + path + ":");
	                Log.i("ExternalStorage", "-> uri=" + uri);
	            }
	        });*/
	    } catch (IOException e) {
	        // Unable to create file, likely because external storage is
	        // not currently mounted.
	        Log.w("ExternalStorage", "Error writing " + file, e);
	    }
	    
	    Log.d("SAVINGGGGGG", "PICTURE WAS SAVED SUPPOSEDLY!!!!!");
	}
/*
	void deleteExternalStoragePrivatePicture() {
	    // Create a path where we will place our picture in the user's
	    // public pictures directory and delete the file.  If external
	    // storage is not currently mounted this will fail.
	    File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
	    if (path != null) {
	        File file = new File(path, "DemoPicture.jpg");
	        file.delete();
	    }
	}
*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
