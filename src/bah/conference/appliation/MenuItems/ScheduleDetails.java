package bah.conference.appliation.MenuItems;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.dataStructures.ScheduleItem2;
import bah.conference.appliation.database.quick.Bottom_Menu;

public class ScheduleDetails extends Activity {
	ConfApp confApp;
	ScheduleItem2 scheduleItem;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_schedule);
        Intent intent = this.getIntent();
        confApp = (ConfApp)this.getApplication();
        if(intent.hasExtra("extra")){
        	scheduleItem = intent.getParcelableExtra("extra");
		    TextView title =(TextView)this.findViewById(R.id.schedule_title);
		    if(title!=null)
		    	title.setText(scheduleItem.summary);
		    TextView startTime = (TextView)this.findViewById(R.id.schedule_time_start);
		    if(startTime!=null)
		    	startTime.setText(scheduleItem.getStartTimeFormatted());
		    TextView endTime = (TextView)this.findViewById(R.id.schedule_time_end);
		    if(endTime!=null)
		    	endTime.setText(scheduleItem.getEndTimeFormatted());
		    TextView location = (TextView)this.findViewById(R.id.location);
		    if(location!=null)
		    	location.setText(scheduleItem.location);
		    TextView description = (TextView)this.findViewById(R.id.description);
		    if(description!=null)
		    	description.setText(scheduleItem.description);
		    ImageView map = (ImageView)this.findViewById(R.id.map);
		    if(map!=null)
		    	centerOnLocation(scheduleItem.location);
		    
		    ImageButton viewNotes = (ImageButton)this.findViewById(R.id.notes);
		    if(viewNotes!=null){
		    	viewNotes.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						Intent intent = new Intent(ScheduleDetails.this,Notes.class);
						ScheduleDetails.this.startActivity(intent);
					}
		    	});
		    }
		    ImageButton viewBio = (ImageButton)this.findViewById(R.id.bio);
		    if(viewBio!=null){
		    	viewBio.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						//Intent intent = new Intent(ScheduleDetails.this,Bio.class);
						//intent.putExtra("extra", scheduleItem.speaker);
						//ScheduleDetails.this.startActivity(intent);
					}
		    	});
		    }
        }
        Bottom_Menu.initialize(this);
    }
    public void centerOnLocation(String location){
    	//dosomething with the map to show it in the imageview
    }
}
