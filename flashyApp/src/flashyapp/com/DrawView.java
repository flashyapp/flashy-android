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


public class DrawView extends View /*implements OnLongClickListener*/{
    Paint dotpaint;
    Paint linepaint;
    Paint whitelinepaint;
    //private Canvas mcanvas;
    private Path linepath;
    private Path dotpath;
    private ArrayList<Row> rows;
    //private ArrayList ycoords;
    private int touchedx;
    private int touchedy;
   // private float[] arrayx;
    private ArrayList<Integer> arrayy;
    private Row movVrow;
    private Context mcontext;
    private int photoWidth;
    private int photoHeight;
    private Bitmap backBitmap;
   /* private ToggleButton mdeleteLine;
    private ToggleButton maddLine;*/
    private GestureDetector gd;
    private int movVrowIndex;
   
    
    
    GestureDetector.SimpleOnGestureListener sogl =
            new GestureDetector.SimpleOnGestureListener() {

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
		
		
		if (movVrow==null)
			Log.d("COULDNT FIND A ROW", "CAUSE OF ERROR");
		
   	 	touchedx=x;
   	 	touchedy=y;
		
   	 	
   	 	invalidate();
   	 	
   	 	return true;
    }

    
    public boolean onSingleTapUp(MotionEvent event)
    {
    	int x =(int) event.getX();
        int y = (int)event.getY();
        DrawLines dl=(DrawLines)mcontext;
        boolean addRowbool=dl.getMenuAddRow();
        boolean addColbool=dl.getMenuAddCol();
        boolean deleteColbool=dl.getMenuDeleteCol();
        boolean deleteRowbool=dl.getMenuDeleteRow();
		
        if(addColbool)
        {
        	if (x >= photoWidth){
        		return true;
        	}
        	for(Row r: rows)
        	{
        		r.newCol(x);
        	}
        	invalidate();
        	dl.setMenuAddCol(false);
        }
        else if (addRowbool)
        {
        	if (movVrow == null)
        		return true;
        	
        	//Add row
        	Log.d("Added a row", "in theory");
        
        	
        	
        	for(Row r: rows)
        	{
        		r.print();
        	}
        	for (int w: arrayy)
        	{
        		Log.d("ArrayY: ", ""+w+"  ");
        	}
        	
        	
        	int oldTop=movVrow.getTop();
        	movVrow.setTop(y);
        	ArrayList<Integer> divs=movVrow.getDivs();
        	Row rnew=new Row(oldTop,y,divs,photoWidth, photoHeight);
        	rows.add(movVrowIndex+1, rnew);
        	arrayy.add(movVrowIndex+1, y);
        	//arrayy.add(movVrowIndex+2,oldTop);
        	
        	dl.setMenuAddRow(false);
        	
        	invalidate();
        	
        	Log.d("DEBUG", "AFTER ADDED ROW");
        	
        	for(Row r: rows)
        	{
        		r.print();
        	}
        	for (int w: arrayy)
        	{
        		Log.d("ArrayY: ", ""+w+"  ");
        	}
        	
        }
       
        
        
        
        if (deleteColbool)
   	 	{
   	 		Log.d("BEFORE I DELETE!!", "This is right before the row delete call");
   	 		movVrow.deleteLine(x);
   	 		invalidate();
   	 		dl.setMenuDeleteCol(false);
   	 
   	 	}
   	 	
		
		
        if (deleteRowbool)
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
	      			   dl.setMenuDeleteRow(false);
	      			   break;
	      			   
  				}

  	    	}
  			   
  			 
  			 
  			 
  			 
  			 Log.d("DEBUG", "AFTER DELETE ROW");
          	
          	for(Row r: rows)
          	{
          		r.print();
          	}
          	for (int w: arrayy)
          	{
          		Log.d("ArrayY: ", ""+w+"  ");
          	}
   			   
   			   
   			   
   			   
   		   
        }
		
		
		
   	 	invalidate();
		
        
        
        
        
        
        
        
        
        
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
        
        Log.d("GESTURE DETECTOR: ", "onFling registered");
        Log.d("Coordinates:", "X1: "+x1+"   Y1:"+y1+"   X2: "+x2+"   Y2:"+y2);
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
        
        
        
       /* DrawLines dl=(DrawLines)mcontext;
        boolean deleteRowbool=dl.getMenuDeleteRow();
        boolean deleteColbool=dl.getMenuDeleteCol();
        
		
		
		if (deleteColbool)
   	 	{
   	 		Log.d("BEFORE I DELETE!!", "This is right before the row delete call");
   	 		movVrow.deleteLine(x);
   	 		invalidate();
   	 		dl.setMenuDeleteCol(false);
   	 
   	 	}
   	 	
		
		
        if (deleteRowbool)
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
	      			   dl.setMenuDeleteRow(false);
	      			   break;
	      			   
  				}

  	    	}
  			   
  			 
  			 
  			 
  			 
  			 Log.d("DEBUG", "AFTER DELETE ROW");
          	
          	for(Row r: rows)
          	{
          		r.print();
          	}
          	for (int w: arrayy)
          	{
          		Log.d("ArrayY: ", ""+w+"  ");
          	}
   			   
   			   
   			   
   			   
   		   
        }
		
		
		
   	 	invalidate();*/
		
		
       // theListener.onLongClick(MyWebView.this);
    	 Log.d("GESTURE DETECTOR: ", "onLongPress registered");
    	 Log.d("Coordinates:", "X: "+x+"   Y:"+y);
    	 
    	 
    	 
    	 
    }
 
   
    
 };
    
    
    
    
    
    
    
    
    
    public DrawView(Context context, String coords, ArrayList<Row> inrows/*, ToggleButton deleteLine, ToggleButton addLine*/) {
        super(context);
     
       /* this.setOnLongClickListener(this);*/
        
        gd = new GestureDetector(context, sogl);
        //gd.setIsLongpressEnabled(false);
        
       /* mdeleteLine=deleteLine;
        maddLine=addLine;*/
        dotpaint=new Paint();
        dotpaint.setStyle(Paint.Style.FILL);
        dotpaint.setColor(Color.BLACK);
       
        linepaint=new Paint();
        linepaint.setStyle(Paint.Style.STROKE);
        linepaint.setColor(Color.BLACK);
        
       whitelinepaint=new Paint();
        whitelinepaint.setStyle(Paint.Style.STROKE);
        whitelinepaint.setStrokeWidth(4);
        whitelinepaint.setColor(Color.WHITE);
        
        
        linepath=new Path();
        dotpath=new Path();
       // arrayx=new float[2];
        //arrayy=new float[6];
        rows=inrows;
        mcontext=context;
       
       /* float minHeight=0;
        float maxHeight=200;
       
        arrayx[0]=200; arrayx[1]=400;
        arrayy[0]=0;arrayy[1]=40;arrayy[2]=80;
        arrayy[3]=120; arrayy[4]=160;arrayy[5]=400;
        */
        
        
        File path = Environment.getExternalStorageDirectory();
	    File dir=new File(path,MainActivity_LogIn.FILE_DIR);
	    //dir.mkdir();
	    
	    File f = new File(dir,MainActivity_LogIn.CAMERA_FILE);
	    
	    String mCurrentPhotoPath = f.getAbsolutePath();
    	
    	
    	//File f2=new File(mCurrentPhotoPath);
 	    backBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
 	    photoWidth=backBitmap.getWidth();
 	    photoHeight=backBitmap.getHeight();
 	    
 	    Log.d("SIZEEEEEEEEEEEEEEEEEEEEEEEEEEEE", "W: "+ photoWidth + "  H: "+photoHeight);
    	
 	   
        
        
        
        
        
        
        
        
        
       
        makeRows(coords);
        printRows();
        addCol(75);
       // rows.get(1).newCol(67);
        printRows();
        
        
        addRowsToPath();
        
       /* mpath.moveTo(30, 50);
        mpath.lineTo(50, 50);
        mpath.moveTo(80, 200);
        mpath.lineTo(80, 250);*/
        /*oldx=50;
        oldy=50;*/
    }

    
    
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
 				
 				
 				for (int j=0; j<colNums.length(); j++){
 					verts.add(colNums.getInt(j));
 					Log.d("DEBUG Lines", "Row " + top+ " Has a col at : "+colNums.getInt(j));
 				}
 				
 			}
 			catch(Exception e) {
 				 Log.d("Error", "Cannot make the mini arrays");
 				e.printStackTrace();
 		       
 		    }
 			arrayy.add(index+1,top);
 			rows.add(new Row(top,bottom,verts,photoWidth,photoHeight));
 			bottom=top;
 			Log.d("DEBUG","finished MakeRows");
 			
 		}
    	
    	/*ArrayList<Float> param=new ArrayList();
    	int index;
    	for (index =0; index < arrayx.length; index++){
    		
    		param.add(arrayx[index]);
    	}
    	for(Float f: param)
    		Log.d("PARAM ARRAY", f+"");
    	
    	float old_y=0;
    	float y;
    	
    	//Starts at 1 so we can have two pointers in the array
    	for (index =1; index < arrayy.length; index++)
    	{
    		    		
    		y=arrayy[index];
    		//Log.d("old and Y:",old_y+""+y);
    		rows.add(new Row(y,old_y,param));
    		old_y=y;
    	}*/
    	//Log.d("DEBUG","finished MakeRows");
    }
    
    private void printRows()
    {
    	for(Row r: rows)
    	{
    		r.print();
    	}
    }
    
    private void addCol(int newX)
    {	
    	for(Row r: rows)
		{
			r.newCol(newX);
		}
	}
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
    	
    	
    	
    	
    	
    	//Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flashy_logo_word);
    	//canvas=new Canvas(bitmap);
    	//canvas.setBitmap(bitmap);
    	canvas.drawBitmap(backBitmap,null,new Rect(0,0,photoWidth,photoHeight),null);
    	
    	//canvas.drawColor(Color.WHITE);
    	addRowsToPath();
    
    	canvas.drawPath(dotpath,dotpaint);
    	canvas.drawPath(linepath,whitelinepaint);
    	canvas.drawPath(linepath, linepaint);
    	
    	
        //mcanvas.drawLine(0, 0, 20, 20, paint);
        //mcanvas.drawLine(20, 0, 0, 20, paint);
            
    }

    
   /* @Override
    public boolean onLongClick(View v) {
    	
       Log.d("LONGGGGGGGGGGGGGGGG ", "CLICKKKKKKKKKKKKKKKKKKKKKK works");
        return true;
    }
    */
  
   
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	DrawLines dl=(DrawLines)mcontext;
    	boolean deleteRowbool=dl.getMenuDeleteRow();
    	boolean deleteColbool=dl.getMenuDeleteCol();
    	boolean addRowbool=dl.getMenuAddRow();
    	boolean addColbool=dl.getMenuAddCol();
    	/* Boolean dChecked=mdeleteLine.isChecked();
    		//Log.d("Delete Button status", ""+dChecked);
    		Boolean addChecked=maddLine.isChecked();
    		//Log.d("Add Button status", ""+addChecked);
*/    	
    	
    	
    	if (event.getAction()==MotionEvent.ACTION_MOVE && !deleteRowbool && !deleteColbool && !addRowbool && !addColbool){
    		//Log.d("MOVE ACTION", "Not passed to gesture!");
    		int x =(int) event.getX();
            int y = (int)event.getY();
    		//Log.d("Drag Coords:", ""+x+" , "+y);
      	   int deltax=Math.abs(touchedx-x);
      	   int deltay=Math.abs(touchedy-y);
      		
      	  // Log.d("Diff of Coords", ""+deltax+"   ,   "+deltay );
      	   if (deltax > 3 && deltay > 3){
	      	   if (deltax >= deltay){
	      		   Log.d("DRAG Direction","HORIZONTALLLLLL");
	      		   if (x<photoWidth){
	      			   if (movVrow != null)
	      					   movVrow.moveVerticalLineHorizontally(x);
	      		   }
	      	   }
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
	      			   
	      			   
	      			   
	      			   
	      			   
	      			 Log.d("DEBUG", "AFTER MOVE ROW");
	             	
	             	for(Row r: rows)
	             	{
	             		r.print();
	             	}
	             	for (int w: arrayy)
	             	{
	             		Log.d("ArrayY: ", ""+w+"  ");
	             	}
	      			   
	      			   
	      			   
	      			   invalidate();
	      		   
	      		   
	      		   
	      		   //need to handle that TWO rows would have a horizontal line coordinate change....
	      	   }
      	   
      	   }
      	  
      	   invalidate();
          return true;
    	}
    	else
    		return gd.onTouchEvent(event);
    		  
    }
    
    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	int x =(int) event.getX();
        int y = (int)event.getY();
        
        Boolean dChecked=mdeleteLine.isChecked();
   		Log.d("Delete Button status", ""+dChecked);
   		Boolean addChecked=maddLine.isChecked();
   		Log.d("Add Button status", ""+addChecked);
   	 
        if(addChecked)
        {
        	for(Row r: rows)
        	{
        		r.newCol(x);
        	}
        	invalidate();
        	return false;
        }
   		
    	if (event.getAction() == MotionEvent.ACTION_DOWN && !addChecked){
	    	movVrow=null;
	       
			Log.d("Drawing", "Got into drawing method 2");
			 
			Log.d("Touch Coords:", ""+x+" , "+y);
			int index;
			Log.d("LENGTHS: array, arrayList", arrayy.length+","+rows.size());
			
			for (index =1; index < arrayy.length; index++)
	    	{
				if (y>arrayy[index-1] && y<=arrayy[index]){
					
					movVrow=rows.get(index-1);
					movVrow.print();
					break;
				}
	
	    	}
			
	   	 	touchedx=x;
	   	 	touchedy=y;
			//Canvas canvas = new Canvas();
	         
	        //onDraw(canvas);
	      
	   	 	if (dChecked)
	   	 	{
	   	 	Log.d("BEFORE I DELETE!!", "This is right before the row delete call");
	   	 		movVrow.deleteLine(x);
	   	 		invalidate();
	   	 		return false;
	   	 		
	   	 	}
	   	 	
	   	 	invalidate();
	   	 	
	   	 	return true;
       }
       
       
       if (event.getAction() == MotionEvent.ACTION_MOVE && movVrow != null && !dChecked && !addChecked){
    	   
    	   Log.d("Drag Coords:", ""+x+" , "+y);
    	   int deltax=Math.abs(touchedx-x);
    	   int deltay=Math.abs(touchedy-y);
    			
    	   Log.d("Diff of Coords", ""+deltax+"   ,   "+deltay );
    	   
    	   if (deltax >= deltay){
    		   Log.d("DRAG Direction","HORIZONTALLLLLL");
    		   if (x<192)
    			   movVrow.moveVerticalLineHorizontally(x);
    	   }
    	   else{
    		   Log.d("DRAG Direction", "VERTICALLLLLLL");
    		   //need to handle that TWO rows would have a horizontal line coordinate change....
    	   }
    	   
    	 
    	  
    	   invalidate();
       }
        return true;
    }*/
}