package us.ba3.altusdemo.internetmaptests;
import us.ba3.altusdemo.METest;
import us.ba3.me.virtualmaps.VirtualMapInfo;

public class MapBoxSatelliteTest extends METest {

	public MapBoxSatelliteTest(){
		this.name="MapBox Satellite";
	}
	
	@Override
	protected void start() {
		mapView.addInternetMap(this.name,"http://c.tiles.mapbox.com/v3/examples.map-qfyrx5r8");
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}
}
