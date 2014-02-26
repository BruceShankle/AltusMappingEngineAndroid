package us.ba3.altusdemo.internetmaptests;
import us.ba3.altusdemo.METest;

public class MapQuestAerialTest extends METest {
	
	public MapQuestAerialTest(){
		this.name="MapQuest Aerial";
	}
	
	@Override
	protected void start() {
		mapView.addInternetMap(this.name,"http://otile1.mqcdn.com/tiles/1.0.0/sat", "jpg");
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}
}
