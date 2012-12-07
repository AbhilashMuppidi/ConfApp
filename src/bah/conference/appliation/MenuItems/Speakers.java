package bah.conference.appliation.MenuItems;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.MainActivity;
import bah.conference.appliation.R;
import bah.conference.appliation.dataStructures.BioItem;
import bah.conference.appliation.dataStructures.ScheduleItem;
import bah.conference.appliation.database.quick.Bottom_Menu;
import bah.conference.appliation.dialogs.BioDetails;

public class Speakers extends ListActivity implements MenuItem{
	BioAdapter mAdapter;
	ArrayList<BioItem> bioItems;
	Dialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_speakers);
        (new InitializeAsync(this)).execute();
        
        dialog = new Dialog(this);
        bioItems = ConfApp.confApp.local.getBioItems();
        mAdapter = new BioAdapter(this,R.layout.simple_bio_item,bioItems);
		setListAdapter(mAdapter);
        Bottom_Menu.initialize(this);
    }
	public void setAdapter(){
		setListAdapter(new BioAdapter(this,R.layout.simple_bio_item,ConfApp.confApp.local.getBioItems()));
	}
	
	public void open(Activity activity) {
		Intent intent = new Intent(activity,Speakers.class);
		activity.startActivity(intent);
	}
	public class BioAdapter extends ArrayAdapter<BioItem>{
		Activity activity;
		ArrayList<BioItem> bioItems;
		public BioAdapter(Activity activity, int textViewResourceId, ArrayList<BioItem> bioItems){
			super(activity,textViewResourceId,bioItems);
			this.activity = activity;
			this.bioItems = bioItems;
		}
		@Override
		public View getView(int index, View convertView, ViewGroup parent){
			final BioItem bioItem = this.bioItems.get(index);
			
			if(convertView == null){
	            LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = vi.inflate(R.layout.simple_bio_item, null);
			}
			
			ImageView image = (ImageView)convertView.findViewById(R.id.simple_bio_item_image);
			TextView name = (TextView)convertView.findViewById(R.id.simple_bio_item_name);
			TextView title = (TextView)convertView.findViewById(R.id.simple_bio_item_title);
			
			if(image!=null){
				String blah = bioItem.image;
				if(blah.contains("."))
					blah = (String)bioItem.image.subSequence(0, bioItem.image.indexOf("."));
				image.setBackgroundResource(showImage("image_"+blah));
				
			}
			
			if(title!=null)
				title.setText(bioItem.title);
			if(name!=null)
				name.setText(bioItem.name);
			
			convertView.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					if(dialog!=null)
						dialog.dismiss();
					dialog = new Dialog(Speakers.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.dialog_bio);
					BioDetails bioDetails=new BioDetails();
				    bioDetails.initialize(Speakers.this,dialog,bioItem.name);
				    Button ok = (Button)dialog.findViewById(R.id.dialog_bio_ok);
				    if(ok!=null){
				    	ok.setOnClickListener(new OnClickListener(){
							public void onClick(View v) {
								dialog.dismiss();
							}
				    	});
				    }
				    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				    Display display = ((WindowManager) Speakers.this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
				    lp.copyFrom(dialog.getWindow().getAttributes());

				    int height = ((Speakers.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ? display.getHeight():display.getWidth());
				    int width = ((Speakers.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ? display.getWidth():display.getHeight());
				    lp.width = WindowManager.LayoutParams.FILL_PARENT;
					lp.y = Gravity.TOP;
					dialog.show();
					dialog.getWindow().setAttributes(lp);
	        	}
			});
			return convertView;
		}
	}
	public int showImage(String name){
	    String uri = "drawable/"+name;

	    // int imageResource = R.drawable.icon;
	    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
	    return imageResource;
	}
    public static class InitializeAsync extends AsyncTask<Void,Void,Void>{
    	ProgressDialog progressDialog;
    	Speakers speakers;
    	InitializeAsync(Speakers speakers){
    		this.speakers = speakers;
    		progressDialog = new ProgressDialog(speakers);
    	}
		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			speakers.setAdapter();
			super.onPostExecute(result);
		}
		@Override
		protected void onPreExecute() {
			WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();  
			lp.dimAmount=0.0f;
			progressDialog.getWindow().setAttributes(lp);  
			progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);  
			progressDialog.setCancelable(false);
			progressDialog.setTitle("Initializing app,\n Please wait...");
			progressDialog.setMessage("Gathering data and storing it on this phone, enjoy the offline freedom!");
			progressDialog.show(); 
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... values){
	        ConfApp.confApp.populateBio();
			return null;
		}
	}
}
