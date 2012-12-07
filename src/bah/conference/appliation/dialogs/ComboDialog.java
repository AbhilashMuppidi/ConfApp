package bah.conference.appliation.dialogs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.dataStructures.ScheduleItem;
import bah.conference.appliation.dataStructures.ScheduleItem2;

public class ComboDialog {
	ConfApp confApp;
	ScheduleItem2 scheduleItem;
	Activity activity;
	Dialog dialog;
	
	public void initialize(Activity activity,final ScheduleItem2 scheduleItem){
		this.dialog = new Dialog(activity);
		this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.dialog.setContentView(R.layout.dialog_schedule_details);
	    this.confApp = (ConfApp)activity.getApplication();
	    this.activity = activity;
	    this.scheduleItem = scheduleItem;
	    final BioDetails bioDetails = new BioDetails();
	    bioDetails.initialize(activity,dialog,scheduleItem.speaker);
	    final ScheduleItemDetails scheduleItemDetails = new ScheduleItemDetails();
	    scheduleItemDetails.initialize(activity,dialog,scheduleItem);
	    final NoteDetails noteDetails = new NoteDetails();
	    noteDetails.initialize(activity,dialog,scheduleItem);
	    
	    final Button details = (Button)dialog.findViewById(R.id.dialog_schedule_details_details_tab);
	    final Button speaker = (Button)dialog.findViewById(R.id.dialog_schedule_details_speaker_tab);
	    final Button notes = (Button)dialog.findViewById(R.id.dialog_schedule_details_notes_tab);
	    final ArrayList<Button> buttons = new ArrayList<Button>();
	    buttons.add(details);
	    buttons.add(speaker);
	    buttons.add(notes);

	    TextView startTime = (TextView)dialog.findViewById(R.id.details_schedule_time);
	    if(startTime!=null)
	    	startTime.setText(scheduleItem.formatYearMonthDay(scheduleItem.dtstart)+ " From " +scheduleItem.getStartTimeFormatted()+" - "+scheduleItem.getEndTimeFormatted());
	    TextView location = (TextView)dialog.findViewById(R.id.details_schedule_location);
	    if(location!=null)
	    	location.setText(scheduleItem.location);
	    if(details!=null){
	    	details.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					toggleButtons(buttons,details);
					noteDetails.hide();
					bioDetails.hide();
					scheduleItemDetails.show();
				}
	    	});
	    }
	    if(speaker!=null){
	    	speaker.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					toggleButtons(buttons,speaker);
					noteDetails.hide();
					bioDetails.show();
					scheduleItemDetails.hide();
				}
	    		
	    	});
	    }
	    if(notes!=null){
	    	notes.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					toggleButtons(buttons,notes);
					noteDetails.show();
					bioDetails.hide();
					scheduleItemDetails.hide();
				}
	    	});
	    }
	    if(details.getVisibility()!=View.GONE)
	    	details.performClick();
	    else if(speaker.getVisibility()!=View.GONE)
	    	speaker.performClick();
	    else
	    	notes.performClick();
		TextView title = (TextView)dialog.findViewById(R.id.dialog_schedule_details_title);
		if(title!=null)
			title.setText(scheduleItem.summary);
		
		final ImageButton favorites = (ImageButton)dialog.findViewById(R.id.dialog_schedule_details_favorite);
	    if(favorites!=null){
	    	adjustFavorited(favorites);
	    	favorites.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					boolean worked = confApp.local.adjustFavorites(scheduleItem, !scheduleItem.favorited);
					scheduleItem.favorited = !scheduleItem.favorited;
					adjustFavorited(favorites);
				}
	    	});
	    }
		Button ok = (Button)this.dialog.findViewById(R.id.dialog_schedule_details_ok_button);
		if(ok!=null){
			ok.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					noteDetails.hide();
					bioDetails.hide();
					scheduleItemDetails.hide();
					hide();
				}
			});
		}
	}
    public void adjustFavorited(ImageButton imageButton){
    	if(scheduleItem.favorited){
    		imageButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.favorites));
    	}else{
    		imageButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.unfavorite));
    	}
    }
	private void toggleButtons(ArrayList<Button> buttons, Button selected){
		for(Button button : buttons){
			if(button == selected)
				button.setEnabled(false);
			else
				button.setEnabled(true);
		}
	}
	public void show(){
		if(dialog!=null){
		    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		    Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		    lp.copyFrom(dialog.getWindow().getAttributes());

		    lp.width = WindowManager.LayoutParams.FILL_PARENT;
		    //lp.height = ;
			    lp.y = Gravity.TOP;
			    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);  
				dialog.show();
			dialog.getWindow().setAttributes(lp);
		}
	}
	public void hide(){
		if(dialog!=null)
			dialog.dismiss();
	}
}
