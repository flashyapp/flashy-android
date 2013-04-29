package com.example.test3;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
    Paint dotpaint;
    Paint linepaint;
    //private Canvas mcanvas;
    private Path linepath;
    private Path dotpath;
    private ArrayList<Row> rows;
    //private ArrayList ycoords;
    private float touchedx;
    private float touchedy;
    private float[] arrayx;
    private float[] arrayy;
    private Row movVrow;
    
    public DrawView(Context context) {
        super(context);
     
        
        dotpaint=new Paint();
        dotpaint.setStyle(Paint.Style.FILL);
        dotpaint.setColor(Color.BLACK);
       
        linepaint=new Paint();
        linepaint.setStyle(Paint.Style.STROKE);
        linepaint.setColor(Color.BLACK);
        
        
        linepath=new Path();
        dotpath=new Path();
        arrayx=new float[2];
        arrayy=new float[6];
        rows=new ArrayList();
        
        
        
        
       
        float minHeight=0;
        float maxHeight=200;
       
        arrayx[0]=200; arrayx[1]=400;
        arrayy[0]=0;arrayy[1]=40;arrayy[2]=80;
        arrayy[3]=120; arrayy[4]=160;arrayy[5]=400;
        
       
        makeRows();
        printRows();
        addCol(75);
        rows.get(1).newCol(67);
        printRows();
        
        
        addRowsToPath();
        
       /* mpath.moveTo(30, 50);
        mpath.lineTo(50, 50);
        mpath.moveTo(80, 200);
        mpath.lineTo(80, 250);*/
        /*oldx=50;
        oldy=50;*/
    }

    
    
    private void makeRows()
    {
    	ArrayList<Float> param=new ArrayList();
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
    	}
    	//Log.d("DEBUG","finished MakeRows");
    }
    
    private void printRows()
    {
    	for(Row r: rows)
    	{
    		r.print();
    	}
    }
    
    private void addCol(float newX)
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
    	Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ninja);
    	//canvas=new Canvas(bitmap);
    	//canvas.setBitmap(bitmap);
    	canvas.drawBitmap(bitmap,null,new Rect(0,0,200,200),null);
    	
    	//canvas.drawColor(Color.WHITE);
    	addRowsToPath();
    
    	canvas.drawPath(dotpath,dotpaint);
    	canvas.drawPath(linepath,linepaint);
    	
    	
        //mcanvas.drawLine(0, 0, 20, 20, paint);
        //mcanvas.drawLine(20, 0, 0, 20, paint);
            
    }

    
   
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	float x = event.getX();
        float y = event.getY();
    	
    	if (event.getAction() == MotionEvent.ACTION_DOWN){
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
			/*//Canvas canvas = new Canvas();
	         canvas.drawColor(Color.BLUE);
	
	        canvas.drawLine(p1,p2,p3,p4,paint);
	        //onDraw(canvas);
	*/        invalidate();
		
       }
       
       
       if (event.getAction() == MotionEvent.ACTION_MOVE && movVrow != null){
    	   
    	   Log.d("Drag Coords:", ""+x+" , "+y);
    	   float deltax=Math.abs(touchedx-x);
    	   float deltay=Math.abs(touchedy-y);
    			
    	   Log.d("Diff of Coords", ""+deltax+"   ,   "+deltay );
    	   
    	   if (deltax >= deltay){
    		   Log.d("DRAG Direction","HORIZONTALLLLLL");
    		   movVrow.moveVerticalLineHorizontally(x);
    	   }
    	   else{
    		   Log.d("DRAG Direction", "VERTICALLLLLLL");
    		   //need to handle that TWO rows would have a horizontal line coordinate change....
    	   }
    	   
    	 
    	  
    	   invalidate();
       }
        return true;
    }
}