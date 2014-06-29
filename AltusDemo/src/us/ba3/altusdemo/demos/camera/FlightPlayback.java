package us.ba3.altusdemo.demos.camera;
import android.content.Context;
import android.graphics.*;
import us.ba3.altusdemo.demos.*;
import us.ba3.me.*;
import us.ba3.me.markers.*;
import us.ba3.me.styles.*;
import android.util.Log;
import java.io.*;
import java.util.*;

import com.google.common.io.ByteStreams;

class FlightSample {
	public FlightSample(double lon,
			double lat,
			double alt,
			double hdg,
			double roll,
			double pitch){
		this.lon=lon;
		this.lat=lat;
		this.alt=alt;
		this.hdg=hdg;
		this.roll=roll;
		this.pitch=pitch;
	}
	public double lon;
	public double lat;
	public double alt;
	public double hdg;
	public double roll;
	public double pitch;
	
	public Location getLocation(){
		return new Location(this.lat, this.lon);
	}
}

public class FlightPlayback extends DemoWithTimer {

	protected static ArrayList<FlightSample> _flightSamples;
	protected static int _sampleIndex;
	protected static FlightSample _currentSample;
	protected MarkerRotationType _markerRotationType;
	public FlightPlayback(){
		this._name="Flight Playback - North Up";
		_markerRotationType = MarkerRotationType.kMarkerRotationTrueNorthAligned;
	}
	
	public static ArrayList<FlightSample> getFlightSamples(){
		return _flightSamples;
	}

	public void addOwnShipMarker(){
		
		//Cache the image for the marker.
		try{
			byte pngData[]=ByteStreams.toByteArray(_context.getAssets().open("DemoData/blueplane.png"));
			this._mapView.addCachedPngImage("blueplane", pngData, false);
		}
		catch(Exception ex){
			Log.w("addOwnShipMarker", ex.getMessage());
		}
		
		//Add dynamic marker layer
		DynamicMarkerMapInfo mapInfo = new DynamicMarkerMapInfo();
		mapInfo.zOrder = 1000;
		mapInfo.name = "blueplane";
		mapInfo.hitTestingEnabled = false;
		_mapView.addMapUsingMapInfo(mapInfo);
		
		//Add dynamic marker
		DynamicMarker marker = new DynamicMarker();
		marker.rotationType = _markerRotationType;
		marker.name = "blueplane";
		marker.setImage("blueplane");
		marker.location = _currentSample.getLocation();
		marker.anchorPoint.x = 32;
		marker.anchorPoint.y = 32;
		_mapView.addDynamicMarkerToMap("blueplane", marker);
	}
	
	public void updateOwnShipMarker(){
		this._mapView.setDynamicMarkerLocation("blueplane",
				"blueplane",
				_currentSample.getLocation(),
				this.interval);
		
		this._mapView.setDynamicMarkerRotation("blueplane",
				"blueplane",
				_currentSample.hdg,
				this.interval);
	}
	
	public void removeOwnShipMarker(){
		this._mapView.removeMap("blueplane", false);
	}
	
	public void addOwnShipBeacon(){
		//Add animated vector circle
		HaloPulse c = new HaloPulse();
		c.name = "locationRing";
		c.location = _currentSample.getLocation();
		c.minRadius = 5;
		c.maxRadius = 75;
		c.animationDuration = 2.5f;
		c.repeatDelay = 0;
		c.fade = true;
		c.fadeDelay = 1;
		c.zOrder = 999;
		c.lineStyle.strokeColor = Color.WHITE;
		c.lineStyle.outlineColor = Color.rgb(30, 144, 255);
		c.lineStyle.outlineWidth = 4;
		this._mapView.addHaloPulse(c);
	}
	
	public void updateOwnShipBeacon(){
		this._mapView.setHaloPulseLocation("locationRing",
				_currentSample.getLocation(),
				this.interval);
	}
	
	public void removeOwnShipBeacon(){
		this._mapView.removeHaloPulse("locationRing");
	}
	
