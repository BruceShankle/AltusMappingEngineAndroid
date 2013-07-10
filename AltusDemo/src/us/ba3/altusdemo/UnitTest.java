package us.ba3.altusdemo;
import us.ba3.me.*;
import us.ba3.me.markers.DynamicMarker;
import us.ba3.me.markers.DynamicMarkerMapInfo;
import us.ba3.me.styles.LabelStyle;
import us.ba3.me.util.FontUtil;
import android.os.Handler;

public class UnitTest extends METest {

	String mapPath;
	CoordinateBounds bounds;
	float alpha;
	int zOrder;
	int timerCount;
	
	public UnitTest(String name, String mapPath, CoordinateBounds bounds) {
		this(name, mapPath, bounds, 1.0f, 100);
	}
	
	public UnitTest(String name, String mapPath, CoordinateBounds bounds, float alpha) {
		this(name, mapPath, bounds, alpha, 100);
	}
	
	public UnitTest(String name, String mapPath, CoordinateBounds bounds, float alpha, int zOrder) {
		this.name = name;
		this.mapPath = mapPath;
		this.bounds = bounds;
		this.alpha = alpha;
		this.zOrder = zOrder;
	}

	@Override
	public boolean requiresDownloadableAssets() {
		return true;
	}
	
	public DynamicMarker CreateMarker(String name, Location location) {
		DynamicMarker marker = new DynamicMarker();
		marker.name = name;
		marker.location = location;
		marker.setImage(FontUtil.createLabel(marker.name, new LabelStyle()), false);
		return marker;
	}
	
	void TestDyanmicMarkers() {
    	//Add dynamic marker layer
		DynamicMarkerMapInfo mapInfo = new DynamicMarkerMapInfo();
		mapInfo.zOrder = 2000;
		mapInfo.name = "marker";
		mapInfo.hitTestingEnabled = false;
		
		mapView.addMapUsingMapInfo(mapInfo);
		mapView.addDynamicMarkerToMap(mapInfo.name, CreateMarker("1", new Location(40, -100)));
		mapView.addDynamicMarkerToMap(mapInfo.name, CreateMarker("2", new Location(40, -103)));
		mapView.addDynamicMarkerToMap(mapInfo.name, CreateMarker("3", new Location(40, -105)));
		mapView.addDynamicMarkerToMap(mapInfo.name, CreateMarker("4", new Location(40, -107)));
		mapView.addDynamicMarkerToMap(mapInfo.name, CreateMarker("5", new Location(40, -109)));
		mapView.addDynamicMarkerToMap(mapInfo.name, CreateMarker("6", new Location(40, -111)));
		mapView.addDynamicMarkerToMap(mapInfo.name, CreateMarker("7", new Location(40, -113)));
		mapView.addDynamicMarkerToMap(mapInfo.name, CreateMarker("8", new Location(40, -115)));
	}
	
	void EnableMarkers() {
		double angle = timerCount*10;
		if (angle >= 360)
			angle = 0;
		
//		mapView.showDynamicMarker("marker", "1");
//		mapView.addDynamicMarkerToMap("marker", CreateMarker("2", new Location(40, -103)));
//		mapView.setDynamicMarkerImage("marker", "3", FontUtil.createLabel("3a", new LabelStyle()));
//		mapView.setDynamicMarkerAnchorPoint("marker", "4", new PointF(0,15));
//		mapView.setDynamicMarkerRotation("marker", "5", angle, 2.0);
//		mapView.setDynamicMarkerRotation("marker", "6", angle, 2.0);
//		mapView.setDynamicMarkerAnchorPoint("marker", "6", new PointF(0,20));
//		mapView.setDynamicMarkerOffset("marker", "7", new PointF(0,15));
//		mapView.setDynamicMarkerAnchorPoint("marker", "7", new PointF(0,0));
//		mapView.setDynamicMarkerRotation("marker", "8", angle, 2.0);
//		mapView.setDynamicMarkerOffset("marker", "8", new PointF(0,50));
		
//		mapView.setMaxTileRenderSize(512);
		
		//mapView.setTileLevelBias(1.0f);
	}
	
	void DisableMarkers() {
//		mapView.hideDynamicMarker("marker", "1");
//		mapView.removeDynamicMarkerFromMap("marker", "2");
//		mapView.setDynamicMarkerImage("marker", "3", FontUtil.createLabel("3b", new LabelStyle()));
//		mapView.setDynamicMarkerAnchorPoint("marker", "4", new PointF(0,-15));
//		mapView.setDynamicMarkerAnchorPoint("marker", "6", new PointF(0,-20));
//		mapView.setDynamicMarkerAnchorPoint("marker", "7", new PointF(0,10));
//		mapView.setMaxTileRenderSize(190);
		//mapView.setTileLevelBias(0);
	}
	
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() 
	{
	    public void run() 
	    {
	    	++timerCount;
	    	
	    	if (timerCount%2 == 1) {
	    		EnableMarkers();
	    	} else {
	    		DisableMarkers();
	    	}

	         handler.postDelayed(this, 1000);
	    }
	};
	
	@Override
	protected void start() {
		
		runnable.run();
		
		// add map
//        mapView.addMap(name, mapPath + ".sqlite", mapPath + ".map", true);
//        mapView.setMapAlpha(name, alpha);
//        mapView.setMapZOrder(name, zOrder);
//        		
		mapView.addInternetMap(name, "http://a.tiles.mapbox.com/v3/examples.map-4l7djmvo");
		
        TestDyanmicMarkers();
        
		mapView.setPanAcceleration(-0.5f);
		//mapView.setPanVelocityScale(2);
		//mapView.setP
		mapView.setMaxVirtualMapParentSearchDepth(1);
        // zoom to bounds
        if (bounds != null) {
        		mapView.setLocationThatFitsCoordinates(bounds.min, bounds.max, 0, 0, 0.5);
        }
	}

	@Override
	public void stop() {
		mapView.removeMap(name, true);
		mapView.removeMap("marker", true);
		mapView.setMaxTileRenderSize(380);
		mapView.setTileLevelBias(1);
		mapView.setPanVelocityScale(1);
		mapView.setPanAcceleration(-10.0f);
		mapView.setMaxVirtualMapParentSearchDepth(5);
	}

}