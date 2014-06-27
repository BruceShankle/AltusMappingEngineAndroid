package us.ba3.altusdemo;
import java.io.File;
import com.google.common.io.ByteStreams;
import us.ba3.me.*;
import us.ba3.altusdemo.markertests.*;

public class VectorWorldMapTest extends METest {

	CoordinateBounds bounds;
	File _mapFile;
	File _sqliteFile;
	WorldVectorLabelsTest _labels;
	
	public VectorWorldMapTest(String name, String mapPath, CoordinateBounds bounds, METestManager testManager) {
		this.name = name;
		_mapFile = new File(mapPath + ".map");
		_sqliteFile = new File(mapPath + ".sqlite");
		this.bounds = bounds;
		
		_labels = new WorldVectorLabelsTest("Places", testManager.getMapPath("Markers", "Places"));
	}
	
	protected void cachePng(String resourcePath, String cachedImageName){
		try{
			byte pngData[]=ByteStreams.toByteArray(context.getAssets().open(resourcePath));
			mapView.addCachedPngImage(cachedImageName, pngData, false);
		}
		catch(Exception ex){
		}
	}
	
	protected void cacheVectorMapTextures(){
		if(mapView.getDeviceScale()>1){
			//Mapbox style
			cachePng("VectorMapTextures/landTexture@2x.png", "landTexture@2x.png");
			cachePng("VectorMapTextures/park@2x.png","park@2x.png");
			cachePng("VectorMapTextures/waterTexture@2x.png","waterTexture@2x.png");
			
			//Apple style
			cachePng("VectorMapTextures/apple_golf@2x.png","apple_golf@2x.png");
			cachePng("VectorMapTextures/apple_land@2x.png","apple_land@2x.png");
			cachePng("VectorMapTextures/apple_park@2x.png","apple_park@2x.png");
			cachePng("VectorMapTextures/apple_water@2x.png","apple_water@2x.png");
		}
		else{
			//Mapbox style
			cachePng("VectorMapTextures/landTexture.png", "landTexture@2x.png");
			cachePng("VectorMapTextures/park.png","park@2x.png");
			cachePng("VectorMapTextures/waterTexture.png","waterTexture@2x.png");
			
			//Apple style
			cachePng("VectorMapTextures/apple_golf.png","apple_golf@2x.png");
			cachePng("VectorMapTextures/apple_land.png","apple_land@2x.png");
			cachePng("VectorMapTextures/apple_park.png","apple_park@2x.png");
			cachePng("VectorMapTextures/apple_water.png","apple_water@2x.png");
		}
	}
	
	@Override
	public void start() {
		
		
		cacheVectorMapTextures();
		
		_labels.start(this.mapView, this.context);
        mapView.addVectorMap(name, _sqliteFile.getAbsolutePath(), _mapFile.getAbsolutePath());
        
        // zoom to bounds
        if (bounds != null) {
        		mapView.setLocationThatFitsCoordinates(bounds.min, bounds.max, 0, 0, 0.5);
        }
	}
	
	@Override
	public boolean requiresDownloadableAssets() {
		return true;
	}
	

	@Override
	public void stop() {
		_labels.stop();
		mapView.removeMap(name, true);
	}

}
