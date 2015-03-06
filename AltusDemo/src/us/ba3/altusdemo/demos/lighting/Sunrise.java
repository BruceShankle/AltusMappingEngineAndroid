package us.ba3.altusdemo.demos.lighting;

import android.content.Context;
import android.graphics.PointF;
import android.location.Location;
import android.view.ViewGroup;
import us.ba3.altusdemo.demos.DemoWithTimer;
import us.ba3.me.LightingType;
import us.ba3.me.Location3D;
import us.ba3.me.LocationType;
import us.ba3.me.MapView;

public class Sunrise extends DemoWithTimer{
	
	private double sunLongitude;
	private double sunLatitude;
	
	public Sunrise(){
		this.name="Sunrise";
	}
	
	@Override
	public void start(MapView mapView, Context context, ViewGroup viewGroup) {
		this.mapView = mapView;
		this.context = context;
		this.viewGroup = viewGroup;
		sunLatitude = 0;
	    sunLongitude = -20;
	    mapView.setCameraOrientation(100, 0, -5, 0);
	    mapView.setLocation3D(new Location3D( 46.858, -122.157, 3000), 0);
	    mapView.setStarsEnabled(true);
	    mapView.setSunImageEnabled(true);
	    addTerrain3D();
	    this.interval = 0.5f;
	    super.start(mapView, context, viewGroup);
	}
	
	public void stop(){
		mapView.setCameraOrientation(0, 0, 90, 0);
		mapView.removeMap("Terrain 3D", true);
		mapView.removeMap("TerrainHeight", true);
		mapView.removeMap("MapBox Satellite", true);
		super.stop();
	}
	
	public void addTerrain3D(){
		
		//mapView.removeAllMaps(true);
		mapView.removeMap("Terrain", true);
		String terrainMapPath = getMapPath("PackagedMaps","TerrainPng");
		mapView.addTerrainHeightMap("TerrainHeight", terrainMapPath + ".sqlite");
		mapView.setMapZOrder("TerrainHeight", 110);
		mapView.addTerrainMeshMap("Terrain 3D", "TerrainHeight", 32);
		mapView.setMapAlpha("Terrain 3D", 0.3f);
		mapView.setMapZOrder("Terrain 3D", 109);
		mapView.setTileDistanceScale(2.0);
		mapView.setTileLevelBias(0);
		mapView.setDisplayListMode(0);
		mapView.setLightingType(LightingType.kLightingTypeRealistic);
		mapView.addStreamingRasterMap("MapBox Satellite",
				"http://{s}.tiles.mapbox.com/v3/dxjacob.ho6k3ag9/{z}/{x}/{y}.jpg",
				"", //North tiles
				"", //South tiles
				"a,b,c,d", 	//Subdomains
				20,			//Max Level
				2,			//zOrder
				3,			//Number of simultaneous downloads
				true,		//Use cache
				false		//No alpha
				);
	}
	
	public void updateSun(){
		mapView.setSunLocation(new us.ba3.me.Location(sunLatitude, sunLongitude),
				LocationType.kLocationTypeAbsolute);
		sunLongitude = sunLongitude - 0.5;
		if (sunLongitude<=-180){
	        sunLongitude = -1;
	    }
	}
	
	public void timerTick(){
		updateSun();
	}

}
