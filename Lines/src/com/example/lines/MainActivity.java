package com.example.lines;

import java.io.InputStream;
import java.util.ArrayList;

import us.ba3.me.*;
import us.ba3.me.markers.*;
import us.ba3.me.styles.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.*;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Get the map view and add a street map.
		MapView mapView = (MapView)this.findViewById(R.id.mapView1);
		
		mapView.addInternetMap("MapQuest",
				"http://otile1.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.jpg",
				"", 		//Subdomains
				19,			//Max Level
				2,			//zOrder
				3,			//Number of simultaneous downloads
				true,		//Use cache
				false		//No alpha
				);
		
		//Add vector layer
		VectorMapInfo vectorMapInfo = new VectorMapInfo();
		vectorMapInfo.name = "route";
		vectorMapInfo.zOrder = 3;
		vectorMapInfo.alpha = 0.75f;
		mapView.addMapUsingMapInfo(vectorMapInfo);
		mapView.setTesselationThresholdForMap("route", 10);

		//Create a style for the line
		LineStyle lineStyle = new LineStyle();
		lineStyle.outlineColor = Color.GREEN;
		lineStyle.outlineWidth = 3;
		lineStyle.strokeColor = Color.YELLOW;
		lineStyle.strokeWidth = 9;

		//Get the route points
		Location[] routePoints = createRoutePoints();

		//Add the line to the vector layer
		mapView.addLineToVectorMap("route", routePoints, lineStyle);

		//Add markers
		this.addMarkers(mapView, routePoints);

		//Zoom in to the route
		mapView.setLocationThatFitsCoordinates(
				new Location(35.77222179527386,-78.64227338056882),
				new Location(35.77832583463119,-78.63659407693116),
				150, 150, 1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//Helper function to add markers to route end points
	public void addMarkers(MapView mapView, Location[] routePoints){
		//Add dynamic marker map layer
		DynamicMarkerMapInfo mapInfo = new DynamicMarkerMapInfo();
		mapInfo.name = "Markers";
		mapInfo.zOrder = 5;
		mapView.addMapUsingMapInfo(mapInfo);

		//Add a markers
		DynamicMarker startPoint = new DynamicMarker();
		startPoint.name = "startPoint";
		startPoint.setImage(loadBitmap("bluedot.png"), false);
		startPoint.anchorPoint = new PointF(16,16);
		startPoint.location = routePoints[0];
		mapView.addDynamicMarkerToMap("Markers", startPoint);

		DynamicMarker endPoint = new DynamicMarker();
		endPoint.name = "endPoint";
		endPoint.setImage(loadBitmap("greendot.png"), false);
		endPoint.anchorPoint = new PointF(16,16);
		endPoint.location = routePoints[routePoints.length-1];
		mapView.addDynamicMarkerToMap("Markers", endPoint);
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

	//Helper function to create an array of points to represent a route.
	public Location[] createRoutePoints(){
		ArrayList<Location> locations = new ArrayList<Location>();
		locations.add(new Location(35.77222179527386,-78.64227338056882));
		locations.add(new Location(35.77300435847386,-78.64211160843469));
		locations.add(new Location(35.77431930112859,-78.64204765649093));
		locations.add(new Location(35.77567608136691,-78.64196220184964));
		locations.add(new Location(35.77700395100487,-78.64187508617212));
		locations.add(new Location(35.77832583463119,-78.64183947388348));
		locations.add(new Location(35.77827906552807,-78.64020036607604));
		locations.add(new Location(35.77818133831267,-78.63823940725123));
		locations.add(new Location(35.7781182250745,-78.63659407693116));
		locations.add(new Location(35.77684002315468,-78.63668612449102));
		locations.add(new Location(35.77552738790911,-78.63672620342922));
		Location[] locationArray = locations.toArray(new Location[locations.size()]);
		return locationArray;
	}

}
