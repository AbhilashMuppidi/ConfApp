package bah.conference.appliation.MenuItems;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import bah.conference.appliation.R;
import bah.conference.appliation.dataStructures.BoothLocation;
import bah.conference.appliation.dataStructures.Corner;
import bah.conference.appliation.database.quick.Bottom_Menu;

public class SymposiumLocation extends MapActivity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
		
		MapView mapView = (MapView) findViewById(R.id.mapView);
		gaylord a = new gaylord(this.getResources().getDrawable(R.drawable.app_icon));

		GeoPoint gp = new GeoPoint((int)(-81.525261 * 1E6), (int)(28.343628 * 1E6));
		a.addOverlay(new OverlayItem(gp, "Gaylord Palms Resort and Convention Center", "This is the location of GEOINT 2012"));
		
		mapView.getController().animateTo(gp);
		mapView.getController().setZoom(19);
		mapView.getOverlays().add(a);
		
        Bottom_Menu.initialize(this);
	}

	 private class gaylord extends com.google.android.maps.ItemizedOverlay<OverlayItem>{

		@Override
		protected boolean onTap(int index) {
			AlertDialog.Builder alert = new AlertDialog.Builder(SymposiumLocation.this);

			alert.setTitle("Gaylord Palms Resort and Convention Center");
			alert.setMessage("Would you like to leave this applications and view directions to this location?");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Uri uri = Uri.parse("https://maps.google.com/maps?hl=en&ie=UTF-8&gl=us&daddr=gaylord+palms&saddr=current+location");
				Intent intent = new Intent(Intent.ACTION_VIEW,uri);
				startActivity(intent);
			  }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			  }
			});

			alert.show();
			
			return	super.onTap(index);
		}
		ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		public gaylord(Drawable defaultMarker) {
			super(defaultMarker);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}
		public void addOverlay(OverlayItem overlay){
			mOverlays.add(overlay);
		}

	    	
	 }
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	 
}
