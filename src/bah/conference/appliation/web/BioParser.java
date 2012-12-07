package bah.conference.appliation.web;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bah.conference.appliation.dataStructures.BioItem;
import bah.conference.appliation.dataStructures.ScheduleItem;

public class BioParser {
	static ArrayList<BioItem> getItems(String subjectString) throws JSONException{
		ArrayList<BioItem> bioItems = new ArrayList<BioItem>();
		JSONObject obj = new JSONObject(subjectString);
		if(obj.has("speaker")){
			JSONArray speakers = obj.getJSONArray("speaker");
			for(int i = 0; i < speakers.length(); i++){
				JSONObject bioItemJson = speakers.getJSONObject(i);
				BioItem bioItem =  new BioItem();
				bioItem.setWithJSON(bioItemJson);
				bioItems.add(bioItem);
			}
		}
		return bioItems;
	}
}
