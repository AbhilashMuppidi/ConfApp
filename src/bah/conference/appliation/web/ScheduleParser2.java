package bah.conference.appliation.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bah.conference.appliation.dataStructures.ScheduleItem2;

public class ScheduleParser2 {
	static void parseHeader(){
		
	}
	static ArrayList<ScheduleItem2> getItems(ArrayList<String> subjectStrings){
		ArrayList<ScheduleItem2> items = new ArrayList<ScheduleItem2>();
		int begin = -1;
		Pattern beginRegex = Pattern.compile("^BEGIN:(.*?)");
		Pattern endRegex = Pattern.compile("^END:(.*?)");
		Matcher matcher;
		for(int i = 0; i < subjectStrings.size();i++){
			matcher = beginRegex.matcher(subjectStrings.get(i));
			if(matcher.find())
				begin = i;
			matcher = endRegex.matcher(subjectStrings.get(i));
			if(matcher.find() && (begin != -1)){
				ScheduleItem2 a = parseItem(subjectStrings.subList(begin, i+1));
				if((a.location!=null)&&(a.dtstart!=null)&&(a.dtend!=null)){
					if((a.location.length()>0)&&(a.dtstart.length()>0)&&(a.dtend.length()>0))
						items.add(a);
				}
				begin = -1;
			}
		}
		return items;
	}
	static ScheduleItem2 parseItem(List<String> subjectStrings){
		String begin 				= ""; 
		String sequence 			= "";
		String dtend 				= "";
		String uid	 				= "";
		String description 			= "";
		String summary 				= "";
		String dtstart 				= "";
		String dtstamp 				= "";
		String location				= "";
		String end					= "";

		Pattern beginRegex 			= Pattern.compile("^BEGIN:(.*)");
		Pattern sequenceRegex 		= Pattern.compile("^SEQUENCE:(.*)");
		Pattern dtendRegex			= Pattern.compile("^DTEND:(.*)");
		Pattern uidRegex 			= Pattern.compile("^UID:(.*)");
		Pattern descriptionRegex 	= Pattern.compile("^DESCRIPTION:(.*)");
		Pattern summaryRegex 		= Pattern.compile("^SUMMARY:(.*)");
		Pattern dtstartRegex 		= Pattern.compile("^DTSTART:(.*)");
		Pattern dtstampRegex	 	= Pattern.compile("^DTSTAMP:(.*)");
		Pattern locationRegex 		= Pattern.compile("^LOCATION:(.*)");
		Pattern endRegex 			= Pattern.compile("^END:(.*)");
		HashMap<String,String> a = new HashMap<String,String>();
		String currentIndex = "";
		for(int i = 0; i < subjectStrings.size();i++){
			String item = subjectStrings.get(i);
			Matcher beginM = beginRegex.matcher(item);
			Matcher sequenceM = sequenceRegex.matcher(item);
			Matcher dtendM = dtendRegex.matcher(item);
			Matcher uidM = uidRegex.matcher(item);
			Matcher descriptM = descriptionRegex.matcher(item);
			Matcher summaryM = summaryRegex.matcher(item);
			Matcher dStartM = dtstartRegex.matcher(item);
			Matcher dStampM = dtstampRegex.matcher(item);
			Matcher locM = locationRegex.matcher(item);
			Matcher endM = endRegex.matcher(item);

			if(beginM.find()){
				currentIndex = "begin";
				String output = "";
				if(a.containsKey(currentIndex)){
					output += a.get(currentIndex);
				}
			    output += beginM.group(1);
			    a.put(currentIndex,output);
			}else if(sequenceM.find()){
				currentIndex = "sequence";
				String output = "";
				if(a.containsKey(currentIndex)){
					output += a.get(currentIndex);
				}
				output += sequenceM.group(1);
				a.put(currentIndex,output);
			}else if(dtendM.find()){
				currentIndex = "dtend";
				String output = "";
				if(a.containsKey(currentIndex)){
					output += a.get(currentIndex);
				}
				output += icalDateTimeToSQLiteDateTime(dtendM.group(1));
				a.put(currentIndex,output);
			}else if(uidM.find()){
				currentIndex = "uid";
				String output = "";
				if(a.containsKey(currentIndex)){
					output += a.get(currentIndex);
				}
				output += uidM.group(1);
				a.put(currentIndex,output);
			}else if(descriptM.find()){
				currentIndex = "description";
				String output = "";
				if(a.containsKey(currentIndex)){
					output += a.get(currentIndex);
				}
				output += descriptM.group(1);
				a.put(currentIndex,output);
			}else if(summaryM.find()){
				currentIndex = "summary";
				String output = "";
				if(a.containsKey(currentIndex)){
					output += a.get(currentIndex);
				}
				output += summaryM.group(1);
				a.put(currentIndex,output);
			}else if(dStartM.find()){
				currentIndex = "dStart";
				String output = "";
				if(a.containsKey(currentIndex)){
					output += a.get(currentIndex);
				}
				output += icalDateTimeToSQLiteDateTime(dStartM.group(1));
				a.put(currentIndex,output);
			}else if(dStampM.find()){
				currentIndex = "dStampM";
				String output = "";
				if(a.containsKey(currentIndex)){
					output += a.get(currentIndex);
				}
				output += icalDateTimeToSQLiteDateTime(dStampM.group(1));
				a.put(currentIndex,output);
			}else if(locM.find()){
				currentIndex = "location";
				String output = "";
				if(a.containsKey(currentIndex)){
					output += a.get(currentIndex);
				}
				output += locM.group(1);
			    a.put(currentIndex,output);
			}else if(endM.find()){
				currentIndex = "end";
				String output = "";
				if(a.containsKey(currentIndex)){
					output += a.get(currentIndex);
				}
				output += endM.group(1);
			    a.put(currentIndex,output);
			}else{
				if(currentIndex.length()>0){
					String output = "";
					if(a.containsKey(currentIndex)){
						output += a.get(currentIndex);
					}
					output += item;
				    a.put(currentIndex,output);
				}
			}
				
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
		}
		if(a.containsKey("end"))
			end = a.get("end");
		if(a.containsKey("location"))
			location = a.get("location");
		if(a.containsKey("dStampM"))
			dtstamp = a.get("dStampM");
		if(a.containsKey("dStart"))
			dtstart = a.get("dStart");
		if(a.containsKey("summary"))
			summary = a.get("summary");
		if(a.containsKey("description"))
			description = a.get("description");
		if(a.containsKey("uid"))
			uid = a.get("uid");
		if(a.containsKey("dtend"))
			dtend = a.get("dtend");
		if(a.containsKey("sequence"))
			sequence = a.get("sequence");
		if(a.containsKey("begin"))
			begin = a.get("begin");
		ScheduleItem2 schedule = new ScheduleItem2(begin,sequence,dtend,uid,description,summary,dtstart,dtstamp,location,end,"");
		return schedule;
	}
	//20121011T200000Z
	//2012-10-11 T 20:00:00Z'
	
	
	//20121011T183000Z
	//201-1-1 T 1T:8:0Z
	private static String icalDateTimeToSQLiteDateTime(String icalDateTime){
		String output = "";

		output += icalDateTime.substring(0, 4)+"-";
		output += icalDateTime.substring(4, 6)+"-";
		output += icalDateTime.substring(6, 8)+" ";
		output += icalDateTime.substring(8, 9)+" ";
		output += icalDateTime.substring(9, 11)+":";
		output += icalDateTime.substring(11, 13)+":";
		output += icalDateTime.substring(13, 15)+"Z";

		return output;
	}
	
}
