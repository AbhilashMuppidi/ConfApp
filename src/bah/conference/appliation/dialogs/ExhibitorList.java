package bah.conference.appliation.dialogs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.dataStructures.Exhibitor;

public class ExhibitorList{
	Activity activity;
	Dialog dialog;
	ArrayList<Exhibitor> exhibitors;
	ExhibitorAdapter mAdapter;
	public void initialize(Activity activity,Dialog dialog){
		this.dialog = dialog;
		this.activity = activity;
        dialog.setContentView(R.layout.exhibitor_list);
        exhibitors = ConfApp.confApp.local.getExhibitors();
        mAdapter = new ExhibitorAdapter(activity,R.layout.simple_news_item,exhibitors);
        
        ListView list = (ListView)dialog.findViewById(R.id.list_exhibitor);
		list.setAdapter(mAdapter);
		TextView view = (TextView)dialog.findViewById(R.id.empty);
		if(exhibitors.size()>0){
			list.setVisibility(View.VISIBLE);
			view.setVisibility(View.GONE);
		}
		else{
			list.setVisibility(View.GONE);
			view.setVisibility(View.VISIBLE);
		}
		
		Button ok = (Button)this.dialog.findViewById(R.id.exhibitor_list_ok);
		if(ok!=null){
			ok.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					hide();
				}
			});
		}
    }
	public class ExhibitorAdapter extends ArrayAdapter<Exhibitor>{
		Activity activity;
		ArrayList<Exhibitor> exhibitors;
		public ExhibitorAdapter(Activity activity, int textViewResourceId, ArrayList<Exhibitor> exhibitors){
			super(activity,textViewResourceId,exhibitors);
			this.activity = activity;
			this.exhibitors = exhibitors;
		}
		public void changeList(ArrayList<Exhibitor> exhibitors){
			this.exhibitors = exhibitors;
		}
		@Override
		public View getView(int index, View convertView, ViewGroup parent){
			final Exhibitor exhibitor = this.exhibitors.get(index);
			
			if(convertView == null){
	            LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = vi.inflate(R.layout.simple_exhibitor_item, null);
			}
			TextView name = (TextView)convertView.findViewById(R.id.company);
			if(name != null){
				name.setText(exhibitor.company);
			}
			TextView booth = (TextView)convertView.findViewById(R.id.num);
			if(booth!=null){
				booth.setText(exhibitor.boothNumber);
			}

			return convertView;
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
