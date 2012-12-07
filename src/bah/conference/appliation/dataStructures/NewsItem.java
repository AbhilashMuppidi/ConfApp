package bah.conference.appliation.dataStructures;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class NewsItem implements Parcelable {
	public int pk_id = -1;
	public String title = ""; 
	public String link = "";
	public String description = "";
	public String guid = "";
	public String author = "";
	public String pubDate = "";
	public NewsItem(String title, String link, String description,String guid,String author, String pubDate){
		this.title = title; 
		this.link = link;
		this.description = description;
		this.guid = guid;
		this.author = author;
		this.pubDate = pubDate;
	}
	public NewsItem(Cursor cursor){
		this.pk_id = cursor.getInt(cursor.getColumnIndex("pk_id"));
		this.title = cursor.getString(cursor.getColumnIndex("Title"));
		this.link = cursor.getString(cursor.getColumnIndex("Link"));
		this.description = cursor.getString(cursor.getColumnIndex("Description"));
		this.guid = cursor.getString(cursor.getColumnIndex("Guid"));
		this.author = cursor.getString(cursor.getColumnIndex("Author"));
		this.pubDate = cursor.getString(cursor.getColumnIndex("PubDate"));
	}
	public HashMap<String,Object> hashed(){
		HashMap<String,Object> hashed = new HashMap<String,Object>();
		hashed.put("Title", title);
		hashed.put("Link", link);
		hashed.put("Description", description);
		hashed.put("Guid", guid);
		hashed.put("Author", author);
		hashed.put("PubDate", pubDate);
		return hashed;
	}
	public void setWithJSON(JSONObject news){
		try {
			this.title = news.getString("Title");
			this.link = news.getString("Link");
			this.description = news.getString("Description");
			this.guid = news.getString("Guid");
			this.author = news.getString("Author");
			this.pubDate = news.getString("PubDate");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	NewsItem(Parcel source){
		this.pk_id = source.readInt();
		this.title = source.readString();
		this.link = source.readString();
		this.description = source.readString();
		this.guid = source.readString();
		this.author = source.readString();
		this.pubDate = source.readString();
	}
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.pk_id);
		dest.writeString(this.title);
		dest.writeString(this.link);
		dest.writeString(this.description);
		dest.writeString(this.guid);
		dest.writeString(this.author);
		dest.writeString(this.pubDate);
	}
	public static final Parcelable.Creator<NewsItem> CREATOR = new Parcelable.Creator<NewsItem>(){
		public NewsItem createFromParcel(Parcel source) {
			return new NewsItem(source);
		}
		public NewsItem[] newArray(int size) {
			return new NewsItem[size];
		}
	};
}
