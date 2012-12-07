package bah.conference.appliation.web;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bah.conference.appliation.dataStructures.NewsItem;

public class RssParser {
	static void parseHeader(){
		
	}
	static ArrayList<NewsItem> getItems(String subjectString){
		ArrayList<NewsItem> news = new ArrayList<NewsItem>();

		Pattern regex = Pattern.compile("<item>(.*?)</item>");
		Matcher matcher = regex.matcher(subjectString);
		while(matcher.find()){
		    news.add(parseItem(matcher.group(1)));
		}
		return news;
	}
	static NewsItem parseItem(String item){
		String title = ""; 
		String link = "";
		String description = "";
		String guid = "";
		String author = "";
		String pubDate = "";
		Pattern titleRegex = Pattern.compile("<[Tt]itle>(.*?)</[Tt]itle>");
		Pattern linkRegex = Pattern.compile("<[Ll]ink>(.*?)</[Ll]ink>");
		Pattern descriptionRegex = Pattern.compile("<[Dd]escription>(.*?)</[Dd]escription>");
		Pattern guidRegex = Pattern.compile("<[Gg]uid>(.*?)</[Gg]uid>");
		Pattern authorRegex = Pattern.compile("<[aA]uthor>(.*?)</[aA]uthor>");
		Pattern pubDateRegex = Pattern.compile("<[pP]ub[Dd]ate>(.*?)</[pP]ub[Dd]ate>");
		
		Matcher matcher = titleRegex.matcher(item);
		if(matcher.find())
		    title = matcher.group(1);
		matcher = linkRegex.matcher(item);
		if(matcher.find())
		    link = matcher.group(1);
		matcher = descriptionRegex.matcher(item);
		if(matcher.find())
		    description = matcher.group(1);
		matcher = guidRegex.matcher(item);
		if(matcher.find())
		    guid = matcher.group(1);
		matcher = authorRegex.matcher(item);
		if(matcher.find())
		    author = matcher.group(1);
		matcher = pubDateRegex.matcher(item);
		if(matcher.find())
		    pubDate = matcher.group(1);
		NewsItem news = new NewsItem(title,link,description,guid,author,pubDate);
		return news;
	}
}
