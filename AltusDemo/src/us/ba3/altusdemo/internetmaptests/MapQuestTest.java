package us.ba3.altusdemo.internetmaptests;
import us.ba3.altusdemo.METest;
import us.ba3.me.virtualmaps.*;

public class MapQuestTest extends METest {
	
	public MapQuestTest(){
		this.name="MapQuest Streets";
	}
	
	@Override
	protected void start() {
		mapView.addInternetMap(this.name,"http://otile1.mqcdn.com/tiles/1.0.0/osm");
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}
}