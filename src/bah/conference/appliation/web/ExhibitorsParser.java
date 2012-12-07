package bah.conference.appliation.web;

import java.util.ArrayList;

import android.util.Log;
import bah.conference.appliation.dataStructures.Exhibitor;

public class ExhibitorsParser {
	static ArrayList<Exhibitor> getItems(ArrayList<String> subjectStrings){
		ArrayList<Exhibitor> items = new ArrayList<Exhibitor>();
		for(String str : subjectStrings){
			Exhibitor a = new Exhibitor(str);
			if(a.boothNumber!=null && a.company!=null){
				items.add(a);
			}else{
				Log.wtf("ZOMG", "POPTART");
			}
		}
		return items;
	}
}
