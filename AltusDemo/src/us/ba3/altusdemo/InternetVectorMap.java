package us.ba3.altusdemo;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.ba3.altusdemo.markertests.WorldVectorLabelsTest;
import us.ba3.me.CoordinateBounds;
import us.ba3.me.Location;
import us.ba3.me.MapView;
import us.ba3.me.styles.LineStyle;
import us.ba3.me.styles.PolygonStyle;
import us.ba3.me.util.FileDownloader;
import us.ba3.me.vector.GeometryGroup;
import us.ba3.me.vector.Line;
import us.ba3.me.vector.Polygon;
import us.ba3.me.virtualmaps.SphericalMercatorRasterTile;
import us.ba3.me.virtualmaps.TileProvider;
import us.ba3.me.virtualmaps.TileProviderRequest;
import us.ba3.me.virtualmaps.VirtualMapInfo;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * Downloads and caches .json tiles from tile.openstreetmap.us.
 * Converts the JSON to Altus geometry primitives using the 
 * Vector Tile Provider system.
 * NOTES:
 * JSON parsing is rather slow, but this demonstrates the possibility
 * of having your vector data tiled somewhere and downloading it
 * as the user needs it.
 */
class DownloadFilesTask extends AsyncTask<String, Integer, GeometryGroup> {

	private TileProviderRequest request;
	private MapView mapView;
	public String cacheFolderName;
	public File cacheFolder;
	public String tilesUrl;
	public String tileFileExtension;
	
	public DownloadFilesTask(Context context, TileProviderRequest request, MapView mapView) {
		this.request = request;
		this.mapView = mapView;
		this.tilesUrl = "http://tile.openstreetmap.us/vectiles-highroad";
		this.tileFileExtension = "json";
		
		//Create cache folder
		this.cacheFolderName = "InternetVectorMap";
		this.cacheFolder = new File(context.getCacheDir(),this.cacheFolderName);
		this.cacheFolder.mkdirs();
	}
	
	public String getTileURL(int x, int y, int z) {
		return this.tilesUrl + "/" + z + "/" + x + "/" + y + "." + this.tileFileExtension;
	}

	public String getTileFileName(int x, int y, int z) {
		return x+"_"+y+"_"+z+"."+this.tileFileExtension;
	}
	
	public boolean existsInCache(String fileName) {
		File f = new File(this.cacheFolder,fileName);
		if(f.isFile() && f.canRead()){
			return true;
		}
		return false;
	}
	
