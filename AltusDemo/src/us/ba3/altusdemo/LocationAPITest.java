package us.ba3.altusdemo;
import android.util.Log;
import us.ba3.altusdemo.internetmaptests.*;
import us.ba3.me.CoordinateBounds;
import us.ba3.me.Location3D;

public class LocationAPITest extends METest {
	
	MapQuestAerialTest rasterMap;
	public LocationAPITest(String name) {
		this.name = name;
		this.interval = 1.0f;
	}
	
	@Override
	public boolean requiresDownloadableAssets() {
		return false;
	}
	
	@Override
	public void start() {
		this.rasterMap = new MapQuestAerialTest();
		this.rasterMap.mapView = this.mapView;
		this.rasterMap.start();
		this.startTimer();
	}

	@Override
	public void stop() {
		this.rasterMap.stop();
	}
	
	public void timerTick(){
		Location3D loc = mapView.getLocation3D();
		Log.w("LocationAPITest",
				"lon: " + loc.longitude +
				" lat: " + loc.latitude +
				" alt: " + loc.altitude);
	}
}
