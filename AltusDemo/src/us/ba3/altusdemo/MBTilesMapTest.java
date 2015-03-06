package us.ba3.altusdemo;
import us.ba3.me.CoordinateBounds;
import us.ba3.me.ImageDataType;
import us.ba3.me.MapLoadingStrategy;

public class MBTilesMapTest extends PackagedMapTest {

	public MBTilesMapTest(String name, String mapPath, CoordinateBounds bounds) {
		super(name, mapPath, bounds);
		
	}
	
	@Override
	public void start() {
		
		//Add map
		mapView.addMBTilesMap(name, mapPath, "grayGrid", ImageDataType.kImageDataTypePNG, false, zOrder, MapLoadingStrategy.kHighestDetailOnly);
		mapView.setMapAlpha(name, alpha);
		
        // zoom to bounds
        if (bounds != null) {
        		mapView.setLocationThatFitsCoordinates(bounds.min, bounds.max, 0, 0, 0.5);
        }
	}
	
}
