package us.ba3.altusdemo;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import android.graphics.Color;
import android.util.Log;
import us.ba3.me.*;

public class DynamicTerrainColoring extends METest {

	String mapPath;
	CoordinateBounds bounds;
	float currentAltitude = 0;
	float altitudeDelta = 100;
	
	public DynamicTerrainColoring(String name, String mapPath, CoordinateBounds bounds) {
		this.name = name;
		this.mapPath = mapPath;
		this.bounds = bounds;
	}

	@Override
	public boolean requiresDownloadableAssets() {
		return true;
	}

	void addNormalTerrainMap(){
		ColorBar colorBar = new ColorBar();
		colorBar.addColor(0, 0xff4e7a5f);
		colorBar.addColor(50, 0xff5c8563);
		colorBar.addColor(200, 0xff6f8d6d);
		colorBar.addColor(600, 0xff9a9874);
		colorBar.addColor(1000, 0xffa1a094);
		colorBar.addColor(2000, 0xffcab9a6);
		colorBar.addColor(3000, 0xffc6947c);
		colorBar.addColor(4000, 0xff957b66);
		colorBar.addColor(5000, 0xffcab9a6);
		colorBar.addColor(6000, 0xffd8e0ed);
		colorBar.addColor(7000, 0xffecf1f8);
		mapView.setTerrainColorBar(colorBar, 0xff005f99);

		// add map
		mapView.addMap(name, mapPath + ".sqlite", mapPath + ".map", true);
		mapView.setMapZOrder(name, 2);
	}

	//Add a dynamically color terrain map such
	//that if the dynamic color bar offset is within
	//100 meters, the terrain is colored red,
	//100.00001 to 1000 meters, the terrain is colored yellow
	//> 1000 meters, teh terrain is transparent
	void addDynamicTerrainMap(){
		//Create color bar for dynamic terrain
		ColorBar colorBar = new ColorBar();
		colorBar.addColor(-1000.00001, Color.TRANSPARENT);
		colorBar.addColor(-1000, Color.YELLOW);
		colorBar.addColor(-100.00001, Color.YELLOW);
		colorBar.addColor(-100, Color.RED);
		mapView.setDynamicTerrainColorBar(colorBar);

		//Add terrain map
		MapInfo mapInfo = new MapInfo();
		mapInfo.mapType = MapType.kMapTypeFileTerrainTAWS;
		mapInfo.sqliteFileName = this.mapPath + ".sqlite";
		mapInfo.dataFileName = this.mapPath + ".map";
		mapInfo.name = "TAWS";
		mapInfo.zOrder = 3;
		mapInfo.compressTextures = false;
		mapView.addMapUsingMapInfo(mapInfo);
	}


	@Override
	public void start() {
		this.addNormalTerrainMap();
		this.addDynamicTerrainMap();
		this.interval = 0.1f;
		this.startTimer();
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, true);
		mapView.removeMap("TAWS", true);
		//this.stopTimer();
	}
	
	@Override
	public void timerTick(){

		//Adjust altitude up or down
		this.currentAltitude = this.currentAltitude + this.altitudeDelta;

		if(this.currentAltitude > 5000 || this.currentAltitude < 0){
			this.altitudeDelta = -this.altitudeDelta;
		}
		if(this.currentAltitude < 0 ){
			this.currentAltitude = 0;
		}

		//Set dynamic color
		mapView.setDynamicColorBarOffset(this.currentAltitude);
	}

	

}
