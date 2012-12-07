package bah.conference.appliation.MenuItems;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import bah.conference.appliation.ConfApp;
import bah.conference.appliation.R;
import bah.conference.appliation.MenuItems.Favorites.ScheduleAdapter;
import bah.conference.appliation.dataStructures.BioItem;
import bah.conference.appliation.dataStructures.NewsItem;
import bah.conference.appliation.database.quick.Bottom_Menu;
import bah.conference.appliation.web.BioParser;
import bah.conference.appliation.web.Network;

public class News extends ListActivity implements MenuItem{
	public NewsAdapter mAdapter;
	ArrayList<NewsItem> newsItems;
	ConfApp confApp;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_news);
        confApp = (ConfApp)this.getApplication();
        newsItems = new ArrayList<NewsItem>();
        mAdapter = new NewsAdapter(this,R.layout.simple_news_item,newsItems);
		setListAdapter(mAdapter);
        Bottom_Menu.initialize(this);
        
        ImageButton refresh = (ImageButton)this.findViewById(R.id.list_news_button_refresh);
        if(refresh!=null){
        	refresh.setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					NewsAsync async = new NewsAsync(News.this);
					async.execute();
				}
        	});
        }

        
    }
    @Override
	protected void onResume() {
    	(new InitializeAsync(this)).execute();
		super.onResume();
	}
	public static class InitializeAsync extends AsyncTask<Void,Void,Void>{
    	ProgressDialog progressDialog;
    	News news;
    	InitializeAsync(News news){
    		this.news = news;
    		progressDialog = new ProgressDialog(news);
    	}
		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
	        news.getNews();
			super.onPostExecute(result);
		}
		@Override
		protected void onPreExecute() {
			WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();  
			lp.dimAmount=0.0f;
			progressDialog.getWindow().setAttributes(lp);  
			progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);  
			progressDialog.setCancelable(false);
			progressDialog.setTitle("Loading News!\nPlease wait...");
			progressDialog.setMessage("Reaching out to geoint2012.com/news and retrieving current news");
			progressDialog.show(); 
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... values){
	        ConfApp.confApp.populateNews(news);
			return null;
		}
	}
	public static class NewsAsync extends AsyncTask<Void,Void,Void>{
		ProgressDialog progressDialog;
		News news;
		NewsAsync(News news){
			this.news = news;
			this.progressDialog = new ProgressDialog(news);
		}
		@Override
		protected void onPreExecute() {
			WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();  
			lp.dimAmount=0.0f;
			progressDialog.getWindow().setAttributes(lp);  
			progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);  
			progressDialog.setCancelable(false);
			progressDialog.setTitle("Loading News!\nPlease wait...");
			progressDialog.setMessage("Reaching out to geoint2012.com/news and retrieving current news");
			progressDialog.show(); 
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... values){
			ConfApp.confApp.updateNews(news);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			news.getNews();
			progressDialog.dismiss();
			
			super.onPostExecute(result);
		}
	}
	public void open(Activity activity) {
		Intent intent = new Intent(activity,News.class);
		activity.startActivity(intent);
	}
	public void getNews(){
		setListAdapter(new NewsAdapter(this,R.layout.simple_news_item,confApp.local.getNews()));
	}
	public class NewsAdapter extends ArrayAdapter<NewsItem>{
		Activity activity;
		ArrayList<NewsItem> newsItems;
		public NewsAdapter(Activity activity, int textViewResourceId, ArrayList<NewsItem> newsItems){
			super(activity,textViewResourceId,newsItems);
			this.activity = activity;
			this.newsItems = newsItems;
		}
		public void changeList(ArrayList<NewsItem> newsItems){
			this.newsItems = newsItems;
		}
		@Override
		public View getView(int index, View convertView, ViewGroup parent){
			final NewsItem news = this.newsItems.get(index);
			
			if(convertView == null){
	            LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = vi.inflate(R.layout.simple_news_item, null);
			}
			TextView scheduleTitle = (TextView)convertView.findViewById(R.id.news_title);
			if(scheduleTitle != null){
				scheduleTitle.setText(news.title);
			}
			TextView time = (TextView)convertView.findViewById(R.id.pubDate);
			if(time!=null){
				time.setText(news.pubDate);
			}

			convertView.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					dayItemOnClick(v,news);
	        	}
			});
			return convertView;
		}
		public void dayItemOnClick(View v,NewsItem newsItem){
			openDialog(newsItem);
		}
		public boolean dayItemOnLongClick(View v,NewsItem newsItem){
			
			return false;
		}
		protected void onActivityResult(int requestCode,int resultCode,Intent data){
			
		}
	}
	private void openDialog(NewsItem newsItem){
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_news);
		/*TextView author = (TextView)dialog.findViewById(R.id.dialog_news_author);
		TextView guid = (TextView)dialog.findViewById(R.id.dialog_news_guid);
		TextView link = (TextView)dialog.findViewById(R.id.dialog_news_link);*/
		TextView description = (TextView)dialog.findViewById(R.id.dialog_news_description);
		TextView pubDate = (TextView)dialog.findViewById(R.id.dialog_news_pub_date);
		TextView title = (TextView)dialog.findViewById(R.id.dialog_news_title);
		/*if(author!=null){
			author.setText(newsItem.author);
		}
		if(guid!=null){
			guid.setText(newsItem.guid);
		}
		if(link!=null){
			link.setText(newsItem.link);
		}*/
		if(description!=null){
			description.setText(newsItem.description);
		}
		if(pubDate!=null){
			pubDate.setText(newsItem.pubDate);
		}
		if(title!=null){
			title.setText(newsItem.title);
		}
		Button ok = (Button)dialog.findViewById(R.id.dialog_news_ok);
		if(ok!=null){
			ok.setOnClickListener(new OnClickListener(){
				
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}
	    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    lp.copyFrom(dialog.getWindow().getAttributes());

	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    //lp.height = ;
		    lp.y = Gravity.TOP;
		    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);  
			dialog.show();
		dialog.getWindow().setAttributes(lp);
	}
}