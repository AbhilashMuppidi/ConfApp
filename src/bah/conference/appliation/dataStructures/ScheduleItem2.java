package bah.conference.appliation.dataStructures;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class ScheduleItem2 {
	public int pk_id = -1;
	public String begin = "";
	public String sequence = "";
	public String dtend = "";
	public String uid = "";
	public String description = "";
	public String summary = "";
	public String dtstart = "";
	public String dtstamp = "";
	public String location = "";
	public String end = "";
	public boolean active = true;
	public boolean favorited = false;
	public String notes = "";
	public String speaker = "";
	
	public ScheduleItem2(Cursor cursor){
		this.pk_id = cursor.getInt(cursor.getColumnIndex("pk_id"));
		this.begin = cursor.getString(cursor.getColumnIndex("Begin"));
		this.sequence = cursor.getString(cursor.getColumnIndex("Sequence"));
		this.dtend = cursor.getString(cursor.getColumnIndex("DTEnd"));
		this.uid = cursor.getString(cursor.getColumnIndex("UID"));
		this.description = cursor.getString(cursor.getColumnIndex("Description"));
		this.summary = cursor.getString(cursor.getColumnIndex("Summary"));
		this.dtstart = cursor.getString(cursor.getColumnIndex("DTStart"));
		this.dtstamp = cursor.getString(cursor.getColumnIndex("DTStamp"));
		this.location = cursor.getString(cursor.getColumnIndex("Location"));
		this.end = cursor.getString(cursor.getColumnIndex("End"));
		this.active = cursor.getInt(cursor.getColumnIndex("Active"))>0;
		this.favorited = cursor.getInt(cursor.getColumnIndex("Favorited"))>0;
		this.notes = cursor.getString(cursor.getColumnIndex("Notes"));
		this.speaker = cursor.getString(cursor.getColumnIndex("Speaker"));
		clean();
	}
	public void clean(){
		if(summary!=null){
			if(summary.length()>0){
				summary = summary.replaceAll("\\\\,", ",");
				summary = summary.replaceAll("\\\\n","\n");
				summary = summary.replaceAll("\\\\;", ";");
			}
		}
		if(location!=null){
			if(location.length()>0){
				location = location.replaceAll("\\\\,", ",");
				location = location.replaceAll("\\\\n","\n");
				location = location.replaceAll("\\\\;", ";");	
			}
		}
		if(description!=null){
			if(description.length()>0){
				description = description.replaceAll("\\\\,", ",");
				description = description.replaceAll("\\\\n","\n");
				description = description.replaceAll("\\\\;", ";");
			}
		}
		if(speaker!=null){
			if(speaker.length()>0){
				speaker = speaker.replaceAll("\\\\,", ",");
				speaker = speaker.replaceAll("\\\\n","\n");
				speaker = speaker.replaceAll("\\\\;", ";");	
			}
		}
	}
	public ScheduleItem2(String begin, String sequence,String dtend, String uid, String description, String summary, String dtstart, String dtstamp, String location, String end, String speaker){
		this.begin = begin;
		this.sequence = sequence;
		this.dtend = dtend;
		this.uid = uid;
		this.description = description;
		this.summary = summary;
		this.dtstart = dtstart;
		this.dtstamp = dtstamp;
		this.location = location;
		this.end = end;
		this.notes="";
		this.speaker = "";
		clean();
	}
	public String getStartTimeFormatted(){
		return formatTime(this.dtstart);
	}
	public String getEndTimeFormatted(){
		return formatTime(this.dtend);
	}
	public String formatTime(String time){
		String fomattedString = "";
		if(time.length() > 0 ){
			String month="";
			String day="";
			String year="";
			String hour="";
			String minute="";
			String second="";
			Pattern endRegex = Pattern.compile("^([0-9]{4})-([0-1][0-9])-([0-3][0-9]) T ([0-2][0-9]):([0-6][0-9]):([0-6][0-9])Z$");
			Matcher matcher = endRegex.matcher(time);
			if(matcher.find()){
				year = matcher.group(1);
			    month = matcher.group(2);
			    day = matcher.group(3);
			    hour = matcher.group(4);
			    minute = matcher.group(5);
			    second = matcher.group(6);
			    
			    int h = Integer.parseInt(hour);
			    int m = Integer.parseInt(minute);
			    
			    h = h-4;
			    String a = " am";
			    if(h > 11)
			    	a = " pm";
			    if(h>12)
			    	h = h-12;
			    
			    
			    return h+":"+minute + a;
			}
		}
		return "";
	}
	public String formatYearMonthDay(String time){
		String fomattedString = "";
		if(time.length() > 0 ){
			String month="";
			String day="";
			String year="";
			String hour="";
			String minute="";
			String second="";
			Pattern endRegex = Pattern.compile("^([0-9]{4})-([0-1][0-9])-([0-3][0-9]) T ([0-2][0-9]):([0-6][0-9]):([0-6][0-9])Z$");
			Matcher matcher = endRegex.matcher(time);
			if(matcher.find()){
				year = matcher.group(1);
			    month = matcher.group(2);
			    day = matcher.group(3);
			    hour = matcher.group(4);
			    minute = matcher.group(5);
			    second = matcher.group(6);
			    
			    int h = Integer.parseInt(hour);
			    int m = Integer.parseInt(minute);
			    
			    h = h-4;
			    String a = " am";
			    if(h > 11)
			    	a = " pm";
			    if(h>12)
			    	h = h-12;
			    
			    
			    return month+"/"+day+"/"+year;
			}
		}
		return "";
	}
	public HashMap<String,Object> hashed(){
		HashMap<String,Object> hashed = new HashMap<String,Object>();
		//hashed.put("pk_id",pk_id);
		hashed.put("Begin",begin);
		hashed.put("Sequence",sequence);
		hashed.put("DTEnd",dtend);
		hashed.put("UID",uid);
		hashed.put("Description",description);
		hashed.put("Summary",summary);
		hashed.put("DTStart",dtstart);
		hashed.put("DTStamp",dtstamp);
		hashed.put("Location",location);
		hashed.put("End",end);
		hashed.put("Favorited", favorited ? 1:0);
		hashed.put("Active", active ? 1:0);
		hashed.put("Notes", notes);
		hashed.put("Speaker", speaker);
		
		return hashed;
	}
	ScheduleItem2(Parcel source){
		this.pk_id = source.readInt();
		this.begin  = source.readString();
		this.dtend = source.readString();
		this.uid = source.readString();
		this.description = source.readString();
		this.summary = source.readString();
		this.dtstart = source.readString();
		this.dtstamp = source.readString();
		this.location = source.readString();
		this.end = source.readString();
		this.active = source.readInt()>0;
		this.favorited = source.readInt()>0;
		this.notes = source.readString();
		this.speaker = source.readString();
	}
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		//dest.writeInt(this.day);
		dest.writeInt(this.pk_id);
		dest.writeString(this.begin);
		dest.writeString(this.dtend);
		dest.writeString(this.uid);
		dest.writeString(this.description);
		dest.writeString(this.summary);
		dest.writeString(this.dtstart);
		dest.writeString(this.dtstamp);
		dest.writeString(this.location);
		dest.writeString(this.end );
		dest.writeByte((byte)(this.active ? 1:0));
		dest.writeByte((byte)(this.favorited ? 1:0));
		dest.writeString(this.notes);
		dest.writeString(this.speaker);
	}
	public static final Parcelable.Creator<ScheduleItem2> CREATOR = new Parcelable.Creator<ScheduleItem2>(){
		public ScheduleItem2 createFromParcel(Parcel source) {
			return new ScheduleItem2(source);
		}
		public ScheduleItem2[] newArray(int size) {
			return new ScheduleItem2[size];
		}
	};
	
	public String toString_notes_format(){
		return
			"##############################################################\n"+
			"#  Schedule Title: "+this.summary+"\n"+
			"#  Schedule Time: "+this.formatTime(this.dtstart)+"-"+this.formatTime(this.dtend)+"\n"+
			"##############################################################\n"+
								 notes+"\n"+
			"##############################################################\n";
	}
	public String toString_favorites_format(){
		return
			"##############################################################\n"+
			"#  Schedule Title: "+this.summary+"\n"+
			"#  Schedule Time: "+this.formatTime(this.dtstart)+"-"+this.formatTime(this.dtend)+"\n"+
			"##############################################################\n";
	}
}
