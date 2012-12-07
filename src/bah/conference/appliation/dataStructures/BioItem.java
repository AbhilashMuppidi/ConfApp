package bah.conference.appliation.dataStructures;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class BioItem implements Parcelable {
	public int pk_id = -1;
	public String title = ""; 
	public String name = "";
	public String bio = "";
	public String image = "";
	public BioItem(){
		
	}
	public BioItem(String title, String name, String bio,String image){
		this.title = title; 
		this.name = name;
		this.bio = bio;
		this.image = image;
	}
	public BioItem(Cursor cursor){
		this.pk_id = cursor.getInt(cursor.getColumnIndex("pk_id"));
		this.name = cursor.getString(cursor.getColumnIndex("Name"));
		this.title = cursor.getString(cursor.getColumnIndex("Title"));
		this.bio = cursor.getString(cursor.getColumnIndex("Bio"));
		this.image = cursor.getString(cursor.getColumnIndex("Image"));
	}
	public HashMap<String,Object> hashed(){
		HashMap<String,Object> hashed = new HashMap<String,Object>();
		hashed.put("Title", title);
		hashed.put("Name", name);
		hashed.put("Bio", bio);
		hashed.put("Image", image);
		return hashed;
	}
	public void setWithJSON(JSONObject bioItem){
		try {
			name = bioItem.getString("name");
			title = bioItem.getString("title");
			bio = bioItem.getString("bio");
			image = bioItem.getString("image");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	BioItem(Parcel source){
		this.pk_id = source.readInt();
		this.title = source.readString();
		this.name = source.readString();
		this.bio = source.readString();
		this.image = source.readString();
	}
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.pk_id);
		dest.writeString(this.title);
		dest.writeString(this.name);
		dest.writeString(this.bio);
		dest.writeString(this.image);
	}
	public static final Parcelable.Creator<BioItem> CREATOR = new Parcelable.Creator<BioItem>(){
		public BioItem createFromParcel(Parcel source) {
			return new BioItem(source);
		}
		public BioItem[] newArray(int size) {
			return new BioItem[size];
		}
	};
}
