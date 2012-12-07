package bah.conference.appliation.MenuItems;

import java.util.HashMap;
import java.util.Iterator;

import android.widget.ImageButton;

public class MenuItems {

	static HashMap<String,MenuItem> menuItems = new HashMap<String,MenuItem>();
	static{
		menuItems.put("map",new Map());
		menuItems.put("favorites",new Favorites());
		menuItems.put("info",new Info());
		menuItems.put("news",new News());
		menuItems.put("notes",new Notes());
		menuItems.put("schedule",new Schedule());
	}
	public static MenuItem open(String menuName){
        if(menuItems.containsKey(menuName)){
        	return menuItems.get(menuName);
        }
        return null;
	}
}
