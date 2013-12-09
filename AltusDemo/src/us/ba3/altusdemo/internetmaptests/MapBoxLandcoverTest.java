package us.ba3.altusdemo.internetmaptests;
import us.ba3.altusdemo.METest;


public class MapBoxLandcoverTest extends METest {

	public MapBoxLandcoverTest(){
		this.name="MapBox Streets";
	}
	
	@Override
	protected void start() {
		mapView.addInternetMap(this.name,"http://a.tiles.mapbox.com/v3/examples.map-vyofok3q");
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}
}
