package bah.conference.appliation.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.dataStructures.ScheduleItem;
import bah.conference.appliation.dataStructures.ScheduleItem2;

public class NoteDetails {
	View myView;
	ScheduleItem2 scheduleItem;
	ConfApp confApp;
	EditText notes;
	public void initialize(Activity activity,Dialog dialog, ScheduleItem2 tmp){
		confApp = (ConfApp)activity.getApplication();
	    scheduleItem = confApp.local.getSchedule2Item(tmp);
	    myView = dialog.findViewById(R.id.dialog_schedule_details_notes);
	    if(myView != null){
		    if(scheduleItem!=null){
			    notes = (EditText)dialog.findViewById(R.id.note_details_notes);
			    if(notes!=null)
			    	notes.setText(scheduleItem.notes);
		    }
	    }
	}
	public void show() {
		if(myView!=null)
			myView.setVisibility(View.VISIBLE);
	}
	public void hide() {
		boolean worked = false;
		if(myView!=null){
			if(notes!=null){
				worked = confApp.local.adjustNotes(scheduleItem, notes.getText().toString());
			}
			if(myView!=null)
				myView.setVisibility(View.GONE);
			
		}
	}
}