	public boolean downloadFile(String sourceUrl, String fileName){
		try{
			Log.w("Downloading", sourceUrl);
			FileDownloader.downloadFile(sourceUrl, fileName);
		}
		catch(Exception ex){
			Log.w("InternetVectorMap","Failed to download " + sourceUrl + " " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	//Returns a polygon that covers a tile
	Polygon createBoundingPolygon(SphericalMercatorRasterTile tile, int shapeId){
		Location min = request.bounds.min;
		Location max = request.bounds.max;
		double width = max.longitude - min.longitude;
		double height = max.latitude - min.latitude;
		Location size = new Location(height, width);	
		Polygon polygon = new Polygon();
		polygon.shell = new Location[] {
				min,
				new Location(min.latitude + size.latitude, min.longitude),
				max,
				new Location(min.latitude, min.longitude + size.longitude),
				min
		};
		polygon.shapeId = shapeId;
		return polygon;
	}
	
	@Override
	protected GeometryGroup doInBackground(String... params) {
		
		SphericalMercatorRasterTile tile = request.sphericalMercatorRasterTiles[0];
		
		String fileName = getTileFileName(tile.slippyX, tile.slippyY, tile.slippyZ);
		String fullFileName = this.cacheFolder.getAbsolutePath() + File.separator + fileName;
		
		if(!existsInCache(fileName)){
			boolean downloaded = downloadFile(getTileURL(tile.slippyX, tile.slippyY, tile.slippyZ), fullFileName);
			if(!downloaded){
				//return an empty geometry group
				return new GeometryGroup();
			}
		}
		
		//Parse the JSON data
		GeometryGroup geometryGroup = parseJSONFile(fullFileName);
		
		//Add a solid background as a polygon
		geometryGroup.polygons = new Polygon[]{createBoundingPolygon(tile, 2)};
	
		return geometryGroup;
	}

	public GeometryGroup parseJSONFile(String fileName) {
		
		Log.w("Parsing JSON", fileName);
		
		GeometryGroup geometry = new GeometryGroup();
		
		try {
			//Load the file into a string
			String jsonString = Files.toString(new File(fileName), Charsets.UTF_8);
			
			// parse top level
			JSONObject root = new JSONObject(jsonString);
			
			JSONArray features = root.getJSONArray("features");
			// loop over features
			int featureCount = features.length();
			geometry.lines = new Line[featureCount];
			for (int i = 0; i < featureCount; ++i) {

				// dive straight into coordinates
				JSONObject feature = features.getJSONObject(i);
				JSONObject geometryObject = feature.getJSONObject("geometry");
				JSONArray coordinates = geometryObject.getJSONArray("coordinates");
				
				// create a line
				Line line = new Line();
				line.shapeId = 1;
				int coordinateCount = coordinates.length();
				line.coordinates = new Location[coordinateCount];

				// parse coordinates into line
				for (int j = 0; j < coordinateCount; ++j) {
					try{
						JSONArray coordinate = coordinates.getJSONArray(j);
						double lat = coordinate.getDouble(1);
						double lon = coordinate.getDouble(0);
						line.coordinates[j] = new Location(lat, lon);
					}
					catch(Exception ex){
						
					}
				}
				// add line to result
				geometry.lines[i] = line;
			}

		}
		catch (JSONException ex) {
			Log.w("JSONException", ex.toString());
		}
		catch (IOException ex){
			Log.w("IOException", ex.toString());
		}

		return geometry;
	}

	protected void onProgressUpdate(Integer... progress) {

	}

	protected void onPostExecute(GeometryGroup result) {
		//Notify the mapping engine the request is ready
		mapView.vectorTileLoadComplete(request, result);
	}

}

public class InternetVectorMap extends METest implements TileProvider {

	CoordinateBounds startingBounds;
	WorldVectorLabelsTest labels;
	public InternetVectorMap(String name, CoordinateBounds bounds, METestManager testManager) {
		this.name = name;
		this.startingBounds = bounds;
		this.labels = new WorldVectorLabelsTest("Places", testManager.getMapPath("Markers", "Places"));
	}

	@Override
	public void start() {
		
		//Zoom to bounds
        mapView.setLocationThatFitsCoordinates(this.startingBounds.min, this.startingBounds.max, 0, 0, 0.5);
        
		//Turn on labels
		labels.start(this.mapView, this.context);
		
		//Add virtual vector map
		VirtualMapInfo mapInfo = new VirtualMapInfo();
		mapInfo.name = name;
		mapInfo.zOrder = 100;
		mapInfo.isVector = true;
		mapInfo.setTileProvider(this);
		mapView.addMapUsingMapInfo(mapInfo);
		mapView.setMapZOrder(name, 100);
		
		//Create style for road lines
		LineStyle lineStyle = new LineStyle();
		lineStyle.strokeWidth = 2;
		lineStyle.strokeColor = Color.argb(255, 205,201,201);
		lineStyle.outlineWidth = 0;
		mapView.setLineStyleOnVectorMap(name, 1, lineStyle);
		
		//Create a style for the background
		//250-235-215
		PolygonStyle polygonStyle = new PolygonStyle();
		polygonStyle.strokeWidth = 0;
		polygonStyle.strokeColor = Color.argb(0, 0, 0, 0);
		polygonStyle.outlineWidth = 0;
		polygonStyle.fillColor = Color.argb(255, 250, 235, 215);
		mapView.setPolygonStyleOnVectorMap(name, 2, polygonStyle);
		
	}

	@Override
	public void stop() {
		labels.stop();
		mapView.removeMap(this.name, false);
	}

	@Override
	public void requestTile(TileProviderRequest request) {
		if (request.sphericalMercatorRasterTiles.length == 1) {
			new DownloadFilesTask(this.context, request, mapView).execute("");
		} else {
			mapView.vectorTileLoadComplete(request, new GeometryGroup());
		}
	}
	
}
