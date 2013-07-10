package us.ba3.altusdemo;
import us.ba3.me.*;


public class MBTilesMapTest extends RasterMapTest {

	public MBTilesMapTest(String name, String mapPath, CoordinateBounds bounds) {
		super(name, mapPath, bounds);
	}
	
	@Override
	protected void start() {
		
		//Add map
		mapView.addMBTilesMap(name, mapPath, "grayGrid", ImageDataType.kImageDataTypePNG, false, zOrder);
		mapView.setMapAlpha(name, alpha);
		
        // zoom to bounds
        if (bounds != null) {
        		mapView.setLocationThatFitsCoordinates(bounds.min, bounds.max, 0, 0, 0.5);
        }
	}
	
}
