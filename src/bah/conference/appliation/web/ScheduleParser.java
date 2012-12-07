package bah.conference.appliation.web;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bah.conference.appliation.dataStructures.ScheduleItem;

public class ScheduleParser {
	
	static ArrayList<ScheduleItem> getItems(String subjectString) throws JSONException{
		ArrayList<ScheduleItem> scheduleItems = new ArrayList<ScheduleItem>();
		/*JSONObject obj = new JSONObject(subjectString);
		if(obj.has("schedule")){
			JSONArray schedules = obj.getJSONArray("schedule");
			for(int i = 0; i < schedules.length(); i++){
				JSONObject scheduleItem = schedules.getJSONObject(i);
				String date = scheduleItem.getString("date");
				String day = scheduleItem.getString("day");
				JSONArray timeSlots = scheduleItem.getJSONArray("timeslots");
				for(int j = 0; j < timeSlots.length(); j++){
					JSONObject timeSlot = timeSlots.getJSONObject(j);
					String time = timeSlot.getString("time");
					String endTime = timeSlot.getString("endtime");
					String title = timeSlot.getString("title");
					String description = timeSlot.getString("description");
					String speaker = timeSlot.getString("Speaker");
					String avatarUrl = timeSlot.getString("AvatarURL");
					String event_type = timeSlot.getString("event_type");
					String location = timeSlot.getString("location");
					ScheduleItem newScheduleItem = new Schedule2Item(date,day,time,endTime,title,description,speaker,avatarUrl,event_type,location);
					scheduleItems.add(newScheduleItem);
				}
			}
		}*/
		return scheduleItems;
	}
}
