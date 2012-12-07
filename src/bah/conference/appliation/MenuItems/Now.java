package bah.conference.appliation.MenuItems;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.MenuItems.Schedule.ScheduleAdapter;
import bah.conference.appliation.dataStructures.ScheduleItem;
import bah.conference.appliation.dataStructures.ScheduleItem2;
import bah.conference.appliation.database.quick.Bottom_Menu;
import bah.conference.appliation.dialogs.ComboDialog;

public class Now extends ListActivity implements MenuItem{

	ScheduleAdapter mAdapter;
	ArrayList<ScheduleItem2> scheduleItems;
	ConfApp confApp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_now);
        confApp = (ConfApp)this.getApplication();
        
        scheduleItems = confApp.local.getSchedule2TimeSlots(1);
        ConfApp.confApp.doWork();
        setAdapter();
       
        Bottom_Menu.initialize(this);
    }
	/**
	 * Using a list of button, toggles each on or off depending on the selected Button
	 * @param selected
	 */
	public void toggleButtons(Button selected,HashMap<Integer,Button> buttons){
        Iterator<Integer> iter = buttons.keySet().iterator();
        while(iter.hasNext()){
        	Button button = buttons.get(iter.next());
			if(button == selected)
				button.setEnabled(false);
			else
				button.setEnabled(true);
		}
	}
	public void setAdapter(){
		setListAdapter(new ScheduleAdapter(this,R.layout.simple_schedule_item,confApp.local.getSchedule2Now(confApp.getCurrentTimeStamp())));
	}
	public void open(Activity activity) {
		Intent intent = new Intent(activity,Now.class);
		activity.startActivity(intent);
	}
	public class ScheduleAdapter extends ArrayAdapter<ScheduleItem2>{
		Activity activity;
		ArrayList<ScheduleItem2> scheduleItems;
		public ScheduleAdapter(Activity activity, int textViewResourceId, ArrayList<ScheduleItem2> scheduleItems){
			super(activity,textViewResourceId,scheduleItems);
			this.activity = activity;
			this.scheduleItems = scheduleItems;
		}
		public void changeList(ArrayList<ScheduleItem2> scheduleItems){
			this.scheduleItems = scheduleItems;
		}
		@Override
		public View getView(int index, View convertView, ViewGroup parent){
			final ScheduleItem2 scheduleItem = this.scheduleItems.get(index);
			
			if(convertView == null){
	            LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = vi.inflate(R.layout.simple_schedule_item, null);
			}
			TextView scheduleTitle = (TextView)convertView.findViewById(R.id.schedule_title);
			if(scheduleTitle != null){
				scheduleTitle.setText(scheduleItem.summary);
			}
			TextView time = (TextView)convertView.findViewById(R.id.time);
			if(time!=null){
				;
				time.setText(scheduleItem.getStartTimeFormatted() + '-' + scheduleItem.getEndTimeFormatted());
			}
			TextView location = (TextView)convertView.findViewById(R.id.location);
			if(location!=null){
				location.setText(scheduleItem.location);
			}
			View view = (View)convertView.findViewById(R.id.simple_schedule_item_status);
			boolean found = false;
			for(ScheduleItem2 items : confApp.previous){
				if(items.pk_id == scheduleItem.pk_id){
					view.setBackgroundColor(activity.getResources().getColor(R.color.past_schedule_item));
					found = true;
					break;
				}
			}
			if(!found){
				for(ScheduleItem2 items : confApp.current){
					if(items.pk_id == scheduleItem.pk_id){
						view.setBackgroundColor(activity.getResources().getColor(R.color.current_schedule_item));
						found = true;
						break;
					}
				}
			}
			if(!found){
				view.setBackgroundColor(activity.getResources().getColor(R.color.future_schedule_item));
			}

			convertView.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					dayItemOnClick(v,scheduleItem);
	        	}
			});
			return convertView;
		}
		public void dayItemOnClick(View v,ScheduleItem2 scheduleItem){
			ComboDialog comboDialog = new ComboDialog();
			comboDialog.initialize(Now.this, scheduleItem);
			comboDialog.show();
		}
	}
	

}
