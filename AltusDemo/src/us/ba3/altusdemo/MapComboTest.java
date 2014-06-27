package us.ba3.altusdemo;

public class MapComboTest extends METest {

	RasterMapTest vectorTest;
	RasterMapTest parkMapTest;
	
	public MapComboTest(METestManager testManager) {
		name = "Vector and Park";
		
		parkMapTest = new RasterMapTest("Park Map",
				testManager.getMapPath("National_Parks","Acadia"),
				Locations.AcadiaBounds, 0.5f, 101);
		
		vectorTest = new RasterMapTest("Vector World",
				testManager.getMapPath("WorldVector","World"),
				Locations.USBounds, 1.0f, 100);
	}
	
	@Override
	public boolean requiresDownloadableAssets() {
		if(vectorTest.requiresDownloadableAssets() ||
				parkMapTest.requiresDownloadableAssets()){
			return true;
		}
		return false;
	}
	
	@Override
	public void start() {
		vectorTest.start(mapView, context);
		parkMapTest.start(mapView, context);
	}

	@Override
	public void stop() {
		vectorTest.stop();
		parkMapTest.stop();
	}

}
