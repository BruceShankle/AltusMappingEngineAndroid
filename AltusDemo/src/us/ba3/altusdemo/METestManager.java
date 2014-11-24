package us.ba3.altusdemo;
import us.ba3.altusdemo.internetmaptests.*;
import us.ba3.altusdemo.markertests.*;
import java.io.File;
import java.util.ArrayList;
import us.ba3.me.*;
import android.content.Context;
import android.util.Log;
import us.ba3.altusdemo.tileworkertests.*;

class Locations {
	public static final CoordinateBounds USBounds = new CoordinateBounds(
			new Location(37.246798, -126.554263), 
			new Location(34.648367, -72.814862));

	public static final CoordinateBounds CharlotteBounds = new CoordinateBounds(
			new Location(34.804783, -81.765747), 
			new Location(35.762115, -80.175476));

	public static final CoordinateBounds AcadiaBounds = new CoordinateBounds(
			new Location(44.2206, -68.503876), 
			new Location(44.45633, -68.14682));
	
	public static final CoordinateBounds HoustonBounds = new CoordinateBounds(
			new Location(29.6084164, -95.5534003),
			new Location(29.940553, -95.185871));
	
	public static final CoordinateBounds DCBounds = new CoordinateBounds(
			new Location(38.848,-77.1127),
			new Location(38.933,-76.9665));
	
	public static final CoordinateBounds WindsBounds = new CoordinateBounds(
			new Location(-180,-85),
			new Location(180,85));
			
}

public class METestManager {
	private METest previousSelectedTest;
	private ArrayList<METest> testList;
	private MapView mapView;
	private Context context;
	private ArrayList<METest> runningTests;
	
	public METestManager(MapView mapView, Context context) {
		this.mapView = mapView;
		this.context = context;
		testList = new ArrayList<METest>();
		
		testList.add(new MapBoxSatelliteTest());
		testList.add(new MapQuestAerialTest());
		testList.add(new MapBoxLandcoverTest());
		testList.add(new MapQuestTest());
		testList.add(new BitmapTileWorkerTest());
		
		//testList.add(new VectorWorldMapTest("World Vector", getMapPath("NewVector","WorldVector"), Locations.USBounds, this));
		testList.add(new LocationAPITest("Location API"));
		testList.add(new VectorWorldMapTest("Vector Streets", getMapPath("NewVector","World_Style2"), Locations.HoustonBounds, this));
		testList.add(new RasterMapTest("Sectional", getMapPath("Sectional","Charlotte_North"), Locations.CharlotteBounds));
		testList.add(new RasterMapTest("Park Map", getMapPath("National_Parks","Acadia"), Locations.AcadiaBounds));
		testList.add(new TerrainMapTest("Terrain", getMapPath("BaseMap","Earth"), Locations.USBounds));
		testList.add(new DynamicTerrainColoring("Dynamic Terrain", getMapPath("BaseMap","Earth"), Locations.USBounds));
		testList.add(new MBTilesMapTest("MBTiles", getMapPath("MBTiles","open-streets-dc-15.mbtiles"), Locations.DCBounds));
		testList.add(new VectorShapeTest("Shapes", getMapPath("National_Parks","Acadia"), Locations.AcadiaBounds));
		testList.add(new DynamicMarkerTest("Markers", getMapPath("BaseMap", "Earth"), Locations.USBounds));
		testList.add(new InMemoryClusteredMarkersTest("Memory Markers Clustered"));
		testList.add(new TileProviderTest("Tile Provider"));
		testList.add(new TiledVectorMapTest("Tiled Vector Map"));
		testList.add(new InternetVectorMap("Internet Vector Map", Locations.HoustonBounds, this));  
		testList.add(new WorldVectorLabelsTest("Labels", getMapPath("Markers","Places")));
		
		
	}

	String getMapPath(String category, String mapName) {
		File mapFolder = new File(context.getExternalFilesDir(null), category);
		String mapPath = mapFolder.getAbsolutePath() + "/" + mapName;
		Log.w("mapPath", mapPath);
		return mapPath;
	}

	METest findTest(String testName) {
		METest meTest = null;
		for (METest test : testList) {
			if (test.name == testName) {
				meTest = test;
				break;
			}
		}
		return meTest;
	}

	public void stopCurrentTest() {
		if (previousSelectedTest != null) {
			previousSelectedTest.stop();
		}
	}
	
	public void restartCurrentTest() {
		if (previousSelectedTest != null) {
			previousSelectedTest.stop();
			previousSelectedTest.start(mapView, context);
		}
	}
	
	public void startTest(String testName) {
		
		// find selected test
		METest selectedTest = findTest(testName);
		
		if(testName=="Labels") {
			selectedTest.start(mapView, context);
			return;
		}

		// stop previous test
		if (previousSelectedTest != null)
			previousSelectedTest.stop();

		// run test
		if (selectedTest != null)
			selectedTest.start(mapView, context);

		previousSelectedTest = selectedTest;
	}

	String[] getTestNames()
	{
		String names[] = new String[testList.size() + 1];
		names[0] = "None";
		for (int i = 0; i < testList.size(); ++i)
			names[i+1] = testList.get(i).getName();
		return names;
	}
}
