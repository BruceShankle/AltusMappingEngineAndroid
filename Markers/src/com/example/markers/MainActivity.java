package com.example.markers;

import us.ba3.me.*;
import us.ba3.me.markers.*;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.res.AssetManager;
import android.graphics.*;
import java.io.*;
import java.util.*;
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Get the map view and add a street map.
		MapView mapView = (MapView)this.findViewById(R.id.mapView1);
		mapView.addInternetMap("MapQuest Streets","http://otile1.mqcdn.com/tiles/1.0.0/osm");
		
		//Add dynamic marker map layer
		DynamicMarkerMapInfo mapInfo = new DynamicMarkerMapInfo();
		mapInfo.name = "Markers";
		mapInfo.zOrder = 2;
		mapView.addMapUsingMapInfo(mapInfo);
		
		//Add a marker
		DynamicMarker marker = new DynamicMarker();
		marker.name = "marker1";
		marker.setImage(loadBitmap("bluedot.png"));
		marker.anchorPoint = new PointF(16,16);
		marker.location.longitude = -78.6389;
		marker.location.latitude = 35.7719;
		mapView.addDynamicMarkerToMap("Markers", marker);
	}

	//Helper function to load images
	public Bitmap loadBitmap(String assetName) {
		AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(assetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
