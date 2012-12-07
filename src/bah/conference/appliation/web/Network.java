package bah.conference.appliation.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.MenuItems.News;
import bah.conference.appliation.dataStructures.BioItem;
import bah.conference.appliation.dataStructures.BoothLocation;
import bah.conference.appliation.dataStructures.Exhibitor;
import bah.conference.appliation.dataStructures.NewsItem;
import bah.conference.appliation.dataStructures.ScheduleItem;
import bah.conference.appliation.dataStructures.ScheduleItem2;

public class Network {

	static public void grabFeed(ConfApp confApp,Activity activity){
		String url = "http://www.geoint2012.com/news.rss";
		
		String result = "";
		InputStream is = null;
		try {
		    // Create a URL for the desired page
		    URL urla = new URL(url);

		    // Read all the text returned by the server
		    BufferedReader in = new BufferedReader(new InputStreamReader(urla.openStream()));
		    String str;
		    while ((str = in.readLine()) != null) {
		        result += str;
		    }
		    in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
			//Toast.makeText(activity, "Cannot connect to the internet!", Toast.LENGTH_LONG).show();
		}
		
        if(!result.equals("")){
        	ArrayList<NewsItem> news = RssParser.getItems(result);
        	ConfApp.confApp.local.startTransaction();
        	if(news.size()>0){
        		confApp.local.wipeTable("News");
        		for(NewsItem newsItem : news)
        		confApp.local.insertRow("News", newsItem.hashed());
        	}
        	ConfApp.confApp.local.endTransaction();
        }
	}
	static public void getSchedule2(ConfApp confApp,Activity activity){
		ArrayList<ScheduleItem2> items = confApp.local.getSchedule2TimeSlots();
		String url = "http://geoint2012.com/agenda.ical";
		
		//String result = "";
		ArrayList<String> result = new ArrayList<String>();
		InputStream is = null;
		try {
		    // Create a URL for the desired page
		    URL urla = new URL(url);

		    // Read all the text returned by the server
		    BufferedReader in = new BufferedReader(new InputStreamReader(urla.openStream()));
		    String str;
		    while ((str = in.readLine()) != null) {
		        result.add(str);
		    }
		    in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
			//Toast.makeText(activity, "Cannot connect to the internet!", Toast.LENGTH_LONG).show();
		}
        if(!result.equals("")){
        	ArrayList<ScheduleItem2> scheduleItems = ScheduleParser2.getItems(result);
        	if(scheduleItems.size()>0){
        		confApp.local.wipeTable("ScheduleItem2");
        		ConfApp.confApp.local.startTransaction();
        		for(ScheduleItem2 scheduleItem2 : scheduleItems){
        			confApp.local.insertRow("ScheduleItem2", scheduleItem2.hashed());
        		}
        		confApp.local.endTransaction();
        		confApp.doWork();
        	}
        }
	}
	
	static public void grabBios(ConfApp confApp){
		try {
			java.io.InputStream is = confApp.getAssets().open("speakers.json");
			String result = new java.util.Scanner(is).useDelimiter("\\A").next();
			ArrayList<BioItem> bioItems = new ArrayList<BioItem>();
			bioItems = BioParser.getItems(result);
			confApp.local.wipeTable("Bio");
			ConfApp.confApp.local.startTransaction();
			for(BioItem bioItem : bioItems)
				confApp.local.insertRow("Bio", bioItem.hashed());
			ConfApp.confApp.local.endTransaction();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	static public void grabBoothLocations(ConfApp confApp){
		try {
			java.io.InputStream is = confApp.getAssets().open("booths.json");
			String result = new java.util.Scanner(is).useDelimiter("\\A").next();
			ArrayList<BoothLocation> boothLocations = new ArrayList<BoothLocation>();
			boothLocations = BoothParser.getItems(result);
			confApp.local.wipeTable("BoothLocation");
			ConfApp.confApp.local.startTransaction();
			for(BoothLocation boothLocation : boothLocations)
				confApp.local.insertBooth(boothLocation);
			ConfApp.confApp.local.endTransaction();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	static public void getExhibitors(ConfApp confApp){
		ArrayList<String> result = new ArrayList<String>();
		try {
		    java.io.InputStream is = confApp.getAssets().open("exhibitors.csv");
		    BufferedReader in = new BufferedReader(new InputStreamReader(is));
		    String str;
		    while ((str = in.readLine()) != null) {
		        result.add(str);
		    }
		    in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
        if(!result.equals("")){
        	ArrayList<Exhibitor> exhibitors = ExhibitorsParser.getItems(result);
        	if(exhibitors.size()>0){
        		confApp.local.wipeTable("Exhibitor");
        		ConfApp.confApp.local.startTransaction();
        		for(Exhibitor exhibitor : exhibitors){
        			long worked = confApp.local.insertRow("Exhibitor", exhibitor.hashed());
        			if(worked > 0)
        				System.out.println("Worked : "+worked);
        			else
        				System.out.println("BLARG : ");
        			System.out.print("blan");
        		}
        		ConfApp.confApp.local.endTransaction();
        	}
        }
	}
}
