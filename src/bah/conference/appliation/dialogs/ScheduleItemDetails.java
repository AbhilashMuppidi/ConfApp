package bah.conference.appliation.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.dataStructures.ScheduleItem;
import bah.conference.appliation.dataStructures.ScheduleItem2;

public class ScheduleItemDetails {
	View myView;
	public void initialize(Activity activity,Dialog dialog, ScheduleItem2 tmp){
		final ConfApp confApp = (ConfApp)activity.getApplication();
	    final ScheduleItem2 scheduleItem = confApp.local.getSchedule2Item(tmp);
	    final Button details = (Button)dialog.findViewById(R.id.dialog_schedule_details_details_tab);
	    
	    myView = dialog.findViewById(R.id.dialog_schedule_details_schedule);
	    if(myView!=null){
	    	if(scheduleItem !=null){
	    		if(scheduleItem.description!= null){
	    			if(scheduleItem.description.length()>0){
	    				TextView description = (TextView)dialog.findViewById(R.id.details_schedule_description);
	    				if(description!=null){
	    					description.setText(scheduleItem.description);
	    				}
	    			}
	    		}
	    	}
	    }
	}
	public void show() {
		if(myView!=null)
			myView.setVisibility(View.VISIBLE);
	}
	public void hide(){
		if(myView!=null)
			myView.setVisibility(View.GONE);
	}
}
