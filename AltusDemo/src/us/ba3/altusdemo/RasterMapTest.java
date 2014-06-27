package us.ba3.altusdemo;
import us.ba3.me.*;
public class RasterMapTest extends METest {

	String mapPath;
	CoordinateBounds bounds;
	float alpha;
	int zOrder;
	
	public RasterMapTest(String name, String mapPath, CoordinateBounds bounds) {
		this(name, mapPath, bounds, 1.0f, 2);
	}
	
	public RasterMapTest(String name, String mapPath, CoordinateBounds bounds, float alpha) {
		this(name, mapPath, bounds, alpha, 2);
	}
	
	public RasterMapTest(String name, String mapPath, CoordinateBounds bounds, float alpha, int zOrder) {
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
        mapView.addMap(name, mapPath + ".sqlite", mapPath + ".map", true);
        mapView.setMapAlpha(name, alpha);
        mapView.setMapZOrder(name, zOrder);
        
        // zoom to bounds
        if (bounds != null) {
        		mapView.setLocationThatFitsCoordinates(bounds.min, bounds.max, 0, 0, 0.5);
        }
	}

	@Override
	public void stop() {
		mapView.removeMap(name, true);
	}

}
