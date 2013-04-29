package com.example.test3;

import java.util.ArrayList;

import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.Log;

public class Row {

	private float top;
	private float bottom;
	private ArrayList<Float> xcoords;
	
	public Row(float t, float b, ArrayList<Float> x)
	{
		top=t;
		bottom=b;
		xcoords=new ArrayList<Float>();
		//int index;
		for (Float f: x)
			xcoords.add(f);
	}
	
	//add a new vertical line on the row
	public void newCol(float newX)
	{
		int index=0;
		boolean added=false;
		if (xcoords.isEmpty()){
			xcoords.add(index,newX);
			Log.d("Debug","Just added first column to the row");
			return;
		}
		
		for (;index < xcoords.size();index++)
		{
			if (newX < xcoords.get(index)){
				Log.d("DEBUG", "Index of adding="+index);
				xcoords.add(index, newX);
				added=true;
				break;
			}
		}
		//add it as the last thing to the list
		if (!added)
			xcoords.add(index,newX);
		
		
	}

	
	public Path addToDotPath(Path path)
	{
		int radius=3;
		int minwidth=0;
		int maxwidth=300;
		path.addCircle(minwidth, top, radius, Direction.CW);
		path.addCircle(maxwidth, top, radius, Direction.CW);
		path.addCircle(minwidth, bottom, radius, Direction.CW);
		path.addCircle(maxwidth, bottom, radius, Direction.CW);
		
		for (Float x: xcoords){
			
			path.addCircle(x, bottom, radius, Direction.CW);
			path.addCircle(x, top, radius, Direction.CW);
		}
		return path;
			
	}	
	public Path addToLinePath(Path path)
	{
		
		int minwidth=0;
		int maxwidth=300;
		path.moveTo(minwidth, top);
		path.lineTo(maxwidth, top);
		/*path.moveTo(minwidth, bottom);
		path.lineTo(maxwidth, bottom-5);
		*/
		for (Float x: xcoords){
			
			path.moveTo(x, bottom);
			path.lineTo(x, top);
		}
		return path;
			
	}	
	
	public void print()
	{
		//Log.d("DEBUG", "Printing from print for each row");
		Log.d("ROW toString Bottom",bottom+"");
		Log.d("ROW toString Top",top+"");
		String str=" ";
		for (Float x: xcoords)
			str=str+" "+x.toString();
		Log.d("ROW toString X coords",str);
			
	}
	
	
	public void moveVerticalLineHorizontally(float newX)
	{
		boolean moved=false;
		float tolerance =15;
		for (Float x: xcoords){
			if (Math.abs(x-newX) <= tolerance){
				Log.d("Moved a Vertical", x + "to " + newX);
				int index=xcoords.indexOf(x);
				xcoords.set(index, newX);
				Log.d("New Value of Vertical:", ""+xcoords.get(index));
				moved=true;
				break;
			}
				
			
		}
		//return moved;
	}
	
	
	
}
