package us.ba3.altusdemo.markertests;
import java.util.ArrayList;
import java.util.List;

import us.ba3.altusdemo.METest;
import us.ba3.me.Location;
import us.ba3.me.MapType;
import us.ba3.me.markers.MarkerImageLoadingStrategy;
import us.ba3.me.markers.MarkerInfo;
import us.ba3.me.markers.MarkerMapDelegate;
import us.ba3.me.markers.MarkerMapInfo;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

public class InMemoryClusteredMarkersTest extends METest implements MarkerMapDelegate {

	PointF anchorPoint;
	
	public InMemoryClusteredMarkersTest(String name){
		this.name=name;	
	}
	
	@Override
	public boolean requiresDownloadableAssets() {
		return false;
	}
	
	@Override
	public void start() {
		
		//Create some markers to cluster
		List<MarkerInfo> markers = new ArrayList<MarkerInfo>();
		int uid = 1;
		for (double lon = -125; lon < -65; lon++ ) {
			for (double lat = 25; lat < 50; lat++){
				MarkerInfo markerInfo = new MarkerInfo();
				markerInfo.metaData = "Marker " + uid;
				markerInfo.location = new Location(lat,lon);
				markerInfo.weight = 1.0f;
				markerInfo.minimumLevel = 0;
				markers.add(markerInfo);
				uid++;
			}
		}

		//Add in-memory clustered marker map
		MarkerMapInfo mapInfo = new MarkerMapInfo();
		mapInfo.mapType = MapType.kMapTypeMemoryMarker;
		mapInfo.name = this.name;
		mapInfo.zOrder = 200;
		mapInfo.clusterDistance = 30;
		mapInfo.markerMapDelegate = this;
		mapInfo.hitTestingEnabled = true;
		mapInfo.fadeEnabled = false;
		mapInfo.markerImageLoadingStrategy = MarkerImageLoadingStrategy.kMarkerImageLoadingAsynchronous;
		mapInfo.markers = markers.toArray(new MarkerInfo[markers.size()]);
		mapInfo.maxLevel = 18;
		//Cache the image we will use for the marker
		Bitmap blueDot = this.getBitmapFromAsset("images/bluedot@2x.png");
		if(blueDot!=null){
			mapView.addCachedImage("bluedot", blueDot, false);
			this.anchorPoint = new PointF(blueDot.getWidth()/2, blueDot.getHeight()/2);
		}
		
		//Add the marker map
		mapView.addMapUsingMapInfo(mapInfo);
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}

	@Override
	public void updateMarkerInfo(MarkerInfo markerInfo, String mapName) {
		//Set the marker info
		markerInfo.cachedImageName = "bluedot";
		markerInfo.anchorPoint = this.anchorPoint;
	}
	
	@Override
	public void tapOnMarker(	String mapName,
			int markerUid,
			String markerMetaData,
			float markerWeight,
			PointF geographicLocation,
			PointF screenPoint,
			PointF markerPoint){
		
		Log.w("InMemoryClusteredMarkersTest","Marker was tapped on. uid:" + markerUid + 
				" metaData:" + markerMetaData + 
				" weight:" + markerWeight +
				" location:" + geographicLocation.x + "," + geographicLocation.y +
				" screenPoint:" + screenPoint.x + "," + screenPoint.y +
				" markerPoint:" + markerPoint.x + "," + markerPoint.y);
		
		
	}
	
}