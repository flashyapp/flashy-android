package com.example.test3;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();
    //private Canvas mcanvas;
    private Path mpath;
    private ArrayList<Row> rows;
    //private ArrayList ycoords;
    private float oldx;
    private float oldy;
    private float[] arrayx;
    private float[] arrayy;
    private Row movVrow;
    
    public DrawView(Context context) {
        super(context);
        
        paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        mpath=new Path();
        arrayx=new float[2];
        arrayy=new float[4];
        
        arrayx[0]=50; arrayx[1]=100;
        arrayy[0]=20;arrayy[1]=40;arrayy[2]=60;arrayy[3]=80;
        rows=new ArrayList();
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
        oldx=50;
        oldy=50;
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
    	
    	for (index =0; index < arrayy.length; index++)
    	{
    		//FAILING IN HERE NEED TO DEBUG!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    		y=arrayy[index];
    		//Log.d("old and Y:",old_y+""+y);
    		rows.add(new Row(y,old_y,param));
    		old_y=y;
    	}
    	
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
    	mpath=new Path();
    	for(Row r: rows)
		{
			mpath=r.addToPath(mpath);
		}
	}
    
    
    @Override
    public void onDraw(Canvas canvas) {
    	 canvas.drawColor(Color.WHITE);
    	addRowsToPath();
    	
    	canvas.drawPath(mpath,paint);
        //mcanvas.drawLine(0, 0, 20, 20, paint);
        //mcanvas.drawLine(20, 0, 0, 20, paint);
            
    }

    
   
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	float x = event.getX();
        float y = event.getY();
    	
    	if (event.getAction() == MotionEvent.ACTION_DOWN){
	    	
	        //mpath=new Path();
	       /* float p1= 30;
			float p2= 30;
			float p3=80;//event.getX();
			float p4=80;//event.getY();
	*/		Log.d("Drawing", "Got into drawing method 2");
			 /*Paint p = new Paint();
	         p.setColor(Color.WHITE);
	         p.setStyle(Paint.Style.STROKE);
	         */
			/*float TOUCH_TOLERANCE=15;
			float dx = Math.abs(oldx - x);
			float dy = Math.abs(oldy - y);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mpath.moveTo(oldx, oldy);
		   	 	mpath.lineTo(x,y);
		   	 	oldx=x;
		   	 	oldy=y;
			}*/
			Log.d("Touch Coords:", ""+x+" , "+y);
			int index;
			for (index =1; index < arrayy.length; index++)
	    	{
				if (y>arrayy[index-1] && y<=arrayy[index]){
					movVrow=rows.get(index-1);
					break;
				}
					
	    	}
			
	   	 	
			/*//Canvas canvas = new Canvas();
	         canvas.drawColor(Color.BLUE);
	
	        canvas.drawLine(p1,p2,p3,p4,paint);
	        //onDraw(canvas);
	*/        invalidate();
		
       }
       
       
       if (event.getAction() == MotionEvent.ACTION_MOVE){
    	   movVrow.moveVertical(x);
    	   invalidate();
       }
        return true;
    }
}