package bah.conference.appliation.web;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bah.conference.appliation.dataStructures.BioItem;
import bah.conference.appliation.dataStructures.BoothLocation;

public class BoothParser {
	static ArrayList<BoothLocation> getItems(String subjectString) throws JSONException{
		ArrayList<BoothLocation> boothLocations = new ArrayList<BoothLocation>();
		JSONObject obj = new JSONObject(subjectString);
		if(obj.has("data")){
			JSONArray locations = obj.getJSONArray("data");
			for(int i = 0; i < locations.length(); i++){
				JSONObject boothItemJson = locations.getJSONObject(i);
				BoothLocation boothLocation =  new BoothLocation();
				boothLocation.fromJson(boothItemJson);
				boothLocations.add(boothLocation);
			}
		}
		return boothLocations;
	}
}
