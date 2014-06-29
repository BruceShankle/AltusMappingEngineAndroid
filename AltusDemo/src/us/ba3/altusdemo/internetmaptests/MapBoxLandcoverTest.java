package us.ba3.altusdemo.internetmaptests;
import us.ba3.altusdemo.METest;


public class MapBoxLandcoverTest extends METest {

	public MapBoxLandcoverTest(){
		this.name="MapBox Streets";
	}
	
	@Override
	public void start() {
		mapView.addInternetMap(this.name,
				"http://{s}.tiles.mapbox.com/v3/dxjacob.map-s5qr595q/{z}/{x}/{y}.png",
				"a,b,c,d", 	//Subdomains
				20,			//Max Level
				2,			//zOrder
				3,			//Number of simultaneous downloads
				true,		//Use cache
				false		//No alpha
				);
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}
}
