package us.ba3.altusdemo;
import java.io.IOException;

import android.util.Log;
import android.os.*;
import us.ba3.me.*;
import us.ba3.me.virtualmaps.*;
import android.graphics.*;
import android.graphics.Bitmap.Config;

/**
 * Demonstrates an extremely simple way to pull data for the mapping engine from
 * an large image on device.
 * @author Bruce Shankle
 */
public class TileProviderLargeImageSamplingTest extends RasterMapTest implements TileProvider {
	
	public TileProviderLargeImageSamplingTest(String name, String mapPath, CoordinateBounds bounds) {
		super(name, mapPath, bounds);
	}
	
	@Override
	public void start() {
		//Add virtual map
		VirtualMapInfo mapInfo = new VirtualMapInfo();
		mapInfo.name = name;
		mapInfo.zOrder = 200;
		mapInfo.setTileProvider(this);
		mapView.addMapUsingMapInfo(mapInfo);
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}

	@Override
	public void requestTile(TileProviderRequest tileRequest) {
		TileMaker t = new TileMaker(this.mapPath, tileRequest, this.mapView);
		new Thread(t).start();
	}

	private static class TileMaker implements Runnable{
		
		private TileProviderRequest tileRequest = null;
		private us.ba3.me.MapView mapView = null;
		private String imagePath;
		
		private TileMaker(String imagePath, final TileProviderRequest tileRequest, final MapView mapView){
			this.imagePath = imagePath;
			this.mapView = mapView;
			this.tileRequest = tileRequest;
		}
		
		void errorTile(TileProviderRequest tileRequest){
			tileRequest.cachedImageName = "errorTexture";
			tileRequest.responseType = TileProviderResponse.kTileResponseRenderNamedCachedImage;
			this.mapView.tileLoadComplete(tileRequest);
		}
		
		private Rect computeSubRegion(double minX, double minY, double maxX, double maxY){
			
			java.util.Random randomGenerator = new java.util.Random((int)minX);
			
			//19200��������9600
			double dx = randomGenerator.nextDouble();
			double dy = randomGenerator.nextDouble();
			int x = (int)(dx * 19200-256);
			int y = (int)(dy * 9600-256);
			
			Log.i("subRegion","x:" + x + " y:" + y);
			Rect r = new Rect();
			r.set(x, y, x+256, y+256);
			return r;
		}
		
		@Override
		public void run() {
			
			
			Log.w("TileMaker", "running on backgraound thread...");
			
			
			BitmapRegionDecoder regionDecoder;
			try {
				regionDecoder = BitmapRegionDecoder.newInstance(this.imagePath, false);
			} catch (IOException e) {
				Log.i("TileMaker",e.getMessage());
				errorTile(tileRequest);
				return;
			}
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig=Config.ARGB_8888;   //explicit setting!
			
			for(SphericalMercatorRasterTile t : tileRequest.sphericalMercatorRasterTiles){
				Rect r = computeSubRegion(t.minX, t.minY, t.maxX, t.maxY);
				//r.set(0, 0, 256, 256);
				Bitmap subRegion = regionDecoder.decodeRegion(r, options);
				if(subRegion == null){
					Log.w("TileProviderLargeImageSamplingTest", "decodeRegion failed");
					errorTile(tileRequest);
					return;
				}
				t.setImage(subRegion, false);
			}
			
			regionDecoder.recycle();
			
			//We're done
			tileRequest.responseType = TileProviderResponse.kTileResponseRenderImage;
			this.mapView.tileLoadComplete(tileRequest);
			
		}
		
		
	}
	

}

