package bah.conference.appliation.MenuItems;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.database.quick.Bottom_Menu;
import bah.conference.appliation.database.quick.TouchImageView;
import bah.conference.appliation.dialogs.ExhibitorList;

public class Map extends Activity implements MenuItem{
	
	ArrayList<Button> buttons;
	TouchImageView touchImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		(new InitializeAsync(this)).execute();
		setContentView(R.layout.map);
		touchImageView = (TouchImageView)findViewById(R.id.img);
		buttons = new ArrayList<Button>();
		if(touchImageView != null){
			final Button ballroom = (Button)findViewById(R.id.map_button_ballroom);
			final Button exhibitionHall = (Button)findViewById(R.id.map_button_exhibitionhall);
			if((ballroom!=null) && (exhibitionHall != null)){
				buttons.add(exhibitionHall);
				buttons.add(ballroom);
				
				ballroom.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						clickedButtonBallroom(touchImageView);
						toggleButtons(buttons,ballroom);
					}
				});
				exhibitionHall.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						clickedButtonExhibitionHall(touchImageView);
						toggleButtons(buttons,exhibitionHall);
					}
				});
				ballroom.performClick();
			}
			ImageButton listItems = (ImageButton)this.findViewById(R.id.listExhibition);
			listItems.setOnClickListener( new OnClickListener(){
				public void onClick(View v) {
					showListDialog();
				}
			});
		}
		
		Bottom_Menu.initialize(this);
	}

	private void toggleButtons(ArrayList<Button> buttons, Button selectedButton){
		for(Button button : buttons){
			if(button == selectedButton)
				button.setEnabled(false);
			else
				button.setEnabled(true);
		}
	}
	public void clickedButtonExhibitionHall(TouchImageView touchImageView){
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.exhibitionhall_832x1662);
		touchImageView.setImageBitmap(bmp);
		touchImageView.enableInteraction(true);
		touchImageView.setMaxZoom(4f); //change the max level of zoom, default is 3f
	}
	public void clickedButtonBallroom(TouchImageView touchImageView){
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ballroom_832x1662);
		touchImageView.enableInteraction(false);
		touchImageView.setImageBitmap(bmp);
		touchImageView.setMaxZoom(4f); //change the max level of zoom, default is 3f
	}
	public void open(Activity activity) {
		Intent intent = new Intent(activity,Map.class);
		activity.startActivity(intent);
	}
    public static class InitializeAsync extends AsyncTask<Void,Void,Void>{
    	ProgressDialog progressDialog;
    	Activity main;
    	InitializeAsync(Activity activity){
    		main = activity;
    		progressDialog = new ProgressDialog(activity);
    	}
		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
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
			progressDialog.setMessage("Loading images and exhibitors. This occurs once.");
			progressDialog.show(); 
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... values){
	        ConfApp.confApp.populateBooths();
	        ConfApp.confApp.populateExhibitor();
			return null;
		}
	}
    public void showListDialog(){
    	ExhibitorList a = new ExhibitorList();
    	a.initialize(this, new Dialog(this));
    	a.show();
    }
}
