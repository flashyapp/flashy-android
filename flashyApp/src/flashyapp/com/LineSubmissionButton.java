package flashyapp.com;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Button;


//A button that stores the array of lines
public class LineSubmissionButton extends Button{
	private ArrayList<Row> rows;
	
	
	public LineSubmissionButton(Context context, ArrayList<Row> inRows)
	{
		super(context);
		rows=inRows;
	}
	
	
	public ArrayList<Row> returnArray()
	{
		return rows;
	}
}
