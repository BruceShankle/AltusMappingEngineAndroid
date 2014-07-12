package com.example.helloworldmap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import us.ba3.me.*;
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Get the map view and add a map.
		MapView mapView = (MapView)this.findViewById(R.id.mapView1);
		mapView.addInternetMap("MapQuest Aerial",
				"http://otile1.mqcdn.com/tiles/1.0.0/sat/{z}/{x}/{y}.jpg",
				"", 		//Subdomains
				20,			//Max Level
				2,			//zOrder
				3,			//Number of simultaneous downloads
				true,		//Use cache
				false		//No alpha
				);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
