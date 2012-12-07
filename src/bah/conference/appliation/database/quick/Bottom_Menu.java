package bah.conference.appliation.database.quick;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import bah.conference.appliation.MainActivity;
import bah.conference.appliation.R;
import bah.conference.appliation.MenuItems.Now;

public class Bottom_Menu {
	public static void initialize(final Activity activity){
		if(activity!=null){
	       ImageButton map_button = (ImageButton)activity.findViewById(R.id.home);
	       if(map_button != null){
	    	   if(!(activity instanceof MainActivity)){
	    		   map_button.setOnClickListener(new OnClickListener(){
		        		public void onClick(View v){
		        			Intent intent = new Intent(activity,MainActivity.class);
		        			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        			activity.startActivity(intent);
		        		}
		        	});
	    	   }else
	    		   map_button.setEnabled(false);
	        }
	        ImageButton menu_button = (ImageButton)activity.findViewById(R.id.now);
	        if(menu_button!=null){
	        	if(!(activity instanceof Now)){
		        	menu_button.setOnClickListener(new OnClickListener(){
		        		public void onClick(View v){
		        			Intent intent = new Intent(activity,Now.class);
		        			activity.startActivity(intent);
		        		}
		        	});  
	        	}else
	        		menu_button.setEnabled(false);
	        }
		}
	}
}
