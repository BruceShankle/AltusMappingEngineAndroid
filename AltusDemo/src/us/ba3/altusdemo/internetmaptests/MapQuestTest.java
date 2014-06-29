package us.ba3.altusdemo.internetmaptests;
import us.ba3.altusdemo.METest;

public class MapQuestTest extends METest {
	
	public MapQuestTest(){
		this.name="MapQuest Streets";
	}
	
	@Override
	public void start() {
		mapView.addInternetMap(this.name,
				"http://otile1.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.jpg",
				"", 		//Subdomains
				19,			//Max Level
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