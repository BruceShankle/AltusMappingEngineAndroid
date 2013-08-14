package us.ba3.altusdemo;

import android.util.Log;
import us.ba3.me.virtualmaps.TileProvider;
import us.ba3.me.virtualmaps.TileProviderRequest;
import us.ba3.me.virtualmaps.VirtualMapInfo;

public class TileProviderTest extends METest implements TileProvider {

	public TileProviderTest(String name) {
		this.name = name;
	}
	
	@Override
	protected void start() {
		
		VirtualMapInfo mapInfo = new VirtualMapInfo();
		mapInfo.name = name;
		mapInfo.zOrder = 100;
		mapInfo.setTileProvider(this);
		mapView.addMapUsingMapInfo(mapInfo);
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestTile(TileProviderRequest request) {
		Log.w("", "got request!!!");
		//mapView->tileLoadComplete(name, request);
	}

}
