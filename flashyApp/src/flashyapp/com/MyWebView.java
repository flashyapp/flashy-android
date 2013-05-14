package flashyapp.com;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;


// an extension of WebView to handle personalized touchEvents
public class MyWebView extends WebView {

	Context context;
	GestureDetector gd;
	ViewDeck activ;
	View arg;
	    
	
	   GestureDetector.SimpleOnGestureListener sogl =
	            new GestureDetector.SimpleOnGestureListener() {

	    public boolean onDown(MotionEvent event) {
	    	int x =(int) event.getX();
	        int y = (int)event.getY();
	        
	        Log.d("GESTURE DETECTOR: ", "onDown registered");
	        Log.d("Coordinates:", "X: "+x+"   Y:"+y);
	        
	   	 	
	   	 	return true;
	   }

	    //flip over the card if there is a single tap
	    public boolean onSingleTapUp(MotionEvent event)
	    {
	    	int x =(int) event.getX();
	        int y = (int)event.getY();
	        Log.d("DEBUG", "Trying to flip overrrrr");
	        
        	activ.flipOver(arg);
	        
	        Log.d("GESTURE DETECTOR: ", "onSingleTap registered");
	        Log.d("Coordinates:", "X: "+x+"   Y:"+y);
	    	return true;
	    }
	    
	    // on a swipe, move the card to the next or previous card depending on the motion
	    public boolean onFling (MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	    {
	    	int x1 =(int) e1.getX();
	        int y1 = (int)e1.getY();
	       
	        int x2 =(int) e2.getX();
	        int y2 = (int)e2.getY();
	        
	        
	        int deltax=Math.abs(x2-x1);
	        int deltay=Math.abs(y2-y1);
	        
	        Log.d("GESTURE DETECTOR: ", "onFling registered");
	        Log.d("Coordinates:", "X1: "+x1+"   Y1:"+y1+"   X2: "+x2+"   Y2:"+y2);
	        
	        if (deltax > deltay){
	        	//swipe from right to left
		        if (e1.getRawX() > e2.getRawX()) {
	                activ.prevCard(arg);
	            }
		        //swipe from left to right
		        else {
	                activ.prevCard(arg);
	            }
	        }
           
	        return true;
	    }
	    
	    //does nothing, there for future implementation
	    public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	    {
	    	int x1 =(int) e1.getX();
	        int y1 = (int)e1.getY();
	       
	        int x2 =(int) e2.getX();
	        int y2 = (int)e2.getY();
	        
	        Log.d("GESTURE DETECTOR: ", "onScroll registered");
	        Log.d("Coordinates:", "X1: "+x1+"   Y1:"+y1+"   X2: "+x2+"   Y2:"+y2);
	        return true;
	    } 
	    // there for future implementation, not currently doing a particular function
	    public void onLongPress(MotionEvent event) {
	    	int x =(int) event.getX();
	        int y = (int)event.getY();
	        
	    	 Log.d("GESTURE DETECTOR: ", "onLongPress registered");
	    	 Log.d("Coordinates:", "X: "+x+"   Y:"+y);
	    	
	    }
	 
	 };
	
	
	//constructor
	public MyWebView(Context context) {
        super(context);

        this.context = context;
        activ=(ViewDeck)context;
        gd = new GestureDetector(context, sogl);
        arg=this;
    }

	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		Log.d("DEBUG", "Got Motion Event");
        return gd.onTouchEvent(event);
    }
	
	
}

	
	
	

