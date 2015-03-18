package us.ba3.altusdemo.internetmaptests;

import us.ba3.altusdemo.METest;

public class GoogleSatelliteTest extends METest {

	public GoogleSatelliteTest(){
		this.name="Google Satellite";
	}
	
	@Override
	public void start() {
		mapView.addStreamingRasterMap(this.name,
				"http://mt{s}.google.com/vt/lyrs=s&x={x}&y={y}&z={z}",
				"", //North tiles
				"", //South tiles
				"1,2,3,4", 	//Subdomains
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