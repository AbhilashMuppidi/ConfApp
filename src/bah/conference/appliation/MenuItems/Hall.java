package bah.conference.appliation.MenuItems;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import bah.conference.appliation.R;
import bah.conference.appliation.database.quick.Bottom_Menu;

public class Hall extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exhibition_hall);

        Bottom_Menu.initialize(this);
	}
	public void open(Activity activity) {
		Intent intent = new Intent(activity,Hall.class);
		activity.startActivity(intent);
	}
}
