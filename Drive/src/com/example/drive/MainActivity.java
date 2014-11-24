package com.example.drive;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import us.ba3.me.*;
import us.ba3.me.markers.*;
import us.ba3.me.styles.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.Menu;


public class MainActivity extends Activity implements Runnable {

	Location[] routePoints; //Points along the simulated route
	int routeIndex = 0; //Index into the rotue
	protected ScheduledThreadPoolExecutor threadPool; //Timer threadpool
	double interval = 1.0; //timer interval in seconds

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

		//		//Add vector layer
		VectorMapInfo vectorMapInfo = new VectorMapInfo();
		vectorMapInfo.name = "route";
		vectorMapInfo.zOrder = 3;
		vectorMapInfo.alpha = 0.75f;
		mapView.addMapUsingMapInfo(vectorMapInfo);

		//Create a style for the line
		LineStyle lineStyle = new LineStyle();
		lineStyle.outlineColor = Color.GREEN;
		lineStyle.outlineWidth = 3;
		lineStyle.strokeColor = Color.YELLOW;
		lineStyle.strokeWidth = 9;

		//Get the route points
		routePoints = createRoutePoints();

		//Add the line to the vector layer
		mapView.addLineToVectorMap("route", routePoints, lineStyle);

		//Add route end-point markers
		addMarkers(mapView, routePoints);

		//Add a vehicle marker
		addVehicleMarker(mapView, routePoints[0]);

		//Add a beacon to highlight the vehicle locaiton
		addBeacon(mapView, routePoints[0]);

		//Zoom in to the route
		mapView.setLocationThatFitsCoordinates(
				new Location(35.77222179527386,-78.64227338056882),
				new Location(35.77832583463119,-78.63659407693116),
				150,150,1);


		//Start timer
		startTimer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void startTimer(){
		threadPool = new ScheduledThreadPoolExecutor(1);
		int millis = (int)(this.interval * 1000);
		threadPool.scheduleAtFixedRate(this, 0, millis, TimeUnit.MILLISECONDS);
	}

	public void run() {
		timerTick();
	}

	public void timerTick() {

		//Increment / reset route index
		routeIndex++;
		if(routeIndex==this.routePoints.length){
			routeIndex=0;
		}

		//Get the map view
		MapView mapView = (MapView)this.findViewById(R.id.mapView1);

		//Set heading
		double heading = 0;
		if(routeIndex==0){
			mapView.setDynamicMarkerRotation("Vehicles", "car1", heading, 0);
		}
		if(routeIndex==6){
			heading = 90;
			mapView.setDynamicMarkerRotation("Vehicles", "car1", heading, this.interval/4);
		}
		if(routeIndex==9){
			heading = 180;
			mapView.setDynamicMarkerRotation("Vehicles", "car1", heading, this.interval/4);
		}

		//Determine animation duration
		double animationDuration = this.interval;
		if(routeIndex==0) {
			animationDuration = 0;
		}

		//Update location of vehicle marker and beacon
		mapView.setDynamicMarkerLocation("Vehicles", "car1", routePoints[routeIndex], animationDuration);
		mapView.setHaloPulseLocation("beacon", routePoints[routeIndex], animationDuration);
	}

	public void addBeacon(MapView mapView, Location location) {
		//Add animated vector circle
		HaloPulse beacon = new HaloPulse();
		beacon.name = "beacon";
		beacon.location = location;
		beacon.minRadius = 5;
		beacon.maxRadius = 75;
		beacon.animationDuration = 2.5f;
		beacon.repeatDelay = 0;
		beacon.fade = true;
		beacon.fadeDelay = 1;
		beacon.zOrder = 5;
		beacon.lineStyle.strokeColor = Color.WHITE;
		beacon.lineStyle.outlineColor = Color.rgb(0, 255, 0);
		beacon.lineStyle.outlineWidth = 4;
		mapView.addHaloPulse(beacon);
	}

	public void addVehicleMarker(MapView mapView, Location location) {

		//Add dynamic marker map layer
		DynamicMarkerMapInfo mapInfo = new DynamicMarkerMapInfo();
		mapInfo.name = "Vehicles";
		mapInfo.zOrder = 5;
		mapView.addMapUsingMapInfo(mapInfo);

		//Add a marker
		DynamicMarker vehicle = new DynamicMarker();
		vehicle.name = "car1";
		vehicle.setImage(loadBitmap("map-arrow.png"), false);
		vehicle.anchorPoint = new PointF(16,16);
		vehicle.location = location;
		mapView.addDynamicMarkerToMap("Vehicles", vehicle);
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
