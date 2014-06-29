package us.ba3.altusdemo;
import us.ba3.me.*;
import us.ba3.me.markers.DynamicMarker;
import us.ba3.me.markers.DynamicMarkerMapDelegate;
import us.ba3.me.markers.DynamicMarkerMapInfo;
import us.ba3.me.styles.LabelStyle;
import us.ba3.me.util.FontUtil;
import android.os.AsyncTask;
import android.util.Log;
import au.com.bytecode.opencsv.*;
import java.io.*;
import java.util.ArrayList;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;

class City {
	String name;
	ArrayList<Integer> population;
	Location location;
}

class CreateLabelTask extends AsyncTask<String, Void, String> {

	DynamicMarker marker;
	MapView mapView;
	String mapName;
	Bitmap labelBitmap;
	int population;
	
	public CreateLabelTask(DynamicMarker marker, MapView mapView, String mapName, int population) {
		this.mapView = mapView;
		this.marker = marker;
		this.mapName = mapName;
		this.population = population;
	}
	
    @Override
    protected String doInBackground(String... params) {
    	LabelStyle style = DynamicMarkerTest.GetStyleForPopulation(population);
    	String string = DynamicMarkerTest.FormatStringForPopulation(population);
		labelBitmap = FontUtil.createLabel(string, style);
		return null;
    }
    
    @Override public void onPostExecute(String result)
    {
    	// set bitmap
		marker.setImage(labelBitmap, false);
		marker.weight = population;
		marker.anchorPoint = new PointF(labelBitmap.getWidth()/2, labelBitmap.getHeight()/2);

		// don't show the marker for population 0
		if (population == 0) {
			mapView.hideDynamicMarker(mapName, marker.name);
		} else {
			mapView.addDynamicMarkerToMap(mapName, marker);
		}
  }
}

public class DynamicMarkerTest extends METest implements ValueAnimator.AnimatorUpdateListener, DynamicMarkerMapDelegate {

	String mapPath;
	CoordinateBounds bounds;
	float alpha;
	int zOrder;
	ArrayList<Integer> years;
	ArrayList<City> cities;
	String markerMapName;
	ValueAnimator animator;
	int yearIndex;
	
	public DynamicMarkerTest(String name, String mapPath, CoordinateBounds bounds) {
		this.name = name;
		this.mapPath = mapPath;
		this.bounds = bounds;
		this.alpha = 1.0f;
		this.zOrder = 10;
		this.markerMapName = "markers";
		this.yearIndex = 0;
	}

	@Override
	public boolean requiresDownloadableAssets() {
		return true;
	}
	
	void addMarkers(int timeIndex) {

		for (City city : cities) {
			DynamicMarker marker = new DynamicMarker();
			marker.name = city.name;
			marker.location = city.location;
			
		    CreateLabelTask task = new CreateLabelTask(marker, mapView, markerMapName, city.population.get(timeIndex));
		    task.execute();
		}
	}
	
	public void onAnimationUpdate (ValueAnimator animation) {
		int index = (Integer)animation.getAnimatedValue();
		if (index != yearIndex) {
			addMarkers(index);
			yearIndex = index;
		}
	}
	
	public static String FormatStringForPopulation(int population) {
		String string = ".";
		if (population > 1000) {
			string = String.format("%.1f", population/1000.0) + "M";
		} else if (population > 0) {
			string = Integer.toString(population) + "k";
		}
		return string;
	}
	
	public static LabelStyle GetStyleForPopulation(int population) {
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.fontName = "Arial";
		labelStyle.fontStyle = Typeface.BOLD;
		labelStyle.strokeVisible = true;
		labelStyle.strokeColor = Color.WHITE;
		labelStyle.strokeWidth = 4;
    	
		// pick font size and color based on population
    	if (population < 100) {
			labelStyle.fontSize = 24;
			labelStyle.fillColor = 0xffff8080;
    	}
		else if (population < 500) {
			labelStyle.fontSize = 30;
			labelStyle.fillColor = 0xffff6666;
		}
		else if (population < 1000) {
			labelStyle.fontSize = 40;
			labelStyle.fillColor = 0xffff4d4d;
		}
		else if (population < 5000) {
			labelStyle.fontSize = 60;
			labelStyle.fillColor = 0xffff3333;
		}
		else {
			labelStyle.fontSize = 90;
			labelStyle.fillColor = 0xffff0000;
		}

    	return labelStyle;
	}
	
	@Override
	public void start() {
				
		// add map
		mapView.addInternetMap(this.name,
				"http://{s}.tiles.mapbox.com/v3/dxjacob.map-s5qr595q/{z}/{x}/{y}.png",
				"a,b,c,d", 	//Subdomains
				20,			//Max Level
				2,			//zOrder
				3,			//Number of simultaneous downloads
				true,		//Use cache
				false		//No alpha
				);
		
    	//Add dynamic marker layer
		DynamicMarkerMapInfo mapInfo = new DynamicMarkerMapInfo();
		mapInfo.zOrder = 2000;
		mapInfo.name = markerMapName;
		mapInfo.hitTestingEnabled = true;
		mapInfo.delegate = this;
		mapView.addMapUsingMapInfo(mapInfo);

		// add markers
		ReadPopulationData();
		addMarkers(0);

		// animate the time 
		animator = ValueAnimator.ofInt(0, 14);
		animator.setDuration(30000);
		animator.addUpdateListener(this);
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.setStartDelay(1000);
		animator.start();
        
        // zoom to bounds
        if (bounds != null) {
        		mapView.setLocationThatFitsCoordinates(bounds.min, bounds.max, 0, 0, 0.5);
        }
	}

	@Override
	public void stop() {
		animator.end();
		mapView.removeMap(name, true);
		mapView.removeMap(markerMapName, true);
	}
	
	void ReadPopulationData() {
        years = new ArrayList<Integer>();
        cities = new ArrayList<City>();
        
        try {
        	InputStream stream = context.getAssets().open("DemoData/population_history.csv");
	        CSVReader reader = new CSVReader(new InputStreamReader(stream));
	        String [] nextLine = reader.readNext();
	        for (int i = 4; i < nextLine.length; ++i) {
	        	years.add(Integer.parseInt(nextLine[i]));
	        }
	       
	        while ((nextLine = reader.readNext()) != null) {
	        	City city = new City();
	        	city.name = nextLine[0];
	        	city.location = new Location(Float.parseFloat(nextLine[3]), Float.parseFloat(nextLine[2]));
	        	
	        	Log.w("", "location: " + city.location.longitude + ", " + city.location.latitude);
	        	city.population = new ArrayList<Integer>();
	        	for (int i = 4; i < nextLine.length; ++i) {
	        		int population = 0;
	        		String populationString = nextLine[i];
	        		if (!populationString.isEmpty())
	        			population = Integer.parseInt(populationString);
	        		
	        		city.population.add(population);
	        	}
	        	cities.add(city);
	        }
        } catch (IOException e) { }
	}
	
	@Override
	public void tapOnMarker(
			String mapName,
			String markerName,
			PointF screenPoint,
			PointF markerPoint) {
		
		Log.w("tapOnMarker", "map: " + mapName + ", marker: " + markerName + ", screenPoint: (" + screenPoint.x + ", " + screenPoint.y + "), markerPoint: (" + markerPoint.x + ", " + markerPoint.y + ")");
	}

}
