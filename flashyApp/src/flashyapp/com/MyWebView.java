package flashyapp.com;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

public class MyWebView extends WebView {

	Context context;
	GestureDetector gd;
	ViewDeck activ;
	//Button b;
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
		        if (e1.getRawX() > e2.getRawX()) {
	                activ.prevCard(arg);
	            } else {
	                activ.prevCard(arg);
	            }
	        }
           
	        return true;
	    }
	    
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
	    
	    public void onLongPress(MotionEvent event) {
	    	int x =(int) event.getX();
	        int y = (int)event.getY();
	        
	    	 Log.d("GESTURE DETECTOR: ", "onLongPress registered");
	    	 Log.d("Coordinates:", "X: "+x+"   Y:"+y);
	    	
	    }
	 
	 };
	
	
	
	public MyWebView(Context context) {
        super(context);

        this.context = context;
        activ=(ViewDeck)context;
        gd = new GestureDetector(context, sogl);
       // b=new Button(context);
        arg=this;
    }

	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		Log.d("DEBUG", "Got Motion Event");
        return gd.onTouchEvent(event);
    }
	
	
}

	
	
	

