package us.ba3.altusdemo;

import android.util.Log;
import android.os.*;
import us.ba3.me.virtualmaps.TileProvider;
import us.ba3.me.virtualmaps.TileProviderRequest;
import us.ba3.me.virtualmaps.*;
import us.ba3.me.virtualmaps.VirtualMapInfo;

public class TileProviderTest extends METest implements TileProvider {

	
	public TileProviderTest(String name) {
		this.name = name;
	}
	
	@Override
	public void start() {
		
		VirtualMapInfo mapInfo = new VirtualMapInfo();
		mapInfo.name = name;
		mapInfo.zOrder = 100;
		mapInfo.setTileProvider(this);
		mapView.addMapUsingMapInfo(mapInfo);
		
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}

	@Override
	public void requestTile(TileProviderRequest request) {
		if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
			Log.w("", "on the main thread!");
		}
		
		//request.cachedImageName = "asdf";
		request.responseType = TileProviderResponse.kTileResponseTransparentWithChildren;
		mapView.tileLoadComplete(request);
	}

}
