package bah.conference.appliation.dataStructures;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class ScheduleItem {	
	/*public int pk_id;
	public int day;
	public String date;
	public String startTime;
	public String endTime;
	public String title;
	public String description;
	public String speaker;
	public String url;
	public String type;
	public String location;
	public boolean active;
	public boolean favorited;
	public String notes;
	
	public ScheduleItem(Cursor cursor){
		this.pk_id = cursor.getInt(cursor.getColumnIndex("pk_id"));
		this.day = cursor.getInt(cursor.getColumnIndex("Day"));
		this.date = cursor.getString(cursor.getColumnIndex("Date"));
		this.startTime = cursor.getString(cursor.getColumnIndex("StartTime"));
		this.endTime = cursor.getString(cursor.getColumnIndex("EndTime"));
		this.title = cursor.getString(cursor.getColumnIndex("Title"));
		this.description = cursor.getString(cursor.getColumnIndex("Description"));
		this.speaker = cursor.getString(cursor.getColumnIndex("Speaker"));
		this.url = cursor.getString(cursor.getColumnIndex("AvatarURL"));
		this.type = cursor.getString(cursor.getColumnIndex("Event_Type"));
		this.location = cursor.getString(cursor.getColumnIndex("Location"));
		this.active = true;
		this.favorited = cursor.getInt(cursor.getColumnIndex("Favorited"))>0;
		this.notes = cursor.getString(cursor.getColumnIndex("Notes"));
	}
	public ScheduleItem(String date, String day, String time, String endTime, String title, String description, String speaker, String avatarURL, String event_type, String location){
		this.day=Integer.parseInt(day);
		this.date=date;
		this.startTime=time;
		this.endTime=endTime;
		this.title=title;
		this.description=description;
		this.speaker=speaker;
		this.url=avatarURL;
		this.type=event_type;
		this.location=location;
		this.notes="";
	}
	public HashMap<String,Object> hashed(){
		HashMap<String,Object> hashed = new HashMap<String,Object>();
		//hashed.put("Day",day);
		hashed.put("Day",day);
		hashed.put("Date", date);
		hashed.put("StartTime", startTime);
		hashed.put("EndTime", endTime);
		hashed.put("Title", title);
		hashed.put("Description", description);
		hashed.put("Speaker", speaker);
		hashed.put("AvatarURL", url);
		hashed.put("Event_Type", type);
		hashed.put("Location", location);
		hashed.put("Favorited", favorited? 1:0);
		hashed.put("Notes", notes);
		return hashed;
	}
	public void setWithJSON(JSONObject scheduleItem){
		try {
			this.day= scheduleItem.getInt("Day");
			this.date = scheduleItem.getString("Date");
			this.startTime = scheduleItem.getString("time");
			this.endTime = scheduleItem.getString("endtime");
			this.title = scheduleItem.getString("title");
			this.description = scheduleItem.getString("description");
			this.speaker = scheduleItem.getString("Speaker");
			this.url = scheduleItem.getString("AvatarURL");
			this.type = scheduleItem.getString("event_type");
			this.location = scheduleItem.getString("location");
			this.active=true;
			this.active=false;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	ScheduleItem(Parcel source){
		this.pk_id = source.readInt();
		this.day  = source.readInt();
		this.date = source.readString();
		this.startTime = source.readString();
		this.endTime = source.readString();
		this.title = source.readString();
		this.description = source.readString();
		this.speaker = source.readString();
		this.url = source.readString();
		this.type = source.readString();
		this.location = source.readString();
		this.active = source.readInt()>0;
		this.favorited = source.readInt()>0;
		this.notes = source.readString();
	}
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		//dest.writeInt(this.day);
		dest.writeInt(this.pk_id);
		dest.writeInt(this.day);
		dest.writeString(this.date);
		dest.writeString(this.startTime);
		dest.writeString(this.endTime);
		dest.writeString(this.title);
		dest.writeString(this.description);
		dest.writeString(this.speaker);
		dest.writeString(this.url);
		dest.writeString(this.type);
		dest.writeString(this.location);
		dest.writeByte((byte)(this.active ? 1:0));
		dest.writeByte((byte)(this.favorited ? 1:0));
		dest.writeString(this.notes);
	}
	public static final Parcelable.Creator<ScheduleItem> CREATOR = new Parcelable.Creator<ScheduleItem>(){
		public ScheduleItem createFromParcel(Parcel source) {
			return new ScheduleItem(source);
		}
		public ScheduleItem[] newArray(int size) {
			return new ScheduleItem[size];
		}
	};*/
}