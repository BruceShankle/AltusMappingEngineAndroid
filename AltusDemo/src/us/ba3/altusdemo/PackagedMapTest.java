package us.ba3.altusdemo;
import android.util.Log;
import us.ba3.me.*;
public class PackagedMapTest extends METest {

	String mapPath;
	CoordinateBounds bounds;
	float alpha;
	int zOrder;
	
	public PackagedMapTest(String name, String mapPath) {
		this(name, mapPath, null, 1.0f, 2);
	}
	
	public PackagedMapTest(String name, String mapPath, CoordinateBounds bounds) {
		this(name, mapPath, bounds, 1.0f, 2);
	}
	
	public PackagedMapTest(String name, String mapPath, CoordinateBounds bounds, float alpha) {
		this(name, mapPath, bounds, alpha, 2);
	}
	
	public PackagedMapTest(String name, String mapPath, CoordinateBounds bounds, float alpha, int zOrder) {
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
	
	@Override
	public void start() {
		
		// add map
		
			mapView.addPackagedMap(name, mapPath + ".sqlite");
	        if(alpha!=1.0){
	        		mapView.setMapAlpha(name, alpha);
	        }
	        mapView.setMapZOrder(name, zOrder);
		
        
        //Zoom to bounds
        if (bounds != null) {
        		mapView.setLocationThatFitsCoordinates(bounds.min, bounds.max, 0, 0, 0.5);
        }
	}

	@Override
	public void stop() {
		Log.w("Removing map", name);
		mapView.removeMap(name, true);
		
	}

}
