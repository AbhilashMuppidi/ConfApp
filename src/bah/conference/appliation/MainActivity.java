package bah.conference.appliation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;

import bah.conference.appliation.MenuItems.MenuItem;
import bah.conference.appliation.MenuItems.MenuItems;
import bah.conference.appliation.dataStructures.BioItem;
import bah.conference.appliation.dataStructures.BoothLocation;
import bah.conference.appliation.web.BioParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
	protected void onResume() {

		super.onResume();

	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
        if(ConfApp.confApp == null){
        	ConfApp.confApp = (ConfApp)this.getApplication();
        	(new InitializeAsync(this)).execute();
        }
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressDialog p = new ProgressDialog(this);
        p.show();
        
        HashMap<String,ImageButton> buttons=new HashMap<String,ImageButton>();
        buttons.put("map",(ImageButton)this.findViewById(R.id.map));
        buttons.put("favorites",(ImageButton)this.findViewById(R.id.favorites));
        buttons.put("info",(ImageButton)this.findViewById(R.id.info));
        buttons.put("news",(ImageButton)this.findViewById(R.id.news));
        buttons.put("notes",(ImageButton)this.findViewById(R.id.notes));
        buttons.put("schedule",(ImageButton)this.findViewById(R.id.schedule));
        
        Iterator<String> iter = buttons.keySet().iterator();
        while(iter.hasNext()){
        	final String name = iter.next();
        	ImageButton iButton = buttons.get(name);
        	if(iButton != null){
        		iButton.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						MenuItem menuItem = MenuItems.open(name);
						if(menuItem!=null){
							menuItem.open(MainActivity.this);
						}
					}
        		});
        	}
        }
        
        p.dismiss();
        //outputToFile();
    }
    
    public void outputToFile(){
        try{

            File mydir = this.getDir("mydir", this.MODE_PRIVATE); //Creating an internal dir;
        File file = new File(mydir,"test.txt");
       // File fileWithinMyDir = new File(mydir, "myfile"); //Getting a file within the dir.
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        
        boolean first=true;
        writer.write("{\"data\":[");
        String output = "";
        for(BoothLocation booth : ConfApp.confApp.local.getBooths()){
        	if(!first){
        		writer.write(",");
        		output += ",";
        	}
        	writer.write(booth.toJson());
        	first=false;
        	output += booth.toJson();
        	
        }
        Log.wtf("HERE", output);
        System.out.println(output);
        writer.flush();
        writer.close();
        Intent i = new Intent(Intent.ACTION_SEND);
    	i.setType("message/rfc822");
    	i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"samuel.h.littlefield@gmail.com"});
    	i.putExtra(Intent.EXTRA_SUBJECT, "My GEOINT favorites");
    	i.putExtra(Intent.EXTRA_TEXT   , output);
    	try {
    	    startActivity(Intent.createChooser(i, "Send mail..."));
    	} catch (android.content.ActivityNotFoundException ex) {
    	    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
    	}
        } catch (IOException e) {
        e.printStackTrace();}}
    
    
    
    
    public static class InitializeAsync extends AsyncTask<Void,Void,Void>{
    	ProgressDialog progressDialog;
    	MainActivity main;
    	InitializeAsync(MainActivity mainActivity){
    		main = mainActivity;
    		progressDialog = new ProgressDialog(mainActivity);
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
			progressDialog.setMessage("Initializing app, this only occurs once!");
			progressDialog.show(); 
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... values){
	        ConfApp.confApp.initialize(main);
			return null;
		}
	}
}
