package bah.conference.appliation.dataStructures;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;

public class Exhibitor {
	int pk_id=-1;
	public String company;
	public String boothNumber;
	ArrayList<String> categories = new ArrayList<String>();
	public Exhibitor(String in){
		String[] arr = in.split(",");
		if(arr.length > 1){
			for(int i = 0; i < arr.length; i++){
				if(i == 0)
					company = arr[i];
				if(i == 1)
					boothNumber = arr[i];
				else
					categories.add(arr[i]);
			}
		}
	}
	public ContentValues getContentValues(){
		ContentValues contentValues = new ContentValues();
		contentValues.put("company", this.company);
		contentValues.put("boothname", this.boothNumber);
		return contentValues;
	}
	public Exhibitor(Cursor cursor){
		this.pk_id = cursor.getInt(cursor.getColumnIndex("pk_id"));
		this.company = cursor.getString(cursor.getColumnIndex("company"));
		this.boothNumber = cursor.getString(cursor.getColumnIndex("boothname"));
	}
	public HashMap<String,Object> hashed(){
		HashMap<String,Object> hashed = new HashMap<String,Object>();
		//hashed.put("pk_id",pk_id);
		hashed.put("company",this.company);
		hashed.put("boothname",this.boothNumber);
	
		
		return hashed;
	}
}
