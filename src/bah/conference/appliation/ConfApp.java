package bah.conference.appliation;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

import android.app.Activity;
import android.app.Application;
import bah.conference.appliation.dataStructures.ScheduleItem2;
import bah.conference.appliation.database.Local;
import bah.conference.appliation.web.Network;

public class ConfApp extends Application{
	public Local local;
	
	public static ConfApp confApp;

		
	public HashMap<Integer,ArrayList<ScheduleItem2>> dayArrays = new HashMap<Integer,ArrayList<ScheduleItem2>>();
	public ArrayList<ScheduleItem2> previous = new ArrayList<ScheduleItem2>();
	public ArrayList<ScheduleItem2> current = new ArrayList<ScheduleItem2>();
	public void populateSchedules(){
		dayArrays.put(1,local.getSchedule2TimeSlots(1));
		dayArrays.put(2,local.getSchedule2TimeSlots(2));
		dayArrays.put(3,local.getSchedule2TimeSlots(3));
		dayArrays.put(4,local.getSchedule2TimeSlots(4));
	}
	public void updateScheduleStatus(){
		String currentTimeStamp = getCurrentTimeStamp();
		previous = local.getSchedule2Previous(currentTimeStamp);
		current = local.getSchedule2Now(currentTimeStamp);
	}
	public void doWork(){
		populateSchedules();
		updateScheduleStatus();
	}
	public int todaysDay(){
		return -1;
	}
	
	@Override
	public void onCreate() {

		super.onCreate();
	}
	@Override
	public void onTerminate() {
		if(local!=null)
			local.close();
		super.onTerminate();
	}
	public void initialize(Activity activity){
		confApp = this;
		local = new Local(this);
		populateSchedule(activity);
		this.populateNews(activity);
		doWork();
		//ArrayList<BioItem> bioItems = local.getBioItems();
	}
	public void updateSchedule(Activity activity){
		Network.getSchedule2(this,activity);
	}
	public void updateNews(Activity activity){
		Network.grabFeed(this,activity);
	}
	public void updateBoothLocations(){
		Network.grabBoothLocations(this);
	}
	public String getCurrentTimeStamp(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' T 'HH:mm:ss'Z'");;
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    final String utcTime = sdf.format(new Date(System.currentTimeMillis()));
		//String currentDateandTime = sdf.format(new Date(System.currentTimeMillis()));
		//String currentDateandTime = "2012-10-10 T 18:10:00Z";
		return utcTime;
	}
	
	public void populateSchedule(Activity activity){
		if(!local.isPopulated("ScheduleItem2"))
			Network.getSchedule2(this,activity);
	}
	public void populateNews(Activity activity){
		if(!local.isPopulated("News"))
	        Network.grabFeed(this,activity);
	}
	public void populateBio(){
		if(!local.isPopulated("Bio"))
			Network.grabBios(this);
	}
	public void populateExhibitor(){
		if(!local.isPopulated("Exhibitor"))
			Network.getExhibitors(this);
	}
	public void populateBooths(){
		if((!local.isPopulated("BoothLocation"))||(!local.isPopulated("Booth")))
			updateBoothLocations();
	}


}
