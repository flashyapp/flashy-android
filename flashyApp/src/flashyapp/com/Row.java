package flashyapp.com;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.Log;

public class Row {

	private int top;
	private int bottom;
	private ArrayList<Integer> xcoords;
	private int maxWidth;
	private int maxHeight;
	
	public Row(int t, int b, ArrayList<Integer> x, int width, int height)
	{
		top=t;
		bottom=b;
		xcoords=new ArrayList<Integer>();
		//int index;
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
	public Path addToLinePath(Path path)
	{
		
		int minwidth=0;
		
		path.moveTo(minwidth, top);
		path.lineTo(maxWidth, top);
		/*path.moveTo(minwidth, bottom);
		path.lineTo(maxwidth, bottom-5);
		*/
		for (int x: xcoords){
			
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
		for (Integer x: xcoords)
			str=str+" "+x.toString();
		Log.d("ROW toString X coords",str);
			
	}
	
	
	public void moveVerticalLineHorizontally(int newX)
	{
		boolean moved=false;
		float tolerance =15;
		for (int x: xcoords){
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
}
