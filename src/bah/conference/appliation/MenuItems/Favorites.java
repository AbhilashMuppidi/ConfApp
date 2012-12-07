package bah.conference.appliation.MenuItems;

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
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.dataStructures.ScheduleItem;
import bah.conference.appliation.dataStructures.ScheduleItem2;
import bah.conference.appliation.database.quick.Bottom_Menu;
import bah.conference.appliation.dialogs.ComboDialog;
import bah.conference.appliation.web.Network;



public class Favorites extends ListActivity implements MenuItem{
	ScheduleAdapter mAdapter;
	ArrayList<ScheduleItem2> scheduleItems;
	ConfApp confApp;
	HashMap<Integer,Button> buttons;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_favorites);
        confApp = (ConfApp)this.getApplication();
        ConfApp.confApp.doWork();
        buttons=new HashMap<Integer,Button>();
        buttons.put(1,(Button)this.findViewById(R.id.monday));
        buttons.put(2,(Button)this.findViewById(R.id.tuesday));
        buttons.put(3,(Button)this.findViewById(R.id.wednesday));
        buttons.put(4,(Button)this.findViewById(R.id.thursday));
        
        Iterator<Integer> iter = buttons.keySet().iterator();
        while(iter.hasNext()){
        	final Integer num = iter.next();
        	final Button iButton = buttons.get(num);
        	if(iButton != null){
        		iButton.setOnClickListener(new OnClickListener(){
					
					public void onClick(View v) {
						setAdapter(num);
						toggleButtons(iButton,buttons);
					}
        		});
        	}
        }
        
        ImageButton email = (ImageButton)this.findViewById(R.id.list_favorites_button_email);
        if(email!=null){
        	email.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					String output = formatFavorites();
					if(output.length() > 0){
						Intent i = new Intent(Intent.ACTION_SEND);
						i.setType("message/rfc822");
						i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
						i.putExtra(Intent.EXTRA_SUBJECT, "My GEOINT favorites");
						i.putExtra(Intent.EXTRA_TEXT   , output);
						try {
						    startActivity(Intent.createChooser(i, "Send mail..."));
						} catch (android.content.ActivityNotFoundException ex) {
						    Toast.makeText(Favorites.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
						}
					}
					else{
						Toast.makeText(Favorites.this, "You have no favorites to email!", Toast.LENGTH_LONG).show();
					}
				}
        	});
        }
        scheduleItems = confApp.local.getFavoriteTimeSlots(1);
        toggleButtons(buttons.get(1),buttons);
        mAdapter = new ScheduleAdapter(this,R.layout.simple_schedule_item,scheduleItems);
		setListAdapter(mAdapter);
        Bottom_Menu.initialize(this);
    }
    public String formatFavorites(){
    	String output="";
    	for(int i = 1; i < 5; i++){
    		ArrayList<ScheduleItem2> notes = confApp.local.getFavoriteTimeSlots(i);
    		if(notes.size()>0){
    			switch(i){
    			case 1 :
    				output += 
    				"\nMonday October 8, 2012\n";
    				break;
    			case 2 :
    				output +=
    				"\nMonday October 9, 2012\n";
    				break;
    			case 3 :
    				output += 
    				"\nMonday October 10, 2012\n";
    				break;
    			case 4 :
    				output += 
    				"\nMonday October 11, 2012\n";
    				break;
    			}
    			output += arrayToNotes(notes);
    		}
    	}
    	return output;
    }
    private String arrayToNotes(ArrayList<ScheduleItem2> favorites){
    	String result = "";
    	for(ScheduleItem2 scheduleItem : favorites){
    		result += scheduleItem.toString_favorites_format();
    	}
    	return result;
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
	public void setAdapter(Integer buttonName){
		setListAdapter(new ScheduleAdapter(this,R.layout.simple_schedule_item,confApp.local.getFavoriteTimeSlots(buttonName)));
		//mAdapter.changeList(confApp.local.getFavoriteTimeSlots(buttonName));
		//mAdapter.notifyDataSetChanged();
	}
	
	public void open(Activity activity) {
		Intent intent = new Intent(activity,Favorites.class);
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
			ComboDialogFavorites comboDialog = new ComboDialogFavorites();
			comboDialog.setItem(scheduleItem);
			comboDialog.initialize(Favorites.this, scheduleItem);
			comboDialog.show();
		}
		protected void onActivityResult(int requestCode,int resultCode,Intent data){
		
		}
	}
	private class ComboDialogFavorites extends ComboDialog{
		ScheduleItem2 scheduleItem;
		public void setItem(ScheduleItem2 scheduleItem){
			this.scheduleItem = scheduleItem;
		}
		@Override
		public void hide() {
			super.hide();
			Iterator<Integer> keys = buttons.keySet().iterator();
			while(keys.hasNext()){
				int key = keys.next();
				Button button = buttons.get(key);
				if(!button.isEnabled())
					setAdapter(key);
			}
		}
	}

}
