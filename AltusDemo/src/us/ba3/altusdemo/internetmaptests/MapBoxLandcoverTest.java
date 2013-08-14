package us.ba3.altusdemo.internetmaptests;
import us.ba3.altusdemo.METest;


public class MapBoxLandcoverTest extends METest {

	public MapBoxLandcoverTest(){
		this.name="MapBox Landcover";
	}
	
	@Override
	protected void start() {
		mapView.addInternetMap(this.name,"http://c.tiles.mapbox.com/v3/examples.map-uci7ul8p");
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}
}
