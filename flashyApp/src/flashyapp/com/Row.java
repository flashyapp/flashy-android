package flashyapp.com;

import java.util.ArrayList;

import org.json.JSONArray;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.Log;

public class Row {

	private int top;
	private int bottom;
	private ArrayList<Integer> xcoords;
	private int maxWidth;
	private int maxHeight;
	
	//constructor
	public Row(int t, int b, ArrayList<Integer> x, int width, int height)
	{
		top=t;
		bottom=b;
		xcoords=new ArrayList<Integer>();
		for (int f: x)
			xcoords.add(f);
		maxWidth=width;
		maxHeight=height;
	}
	
	//add a new vertical line on the row
	public void newCol(int newX)
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

	
	//add dots to the path to be drawn
	//independent from lines for ease of drawing
	public Path addToDotPath(Path path)
	{
		int radius=3;
		int minwidth=0;
		
		path.addCircle(minwidth, top, radius, Direction.CW);
		path.addCircle(maxWidth, top, radius, Direction.CW);
		path.addCircle(minwidth, bottom, radius, Direction.CW);
		path.addCircle(maxWidth, bottom, radius, Direction.CW);
		
		for (int x: xcoords){
			
			path.addCircle(x, bottom, radius, Direction.CW);
			path.addCircle(x, top, radius, Direction.CW);
		}
		return path;
			
	}	
	
	//add lines to path for drawing on screen
	public Path addToLinePath(Path path)
	{
		
		int minwidth=0;
		
		path.moveTo(minwidth, top);
		path.lineTo(maxWidth, top);
		for (int x: xcoords){
			
			path.moveTo(x, bottom);
			path.lineTo(x, top);
		}
		return path;
			
	}	
	
	//Debugging tool
	public void print()
	{
		Log.d("ROW toString Bottom",bottom+"");
		Log.d("ROW toString Top",top+"");
		String str=" ";
		for (Integer x: xcoords)
			str=str+" "+x.toString();
		Log.d("ROW toString X coords",str);
			
	}
	
	//handles dragging of vertical lines
	public void moveVerticalLineHorizontally(int newX)
	{
		float tolerance =25;
		for (int x: xcoords){
			
			if (x==0 || x==maxWidth)
				continue;
			
			if (Math.abs(x-newX) <= tolerance){
				Log.d("Moved a Vertical", x + "to " + newX);
				int index=xcoords.indexOf(x);
				xcoords.set(index, newX);
				Log.d("New Value of Vertical:", ""+xcoords.get(index));
				
				
				sortArray();

				break;
			}
				
			
		}
		
	}
	
	//in case lines cross over each other, makes sure lines are in order after each touch event
	public void sortArray()
	{
		for (int i=0; i<xcoords.size()-1; i++)
		{
			if (xcoords.get(i) >= xcoords.get(i+1)){
				int temp1=xcoords.get(i);
				int temp2=xcoords.get(i+1);
				xcoords.set(i, temp2 );
				xcoords.set(i+1, temp1);
			}
				
		}
	}
	
	
	
	//deletes a single line in a row
	public void deleteLine(int newX)
	{
		float tolerance =15;
		for (int x: xcoords){
			if (Math.abs(x-newX) <= tolerance){
				Log.d("Found a line", x + "from the ClickPoint:" + newX);
				int index=xcoords.indexOf(x);
				
				
				xcoords.remove(index);
				
				break;
			}
				
			
		}
	}
	
	
	
	
	
	
	// used to create a structure for returning coordinates of final lines to the server
	public JSONArray makeJSONcoords()
	{
		JSONArray ret=new JSONArray();
		JSONArray arr=new JSONArray();
		
		try{
			for (int x: xcoords)
			{
				arr.put(x);
			}
			ret.put(top);
			ret.put(arr);
			
			
		}
		catch(Exception e) {
			 Log.d("Error", "Failing to turn Row into JSON");
			e.printStackTrace();
	       
	    }
		return ret;
	}
	
	
	
	
	public void setTop(int newTop)
	{
		if (newTop > bottom)
			top=newTop;
	}
	public int getTop()
	{
		return top;
	}
	public void setBot(int newBot)
	{
		bottom=newBot;
	}
	public int getBottom()
	{
		return bottom;
	}
	public ArrayList<Integer> getDivs()
	{
		return xcoords;
	}
}
