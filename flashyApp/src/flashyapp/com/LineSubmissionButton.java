package flashyapp.com;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Button;

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
