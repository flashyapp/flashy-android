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
    private ArrayList xcoords;
    private ArrayList ycoords;
    private float oldx;
    private float oldy;
   // private int[] xcoords;
   // private int[] ycoords;
    
    public DrawView(Context context) {
        super(context);
        coordinateInit();
        paint=new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        mpath=new Path();
       // mpath.moveTo(30, 40);
        mpath.lineTo(50, 50);
        oldx=50;
        oldy=50;
    }

    @Override
    public void onDraw(Canvas canvas) {
    	 canvas.drawColor(Color.WHITE);
    	 mpath.lineTo(10, 10);
    	 
    	canvas.drawPath(mpath,paint);
        //mcanvas.drawLine(0, 0, 20, 20, paint);
        //mcanvas.drawLine(20, 0, 0, 20, paint);
            
    }

    
    private void coordinateInit(){
    	xcoords=new ArrayList();
    	ycoords=new ArrayList();
    	
    	//xcoords.add
    	
    	
    }
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
       
    	float x = event.getX();
        float y = event.getY();
        
       /* float p1= 30;
		float p2= 30;
		float p3=80;//event.getX();
		float p4=80;//event.getY();
*/		Log.d("Drawing", "Got into drawing method 2");
		 /*Paint p = new Paint();
         p.setColor(Color.WHITE);
         p.setStyle(Paint.Style.STROKE);
         */
		float TOUCH_TOLERANCE=15;
		float dx = Math.abs(oldx - x);
		float dy = Math.abs(oldy - y);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mpath.moveTo(oldx, oldy);
	   	 	mpath.lineTo(x,y);
	   	 	oldx=x;
	   	 	oldy=y;
		}
		
   	 	
		/*//Canvas canvas = new Canvas();
         canvas.drawColor(Color.BLUE);

        canvas.drawLine(p1,p2,p3,p4,paint);
        //onDraw(canvas);
*/        invalidate();
		
        
        return true;
    }
}