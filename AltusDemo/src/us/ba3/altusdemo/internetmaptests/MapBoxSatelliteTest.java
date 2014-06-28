package us.ba3.altusdemo.internetmaptests;
import us.ba3.altusdemo.METest;

public class MapBoxSatelliteTest extends METest {

	public MapBoxSatelliteTest(){
		this.name="MapBox Satellite";
	}
	
	@Override
	public void start() {
		mapView.addInternetMap(this.name,
				"http://{s}.tiles.mapbox.com/v3/dxjacob.ho6k3ag9/{z}/{x}/{y}.jpg",
				"a,b,c,d",
				20,
				2,
				2, //Workers
				true,
				false);
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}
}
