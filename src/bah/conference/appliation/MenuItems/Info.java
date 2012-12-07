package bah.conference.appliation.MenuItems;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.database.quick.Bottom_Menu;

public class Info extends Activity implements MenuItem{
	ConfApp confApp;
	Dialog dialog;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_info);
        confApp = (ConfApp)this.getApplication();
        dialog = new Dialog(this);
        
        Button bio =(Button)this.findViewById(R.id.list_info_button_speakers);
        if(bio!=null){
        	bio.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					Speakers speakers = new Speakers();
					speakers.open(Info.this);
				}
        	});
        }
        Bottom_Menu.initialize(this);
        
        Button faq =(Button)this.findViewById(R.id.list_info_faq);
        if(faq!=null){
        	faq.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					Faq faq = new Faq();
					faq.open(Info.this);
				}
        	});
        }
        
        Button about =(Button)this.findViewById(R.id.list_info_button_about_geoint);
        if(about!=null){
        	about.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					About about = new About();
					about.open(Info.this);
				}
        	});
        }
        
        Button hall =(Button)this.findViewById(R.id.list_info_exhibit_hall_hours);
        if(hall!=null){
        	hall.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					Hall hall = new Hall();
					hall.open(Info.this);
				}
        	});
        }
        
        Button future =(Button)this.findViewById(R.id.list_info_future_dates);
        if(future!=null){
        	future.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					Future future = new Future();
					future.open(Info.this);
				}
        	});
        }
        
        Button tech =(Button)this.findViewById(R.id.list_info_button_tech_talks);
        if(tech!=null){
        	tech.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					Tech tech = new Tech();
					tech.open(Info.this);
				}
        	});
        }
        /*
        Button location =(Button)this.findViewById(R.id.list_info_button_symposium_location);
        if(location!=null){
        	location.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					Intent intent = new Intent(Info.this,SymposiumLocation.class);
					Info.this.startActivity(intent);
				}
        	});
        }*/
        Button aboutapp =(Button)this.findViewById(R.id.list_info_about_app);
        if(aboutapp!=null){
        	aboutapp.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					BoozAbout aboutapp = new BoozAbout();
					aboutapp.open(Info.this);
				}
        	});
        }        
	}
	public void open(Activity activity) {
		Intent intent = new Intent(activity,Info.class);
		activity.startActivity(intent);
	}
}
