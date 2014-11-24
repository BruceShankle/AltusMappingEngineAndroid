package us.ba3.altusdemo.tileworkertests;

import us.ba3.altusdemo.METest;
import us.ba3.me.virtualmaps.*;

public class BitmapTileWorkerTest extends METest {

	/**Demonstrates how to return your own custom images for map tiles.*/
	public BitmapTileWorkerTest(){
		this.name="Bitmap TileWorker";
	}
	
	@Override
	public void start() {
		TileFactory tileFactory = new TileFactory(this.mapView);
		tileFactory.addWorker(new BitmapTileWorker());
		
		VirtualMapInfo mapInfo = new VirtualMapInfo();
		mapInfo.name = this.name;
		mapInfo.zOrder = 100;
		mapInfo.maxLevel = 20;
		mapInfo.isSphericalMercator = false;
		mapInfo.setTileProvider(tileFactory);
		this.mapView.addMapUsingMapInfo(mapInfo);
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, true);
	}
	
}
