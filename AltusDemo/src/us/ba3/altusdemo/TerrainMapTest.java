package us.ba3.altusdemo;
import us.ba3.me.*;
public class TerrainMapTest extends METest {

	String mapPath;
	CoordinateBounds bounds;
	int zOrder;
	
	public TerrainMapTest(String name, String mapPath, CoordinateBounds bounds, int zOrder) {
		this.name = name;
		this.mapPath = mapPath;
		this.bounds = bounds;
		this.zOrder = zOrder;
	}
	
	public TerrainMapTest(String name, String mapPath, CoordinateBounds bounds) {
		this(name, mapPath, bounds, 2);
	}
	
	@Override
	public boolean requiresDownloadableAssets() {
		return true;
	}
	
	
	@Override
	public void start() {
	
		//Set terrain color bar
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
