package com.example.drive;

import java.io.InputStream;
import java.util.ArrayList;

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
import android.animation.*;
import android.view.animation.*;

public class MainActivity extends Activity implements ValueAnimator.AnimatorUpdateListener {

	ValueAnimator animator;
	PointF[] routePoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Get the map view and add a street map.
		MapView mapView = (MapView)this.findViewById(R.id.mapView1);
		mapView.addInternetMap("MapQuest Streets","http://otile1.mqcdn.com/tiles/1.0.0/osm");

		//Add vector layer
		VectorMapInfo vectorMapInfo = new VectorMapInfo();
		vectorMapInfo.name = "route";
		vectorMapInfo.zOrder = 2;
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
		mapView.lookAtCoordinates(new Location(35.77222179527386,-78.64227338056882),
				new Location(35.77832583463119,-78.63659407693116), 150, 150, 1);

		//Create a value animator
		animator = ValueAnimator.ofInt(0, routePoints.length-1);
		animator.setInterpolator(new LinearInterpolator());
		animator.setDuration(120000);
		animator.addUpdateListener(this);
		animator.setRepeatCount(ValueAnimator.REVERSE);
		animator.setStartDelay(0);
		animator.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onAnimationUpdate (ValueAnimator animation) {
		int index = (Integer)animation.getAnimatedValue();

		//Get the map view
		MapView mapView = (MapView)this.findViewById(R.id.mapView1);

		//Create a location object
		Location location = new Location(routePoints[index].y, routePoints[index].x);

		//Set heading
		double heading = 0;
		if(index==0){
			mapView.setDynamicMarkerRotation("Vehicles", "car1", heading, 0);
		}
		if(index==6){
			heading = 90;
			mapView.setDynamicMarkerRotation("Vehicles", "car1", heading, 1);
		}
		if(index==9){
			heading = 180;
			mapView.setDynamicMarkerRotation("Vehicles", "car1", heading, 1);
		}

		//Determine animation duration
		double animationDuration = 5;
		if(index==0) {
			animationDuration = 0;
		}
		
		//Update location of vehicle marker and beacon
		mapView.setDynamicMarkerLocation("Vehicles", "car1", location, animationDuration);
		mapView.updateAnimatedVectorCircleLocation("beacon", location, animationDuration);
	}

	public void addBeacon(MapView mapView, PointF point) {
		//Add animated vector circle
		AnimatedVectorCircle c = new AnimatedVectorCircle();
		c.name = "beacon";
		c.location = new Location(point.y, point.x);
		c.minRadius = 5;
		c.maxRadius = 75;
		c.animationDuration = 2.5f;
		c.repeatDelay = 0;
		c.fade = true;
		c.fadeDelay = 1;
		c.zOrder = 4;
		c.lineStyle.strokeColor = Color.WHITE;
		c.lineStyle.outlineColor = Color.rgb(0, 255, 0);
		c.lineStyle.outlineWidth = 4;
		mapView.addAnimatedVectorCircle(c);
	}

	public void addVehicleMarker(MapView mapView, PointF point) {

		//Add dynamic marker map layer
		DynamicMarkerMapInfo mapInfo = new DynamicMarkerMapInfo();
		mapInfo.name = "Vehicles";
		mapInfo.zOrder = 5;
		mapView.addMapUsingMapInfo(mapInfo);

		//Add a marker
		DynamicMarker vehicle = new DynamicMarker();
		vehicle.name = "car1";
		vehicle.setImage(loadBitmap("map-arrow.png"));
		vehicle.anchorPoint = new PointF(16,16);
		vehicle.location.longitude = point.x;
		vehicle.location.latitude = point.y;
		mapView.addDynamicMarkerToMap("Vehicles", vehicle);
	}

	//Helper function to add markers to route end points
	public void addMarkers(MapView mapView, PointF[] routePoints){
		//Add dynamic marker map layer
		DynamicMarkerMapInfo mapInfo = new DynamicMarkerMapInfo();
		mapInfo.name = "Markers";
		mapInfo.zOrder = 3;
		mapView.addMapUsingMapInfo(mapInfo);

		//Add a markers
		DynamicMarker startPoint = new DynamicMarker();
		startPoint.name = "startPoint";
		startPoint.setImage(loadBitmap("bluedot.png"));
		startPoint.anchorPoint = new PointF(16,16);
		startPoint.location.longitude = routePoints[0].x;
		startPoint.location.latitude = routePoints[0].y;
		mapView.addDynamicMarkerToMap("Markers", startPoint);

		DynamicMarker endPoint = new DynamicMarker();
		endPoint.name = "endPoint";
		endPoint.setImage(loadBitmap("greendot.png"));
		endPoint.anchorPoint = new PointF(16,16);
		endPoint.location.longitude = routePoints[routePoints.length-1].x;
		endPoint.location.latitude = routePoints[routePoints.length-1].y;
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
	public PointF[] createRoutePoints(){
		ArrayList<PointF> points = new ArrayList<PointF>();
		points.add(new PointF(-78.64227338056882f,35.77222179527386f));
		points.add(new PointF(-78.64211160843469f,35.77300435847386f));
		points.add(new PointF(-78.64204765649093f,35.77431930112859f));
		points.add(new PointF(-78.64196220184964f,35.77567608136691f));
		points.add(new PointF(-78.64187508617212f,35.77700395100487f));
		points.add(new PointF(-78.64183947388348f,35.77832583463119f));
		points.add(new PointF(-78.64020036607604f,35.77827906552807f));
		points.add(new PointF(-78.63823940725123f,35.77818133831267f));
		points.add(new PointF(-78.63659407693116f,35.7781182250745f));
		points.add(new PointF(-78.63668612449102f,35.77684002315468f));
		points.add(new PointF(-78.63672620342922f,35.77552738790911f));
		PointF[] pointArray = points.toArray(new PointF[points.size()]);
		return pointArray;
	}

}
