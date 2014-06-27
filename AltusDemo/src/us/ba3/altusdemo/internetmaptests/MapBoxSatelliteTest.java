package us.ba3.altusdemo.internetmaptests;
import us.ba3.altusdemo.METest;

public class MapBoxSatelliteTest extends METest {

	public MapBoxSatelliteTest(){
		this.name="MapBox Satellite";
	}
	
	@Override
	public void start() {
		mapView.addInternetMap(this.name,"http://d.tiles.mapbox.com/v3/examples.map-qfyrx5r8", "jpg");
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}
}
