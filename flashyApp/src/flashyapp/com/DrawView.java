package flashyapp.com;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class DrawView extends View {
    private Paint dotpaint;
    private Paint linepaint;
    private Paint whitelinepaint;
    private Path linepath;
    private Path dotpath;
    private ArrayList<Row> rows;
    private int touchedx;
    private int touchedy;
    private ArrayList<Integer> arrayy;
    private Row movVrow;
    private Context mcontext;
    private int photoWidth;
    private int photoHeight;
    private Bitmap backBitmap;
    private GestureDetector gd;
    private int movVrowIndex;
   
    
    //handles the various types of motions on the screen by the user
    GestureDetector.SimpleOnGestureListener sogl =
            new GestureDetector.SimpleOnGestureListener() {

    	
    // When the screen is first touched, mark the coordinates and 
    // find the row which the event will affect
    public boolean onDown(MotionEvent event) {
    	int x =(int) event.getX();
        int y = (int)event.getY();
        
        Log.d("GESTURE DETECTOR: ", "onDown registered");
        Log.d("Coordinates:", "X: "+x+"   Y:"+y);
        
        movVrow=null;
	    int index;
		for (index =1; index < arrayy.size(); index++)
    	{
			if (y>arrayy.get(index-1) && y<=arrayy.get(index)){
				
				movVrow=rows.get(index-1);
				movVrowIndex=index-1;
				movVrow.print();
				break;
			}

    	}
		
   	 	touchedx=x;
   	 	touchedy=y;
   	 	invalidate();
   	 	
   	 	return true;
    }

    //handle adding and deleting lines
    public boolean onSingleTapUp(MotionEvent event)
    {
    	int x =(int) event.getX();
        int y = (int)event.getY();
        DrawLines dl=(DrawLines)mcontext;
        //get all the booleans from the options menu of DrawLines activity
        boolean addRowbool=dl.getMenuAddRow();
        boolean addColbool=dl.getMenuAddCol();
        boolean deleteColbool=dl.getMenuDeleteCol();
        boolean deleteRowbool=dl.getMenuDeleteRow();
		
        
        if(addColbool)
        {
        	// make sure X is less than the width of the photo
        	if (x >= photoWidth){
        		return true;
        	}
        	//then add the column to each row
        	for(Row r: rows)
        	{
        		r.newCol(x);
        	}
        	invalidate();
        	dl.setMenuAddCol(false);
        }
        // add a row assuming that the click is in bounds
        else if (addRowbool)
        {
        	if (movVrow == null)
        		return true;
        	int oldTop=movVrow.getTop();
        	movVrow.setTop(y);
        	ArrayList<Integer> divs=movVrow.getDivs();
        	Row rnew=new Row(oldTop,y,divs,photoWidth, photoHeight);
        	rows.add(movVrowIndex+1, rnew);
        	arrayy.add(movVrowIndex+1, y);
        	
        	//reset the options menu item
        	dl.setMenuAddRow(false);
        	
        	invalidate();
        	
        	Log.d("DEBUG", "AFTER ADDED ROW");
        	
        }
       
        // delete a single vertical line
        else if (deleteColbool)
   	 	{
   	 		Log.d("BEFORE I DELETE!!", "This is right before the row delete call");
   	 		movVrow.deleteLine(x);
   	 		invalidate();
   	 		//reset the options menu item
   	 		dl.setMenuDeleteCol(false);
   	 
   	 	}
   	 	
		// delete a row
        else if (deleteRowbool)
        {
        	
        	 int index;
  			 for (index =1; index < arrayy.size(); index++)
  	    	{
  				
  				if (Math.abs(y-arrayy.get(index))<20 && arrayy.get(index)!= photoHeight){
  					
  					   Row bottomRow=rows.get(index-1);
	      			   Row topRow=rows.get(index);
	      			   
	      			   bottomRow.setTop(topRow.getTop());
	      			   rows.remove(index);
	      			   arrayy.remove(index);
	      			   //if there was a line to remove, then reset the options menu
	      			   dl.setMenuDeleteRow(false);
	      			   break;
	      			   
  				}

  	    	}
  			 
        }
		
		
		
   	 	invalidate();
		
        Log.d("GESTURE DETECTOR: ", "onSingleTap registered");
        Log.d("Coordinates:", "X: "+x+"   Y:"+y);
        
        return true;
    }
    
    //set up to handle the function, but doesn't implement it
    public boolean onFling (MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
    	int x1 =(int) e1.getX();
        int y1 = (int)e1.getY();
       
        int x2 =(int) e2.getX();
        int y2 = (int)e2.getY();
        
        Log.d("GESTURE DETECTOR: ", "onFling registered");
        Log.d("Coordinates:", "X1: "+x1+"   Y1:"+y1+"   X2: "+x2+"   Y2:"+y2);
        return true;
    }
    //set up to handle the function, but doesn't implement it
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
    //set up to handle the function, but doesn't implement it
    public void onLongPress(MotionEvent event) {
    	int x =(int) event.getX();
        int y = (int)event.getY();
        
    	 Log.d("GESTURE DETECTOR: ", "onLongPress registered");
    	 Log.d("Coordinates:", "X: "+x+"   Y:"+y);
    	 
    }
 
 };
    
   public DrawView(Context context, String coords, ArrayList<Row> inrows/*, ToggleButton deleteLine, ToggleButton addLine*/) {
        super(context);
     
      
        gd = new GestureDetector(context, sogl);
        
        //set the paint for the dots
        dotpaint=new Paint();
        dotpaint.setStyle(Paint.Style.FILL);
        dotpaint.setColor(Color.BLACK);
       
        //set the paint for the thin black line
        linepaint=new Paint();
        linepaint.setStyle(Paint.Style.STROKE);
        linepaint.setColor(Color.BLACK);
        
        //set the paint for fat white line
        whitelinepaint=new Paint();
        whitelinepaint.setStyle(Paint.Style.STROKE);
        whitelinepaint.setStrokeWidth(4);
        whitelinepaint.setColor(Color.WHITE);
        
        
        linepath=new Path();
        dotpath=new Path();
        rows=inrows;
        mcontext=context;
       
        //load the photo that was stored by the camera
        File path = Environment.getExternalStorageDirectory();
	    File dir=new File(path,MainActivity_LogIn.FILE_DIR);
	    //dir.mkdir();
	    
	    File f = new File(dir,MainActivity_LogIn.CAMERA_FILE);
	    
	    String mCurrentPhotoPath = f.getAbsolutePath();
    	
    	
    	//File f2=new File(mCurrentPhotoPath);
 	    backBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
 	    photoWidth=backBitmap.getWidth();
 	    photoHeight=backBitmap.getHeight();
 	    
 	    Log.d("Photo SIZE", "W: "+ photoWidth + "  H: "+photoHeight);
    	
 	    
 	    //make the rows and display them
        makeRows(coords);
        printRows();
        addCol(75);
        printRows();
        addRowsToPath();
    }

    
    //turn the coordinates into lines 
    private void makeRows(String coords)
    {
    	
    	 JSONArray divArray=null;
         try{
         	divArray=new JSONArray(coords);
         }catch(Exception e) {
 			 Log.d("Error", "Cannot turn string to array for divs");
 			e.printStackTrace();
 	       
 	    }
         
         int bottom=0;
         int top=0;
         arrayy=new ArrayList<Integer>() ;
         arrayy.add(0, top);
         
         ArrayList<Integer> verts;
         for (int index =0; index < divArray.length(); index++  )
 		{
        	verts=new ArrayList<Integer>();
 			try{
 				JSONArray rowArray=divArray.getJSONArray(index);
 				JSONArray colNums=rowArray.getJSONArray(1);
 				top=rowArray.getInt(0);
 				Log.d("DEBUG Lines", "Has a row at height: "+top);
 				
 				//add each vertical line to the array
 				for (int j=0; j<colNums.length(); j++){
 					verts.add(colNums.getInt(j));
 					Log.d("DEBUG Lines", "Row " + top+ " Has a col at : "+colNums.getInt(j));
 				}
 				
 			}
 			catch(Exception e) {
 				 Log.d("Error", "Cannot make the mini arrays");
 				e.printStackTrace();
 		       
 		    }
 			
 			//set the array of just the top coordinate of rows
 			arrayy.add(index+1,top);
 			//make a row and add it to the array of rows
 			rows.add(new Row(top,bottom,verts,photoWidth,photoHeight));
 			bottom=top;
 			Log.d("DEBUG","finished MakeRows");
 			
 		}
    	
    }
    // debugging tool
    private void printRows()
    {
    	for(Row r: rows)
    	{
    		r.print();
    	}
    }
    
    //add the line to every row at coordinate newX
    private void addCol(int newX)
    {	
    	for(Row r: rows)
		{
			r.newCol(newX);
		}
	}
    //add the lines and dots to the paths to be drawn
    private void addRowsToPath()
    {	
    	linepath=new Path();
    	dotpath=new Path();
    	for(Row r: rows)
		{
			linepath=r.addToLinePath(linepath);
			dotpath=r.addToDotPath(dotpath);
		}
	}
  
   
    @Override
    public void onDraw(Canvas canvas) {
    	
    	//redraw the background
    	canvas.drawBitmap(backBitmap,null,new Rect(0,0,photoWidth,photoHeight),null);
    	//get all the lines and dots
    	addRowsToPath();
    
    	//draw the lines and dots
    	canvas.drawPath(dotpath,dotpaint);
    	canvas.drawPath(linepath,whitelinepaint);
    	canvas.drawPath(linepath, linepaint);
            
    }

 
   // handle touch events on the screen
    // if the touch is a drag, handle it in a unique way
    // otherwise pass it on to the gesture detector
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	DrawLines dl=(DrawLines)mcontext;
    	boolean deleteRowbool=dl.getMenuDeleteRow();
    	boolean deleteColbool=dl.getMenuDeleteCol();
    	boolean addRowbool=dl.getMenuAddRow();
    	boolean addColbool=dl.getMenuAddCol();
    	
    	//make sure the action is a drag and no other line operations are happening
    	if (event.getAction()==MotionEvent.ACTION_MOVE && !deleteRowbool && !deleteColbool && !addRowbool && !addColbool){
    		
    		int x =(int) event.getX();
            int y = (int)event.getY();
      	   int deltax=Math.abs(touchedx-x);
      	   int deltay=Math.abs(touchedy-y);
      		
      	 // make sure the drag is significant enough
      	   if (deltax > 3 && deltay > 3){
      		   // horizontal drags are for moving vertical lines
	      	   if (deltax >= deltay){
	      		   Log.d("DRAG Direction","HORIZONTALLLLLL");
	      		   if (x<photoWidth){
	      			   if (movVrow != null)
	      					   movVrow.moveVerticalLineHorizontally(x);
	      		   }
	      	   }
	      	   //vertical drags are to drag rows
	      	   else{
	      		 
	      		 Log.d("DRAG Direction", "VERTICALLLLLLL");
	      		  		   
	      			 int index;
	      			 for (index =1; index < arrayy.size(); index++)
	      	    	{
	      				
	      				if (Math.abs(y-arrayy.get(index))<20 && arrayy.get(index)!= photoHeight){
	      					
	      					
	      					Log.d("ARRAY Y DRAG VERTICAL coords:", " "+ arrayy.get(index));
	      					
	      					Row bottomRow=rows.get(index-1);
	 	      			   Row topRow=rows.get(index);
	 	      			   bottomRow.setTop(y);
	 	      			   topRow.setBot(y);
	 	      			   arrayy.set(index, y);
	 	      			   break;
	 	      			   
	      				}
	      	    	}
	      			
	      			 //update the display
	      			 invalidate();
	      		
	      	   }
      	   
      	   }
      	  
      	   invalidate();
          return true;
    	}
    	// touch event wasn't a drag, listen for long clicks or single taps
    	else
    		return gd.onTouchEvent(event);
    		  
    }
    
    
}