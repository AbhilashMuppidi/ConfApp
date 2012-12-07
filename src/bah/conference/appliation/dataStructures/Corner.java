package bah.conference.appliation.dataStructures;

import android.database.Cursor;

public class Corner{
	public int x=-1;
	public int y=-1;
	Corner(int x, int y){
		this.x = x; this.y=y;
	}
	public Corner(Cursor cursor){
		this.x = cursor.getInt(cursor.getColumnIndex("x"));
		this.y = cursor.getInt(cursor.getColumnIndex("y"));
	}
}
