package bah.conference.appliation.dialogs;

import java.io.IOException;
import java.lang.reflect.Field;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.dataStructures.BioItem;
import bah.conference.appliation.dataStructures.ScheduleItem;

public class BioDetails {
	View myView;
	Activity activity;
	public void initialize(Activity activity,Dialog dialog, String speakerName){
		this.activity = activity;
		myView = dialog.findViewById(R.id.dialog_schedule_details_bio);
		if(myView != null){
			final ConfApp confApp = (ConfApp)activity.getApplication();
		    
		    final BioItem bioItem = confApp.local.getBioItem(speakerName);
		    final Button speaker = (Button)dialog.findViewById(R.id.dialog_schedule_details_speaker_tab);
		    if(bioItem!=null){
		    	
			    TextView title =(TextView)dialog.findViewById(R.id.details_bio_title);
			    TextView name = (TextView)dialog.findViewById(R.id.details_bio_name);
			    ImageView image = (ImageView)dialog.findViewById(R.id.details_bio_image);
			    TextView bio = (TextView)dialog.findViewById(R.id.details_bio_bio);
			   
			    if(title!=null)
			    	title.setText(bioItem.title);
			    if(name!=null)
			    	name.setText(bioItem.name);
			    if(image!=null){
					String blah = bioItem.image;
					if(blah.contains("."))
						blah = (String)bioItem.image.subSequence(0, bioItem.image.indexOf("."));
					image.setBackgroundResource(showImage("image_"+blah));
			    }
			    if(bio!=null)
			    	bio.setText(getBio(bioItem.bio,activity));
		    }
		    else{
		    	if(speaker!=null)
		    		speaker.setVisibility(View.GONE);
		    	if(myView!=null)
		    		myView.setVisibility(View.GONE);
		    }
		}
	}
	
	public int getImage(String imageName,Activity activity){
		return getResId(imageName,activity,Drawable.class);
	}
	public int getResId(String variableName, Context context, Class<?> c) {
	    try {
	        Field idField = c.getDeclaredField(variableName);
	        return idField.getInt(idField);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    } 
	}
	public String getBio(String bioPath,Activity activity){
		try {
			return convertStreamToString(activity.getAssets().open("text_"+bioPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
		
	}
	public String convertStreamToString(java.io.InputStream is) {
	    try {
	        return new java.util.Scanner(is).useDelimiter("\\A").next();
	    } catch (java.util.NoSuchElementException e) {
	        return "";
	    }
	}
	public int showImage(String name){
	    String uri = "drawable/"+name;

	    // int imageResource = R.drawable.icon;
	    int imageResource = activity.getResources().getIdentifier(uri, null, activity.getPackageName());
	    return imageResource;
	}
	
	public void show(){
		if(myView!=null)
			myView.setVisibility(View.VISIBLE);
	}
	public void hide(){
		if(myView!=null)
			myView.setVisibility(View.GONE);
	}
}
