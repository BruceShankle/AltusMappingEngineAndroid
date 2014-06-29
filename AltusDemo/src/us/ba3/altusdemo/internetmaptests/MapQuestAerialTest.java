package us.ba3.altusdemo.internetmaptests;
import us.ba3.altusdemo.METest;

public class MapQuestAerialTest extends METest {
	
	public MapQuestAerialTest(){
		this.name="MapQuest Aerial";
	}
	
	@Override
	public void start() {
		mapView.addInternetMap(this.name,
				"http://otile1.mqcdn.com/tiles/1.0.0/sat/{z}/{x}/{y}.jpg",
				"", 		//Subdomains
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