	public void addFlightPath(){
		VectorMapInfo meVectorMapInfo = new VectorMapInfo();
		meVectorMapInfo.name = "flightpath";
		meVectorMapInfo.zOrder = 800;
		this._mapView.addMapUsingMapInfo(meVectorMapInfo);
		
		//Set the tesselation threshold to 40 nautical miles
		this._mapView.setTesselationThresholdForMap("flightpath", 40);
		
		//Create an array of points for the flight path.
		ArrayList<Location> pointList = new ArrayList<Location>();
		for(FlightSample sample : FlightPlayback.getFlightSamples()){
			pointList.add(sample.getLocation());
		}
		
		//Create a style for the flight path
		LineStyle lineStyle = new LineStyle();
		lineStyle.outlineColor = Color.BLACK;
		lineStyle.outlineWidth = 2;
		lineStyle.strokeColor = Color.BLUE;
		lineStyle.strokeWidth = 4;
		
		//Add the line
		Location[] pointArray = pointList.toArray(new Location[pointList.size()]);
		this._mapView.addLineToVectorMap("flightpath", pointArray, lineStyle);
		
		//Add a long line from SFO to RDU
		this._mapView.setTesselationThresholdForMap("flightpath", 40);
		lineStyle.strokeColor = Color.RED;
		lineStyle.strokeWidth = 8;
		ArrayList<Location> pointList2 = new ArrayList<Location>();
		pointList2.add(new Location(35.908505, -78.666412));
		pointList2.add(new Location(47.618823, -122.314558));
		Location[] pointArray2 = pointList2.toArray(new Location[pointList2.size()]);
		this._mapView.addDynamicLineToVectorMap("flightpath", "SEA-RDU", pointArray2, lineStyle);
	}
	
	public void removeFlightPath(){
		this._mapView.removeMap("flightpath", true);
	}
	
	@Override
	public void start(MapView mapView, Context context) {
		this._mapView = mapView;
		this._context = context;
		
		mapView.removeAllMaps(true);
		
		//Load flight samples?
		if(_flightSamples==null){
			loadSamples(context);
			_sampleIndex = 0;
			_currentSample = nextSample();
		}
		
		//Zoom in to Mt. Ranier area
		this._mapView.setLocationThatFitsCoordinates(
				new Location(46.816508,-121.849219),
				new Location(46.875096,-121.739944),
				20, 20, this.interval);
		
		this.addOwnShipBeacon();
		this.addOwnShipMarker();
		this.addFlightPath();
		_currentSample = nextSample();
		super.start(mapView, context);
	}
	
	@Override
	public void stop(){
		super.stop();
		this.removeOwnShipBeacon();
		this.removeOwnShipMarker();
		this.removeFlightPath();
	}

	protected FlightSample nextSample() {
		FlightSample sample = _flightSamples.get(_sampleIndex);
		_sampleIndex++;
		if(_sampleIndex>_flightSamples.size()-1){
			_sampleIndex=0;
		}
		return sample;
	}
	
	public void updateCamera(){
		this._mapView.setLocation(_currentSample.getLocation(), this.interval);
	}
	
	@Override
	public void timerTick(){
		this.updateCamera();
		this.updateOwnShipBeacon();
		this.updateOwnShipMarker();
		PointF screenPoint = this._mapView.convertCoordinate(_currentSample.getLocation());
		Log.w("FlightPlayback", "Plane is at:" + screenPoint.x+"," + screenPoint.y);
		_currentSample = nextSample();
	}

	protected void loadSamples(Context context){
		_flightSamples = new ArrayList<FlightSample>();
		//Load recorded flight samples.
		InputStream is = null;
		try {
			is = context.getAssets().open("DemoData/recordedflight.csv");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] RowData = line.split(",");
				String lon = RowData[1];
				String lat = RowData[2];
				String alt = RowData[3];
				String hdg = RowData[4];
				String roll = RowData[5];
				String pitch = RowData[6];
				_flightSamples.add(new FlightSample(Double.parseDouble(lon),
						Double.parseDouble(lat),
						Double.parseDouble(alt),
						Double.parseDouble(hdg),
						Double.parseDouble(roll),
						Double.parseDouble(pitch)));
			}
		}
		catch (IOException ex) {
			Log.w("FlightPlayback",ex.getMessage());
		}
		finally {
			try {
				is.close();
			}
			catch (IOException e) {
				Log.w("FlightPlayback",e.getMessage());
			}
		}
	}
	
	//Vector map delegate interface
	/**Called when a hit is detected on a vector line segment.*/
	public void lineSegmentHitDetected(String mapName,
			String shapeId,
			Location coordinate,
			int segmentStartIndex,
			int segmentEndIndex){
		
	}

	/**Called when a hit is detected on a vector line vertex.*/
	public void vertexHitDetected(String mapName,
			String shapeID,
			Location coordinate,
			int vertexIndex){
		
	}

}
