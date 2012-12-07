package bah.conference.appliation.MenuItems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.MenuItems.Favorites.ScheduleAdapter;
import bah.conference.appliation.MenuItems.News.NewsAsync;
import bah.conference.appliation.dataStructures.ScheduleItem;
import bah.conference.appliation.dataStructures.ScheduleItem2;
import bah.conference.appliation.database.quick.Bottom_Menu;
import bah.conference.appliation.dialogs.ComboDialog;
import bah.conference.appliation.web.Network;

public class Schedule extends ListActivity implements MenuItem{
	@Override
	protected void onResume() {
		
		super.onResume();
	}
	ScheduleAdapter mAdapter;
	ArrayList<ScheduleItem2> scheduleItems;
	ConfApp confApp;
	HashMap<Integer,Button> buttons;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_schedule);
        confApp = (ConfApp)this.getApplication();
        buttons=new HashMap<Integer,Button>();
        buttons.put(1,(Button)this.findViewById(R.id.monday));
        buttons.put(2,(Button)this.findViewById(R.id.tuesday));
        buttons.put(3,(Button)this.findViewById(R.id.wednesday));
        buttons.put(4,(Button)this.findViewById(R.id.thursday));
        ConfApp.confApp.doWork();
        Iterator<Integer> iter = buttons.keySet().iterator();
        while(iter.hasNext()){
        	final Integer num = iter.next();
        	final Button iButton = buttons.get(num);
        	if(iButton != null){
        		iButton.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						setAdapter(num);
					}
        		});
        	}
        }
        setAdapter(1);

        ImageButton refresh = (ImageButton)this.findViewById(R.id.list_now_button_refresh);
        if(refresh!=null){
        	refresh.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					doubleCheck();
				}
        	});
        }
        Bottom_Menu.initialize(this);
    }
    private void doubleCheck(){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);

    	alert.setTitle("Warning!");
    	alert.setMessage("Pressing ok will DELETE all Notes and Favorites! Don't loose your Notes and Favorites, use the email buttons found on their respective pages!");

    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    		ScheduleAsync async = new ScheduleAsync(Schedule.this);
    		async.execute();
    	  }
    	});

    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int whichButton) {
    	    Log.wtf("zomg", "zomg");
    	  }
    	});

    	alert.show();
    	// see http://androidsnippets.com/prompt-user-input-with-an-alertdialog
    }
	public static class ScheduleAsync extends AsyncTask<Void,Void,Void>{
		ProgressDialog progressDialog;
		Schedule schedule;
		ScheduleAsync(Schedule schedule){
			this.schedule = schedule;
			this.progressDialog = new ProgressDialog(schedule);
		}
		@Override
		protected void onPreExecute() {
			WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();  
			lp.dimAmount=0.0f;
			progressDialog.getWindow().setAttributes(lp);  
			progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);  
			progressDialog.setCancelable(false);
			progressDialog.setTitle("Downloading Schedule!\nPlease wait...");
			progressDialog.setMessage("Reaching out to geoint2012.com/agenda.ical and retrieving current schedule");
			progressDialog.show(); 
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... values){
			this.schedule.confApp.updateSchedule(schedule);
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			ConfApp.confApp.doWork();
			schedule.setAdapter(1);
			progressDialog.dismiss();
			super.onPostExecute(result);
		}
	}
    public void openToDay(Activity activity, int day){
    	Intent intent = new Intent(activity,Schedule.class);
    	intent.putExtra("day", day);
		activity.startActivity(intent);
    }
	/**
	 * Using a list of button, toggles each on or off depending on the selected Button
	 * @param selected
	 */
	public void toggleButtons(Integer buttonName){
        Iterator<Integer> iter = buttons.keySet().iterator();
        while(iter.hasNext()){
        	int key = iter.next();
        	Button button = buttons.get(key);
			if(buttonName == key)
				button.setEnabled(false);
			else
				button.setEnabled(true);
		}
	}

	public void setAdapter(Integer buttonName){
		//mAdapter.changeList(confApp.local.getScheduleTimeSlots(buttonName));
		//mAdapter.notifyDataSetChanged();
		setListAdapter(new ScheduleAdapter(this,R.layout.simple_schedule_item,confApp.dayArrays.get(buttonName)));
		toggleButtons(buttonName);
	}
	public void open(Activity activity) {
		Intent intent = new Intent(activity,Schedule.class);
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
			ComboDialog comboDialog = new ComboDialog();
			comboDialog.initialize(Schedule.this, scheduleItem);
			comboDialog.show();
		}
	}
}
