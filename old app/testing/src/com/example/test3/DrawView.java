package com.example.test3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();
    private Canvas mcanvas;
    public DrawView(Context context) {
        super(context);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas canvas) {
    	mcanvas=canvas;
        mcanvas.drawLine(0, 0, 20, 20, paint);
        mcanvas.drawLine(20, 0, 0, 20, paint);
            
    }

    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
       
    	float x = event.getX();
        float y = event.getY();
        
        float p1= 30;
		float p2= 30;
		float p3=60;//event.getX();
		float p4=60;//event.getY();
		Log.d("Drawing", "Got into drawing method 2");
		 /*Paint p = new Paint();
         p.setColor(Color.WHITE);
         p.setStyle(Paint.Style.STROKE);
         canvas = new Canvas();
         canvas.drawColor(Color.BLUE);*/

        mcanvas.drawLine(p1,p2,p3,p4,paint);
        invalidate();
		
        
        return true;
    }
}