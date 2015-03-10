package us.ba3.altusdemo.demos.camera;
import android.content.Context;
import android.graphics.*;
import us.ba3.altusdemo.PackagedMapTest;
import us.ba3.altusdemo.demos.*;
import us.ba3.me.*;
import us.ba3.me.markers.*;
import us.ba3.me.styles.*;
import us.ba3.me.util.FontUtil;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

public class FlightPlayback extends DemoWithTimer implements MarkerMapDelegate {

	protected static ArrayList<FlightSample> flightSamples;
	protected static int sampleIndex;
	protected static FlightSample currentSample;
	protected MarkerRotationType markerRotationType;

	protected RadioGroup rg;
	protected RadioButton rbNorthUp;
	protected RadioButton rbTrackUp;
	protected RadioButton rbTrackUpForward;
	protected RadioButton rbThreeD;

	LabelStyle labelStyle;

	public FlightPlayback(){
		this.name="Flight Playback";
		this.markerRotationType = MarkerRotationType.kMarkerRotationTrueNorthAligned;

		this.labelStyle = new LabelStyle();
		this.labelStyle.fontName = "Arial";
		this.labelStyle.fontStyle = Typeface.BOLD;
		this.labelStyle.fontSize=20;
		this.labelStyle.strokeVisible = true;
		this.labelStyle.strokeColor = Color.BLACK;
		this.labelStyle.fillColor = Color.WHITE;
	}

	public static ArrayList<FlightSample> getFlightSamples(){
		return flightSamples;
	}

	public void addOwnShipMarker(){

		//Cache the image for the marker.
		try{
			byte pngData[]=ByteStreams.toByteArray(this.context.getAssets().open("DemoData/blueplane.png"));
			this.mapView.addCachedPngImage("blueplane", pngData, false);
		}
		catch(Exception ex){
			Log.w("addOwnShipMarker", ex.getMessage());
		}

		//Add dynamic marker layer
		DynamicMarkerMapInfo mapInfo = new DynamicMarkerMapInfo();
		mapInfo.zOrder = 1000;
		mapInfo.name = "blueplane";
		mapInfo.hitTestingEnabled = false;
		this.mapView.addMapUsingMapInfo(mapInfo);

		//Add dynamic marker
		DynamicMarker marker = new DynamicMarker();
		marker.rotationType = this.markerRotationType;
		marker.name = "blueplane";
		marker.setImage("blueplane");
		marker.location = this.currentSample.getLocation();
		marker.anchorPoint.x = 32;
		marker.anchorPoint.y = 32;
		this.mapView.addDynamicMarkerToMap("blueplane", marker);
	}

	public void updateOwnShipMarker(){
		this.mapView.setDynamicMarkerLocation("blueplane",
				"blueplane",
				this.currentSample.getLocation(),
				this.interval);

		this.mapView.setDynamicMarkerRotation("blueplane",
				"blueplane",
				this.currentSample.hdg,
				this.interval);
	}

	public void removeOwnShipMarker(){
		this.mapView.removeMap("blueplane", false);
	}

	public void addOwnShipBeacon(){
		//Add animated vector circle
		HaloPulse c = new HaloPulse();
		c.name = "locationRing";
		c.location = this.currentSample.getLocation();
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
		this.mapView.addHaloPulse(c);
	}

	public void updateOwnShipBeacon(){
		this.mapView.setHaloPulseLocation("locationRing",
				this.currentSample.getLocation(),
				this.interval);
	}

	public void removeOwnShipBeacon(){
		this.mapView.removeHaloPulse("locationRing");
	}

	public void addFlightPath(){
		VectorMapInfo meVectorMapInfo = new VectorMapInfo();
		meVectorMapInfo.name = "flightpath";
		meVectorMapInfo.zOrder = 800;
		this.mapView.addMapUsingMapInfo(meVectorMapInfo);

		//Set the tesselation threshold to 40 nautical miles
		this.mapView.setTesselationThresholdForMap("flightpath", 40);

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
		this.mapView.addLineToVectorMap("flightpath", pointArray, lineStyle);

		//Add a long line from SFO to RDU
		this.mapView.setTesselationThresholdForMap("flightpath", 40);
		lineStyle.strokeColor = Color.RED;
		lineStyle.strokeWidth = 8;
		ArrayList<Location> pointList2 = new ArrayList<Location>();
		pointList2.add(new Location(35.908505, -78.666412));
		pointList2.add(new Location(47.618823, -122.314558));
		Location[] pointArray2 = pointList2.toArray(new Location[pointList2.size()]);
		this.mapView.addDynamicLineToVectorMap("flightpath", "SEA-RDU", pointArray2, lineStyle);
	}

	public void removeFlightPath(){
		this.mapView.removeMap("flightpath", true);
	}

	@Override
	public void createUserInterface(ViewGroup viewGroup){
		super.createUserInterface(viewGroup);

		rg = new RadioGroup(this.context);
		rg.setOrientation(RadioGroup.HORIZONTAL);
		rbNorthUp = new RadioButton(this.context);
		rbNorthUp.setChecked(true);
		rbNorthUp.setId(0);
		rbNorthUp.setText("North-Up");
		rbNorthUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		rg.addView(rbNorthUp);

		rbTrackUp = new RadioButton(this.context);
		rbTrackUp.setText("Track-Up");
		rbTrackUp.setId(1);
		rbTrackUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		rg.addView(rbTrackUp);

		rbTrackUpForward = new RadioButton(this.context);
		rbTrackUpForward.setText("Track-Up Forward");
		rbTrackUpForward.setId(2);
		rbTrackUpForward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		rg.addView(rbTrackUpForward);

		rbThreeD = new RadioButton(this.context);
		rbThreeD.setText("3D");
		rbThreeD.setId(3);
		rbThreeD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		rg.addView(rbThreeD);

		this.buttonRow.addView(rg);
		this.viewGroup.requestLayout();
	}

	public void addMarkerMap(){
		String markerMapFile = getMapPath("MarkerMaps","Places.sqlite");
		MarkerMapInfo mapInfo = new MarkerMapInfo();
		mapInfo.name = "Places";
		mapInfo.zOrder = 200;
		mapInfo.mapType = MapType.kMapTypeFileMarker;
		mapInfo.sqliteFileName = markerMapFile;
		mapInfo.markerMapDelegate = this;
		mapInfo.hitTestingEnabled = true;
		mapInfo.fadeEnabled = false;
		mapInfo.markerImageLoadingStrategy = MarkerImageLoadingStrategy.kMarkerImageLoadingAsynchronous;
		mapView.addMapUsingMapInfo(mapInfo);
	}
	
	public void removeMarkerMap(){
		mapView.removeMap("Places", false);
	}

	public void addTerrain3D(){
		String terrainMapPath = getMapPath("PackagedMaps","TerrainPng");
		mapView.addTerrainHeightMap("TerrainHeight", terrainMapPath + ".sqlite");
		mapView.setMapZOrder("TerrainHeight", 110);
		mapView.addTerrainMeshMap("Terrain 3D", "TerrainHeight", 32);
		mapView.setMapAlpha("Terrain 3D", 0.3f);
		mapView.setMapZOrder("Terrain 3D", 109);
		mapView.setTileDistanceScale(2.0);
		mapView.setTileLevelBias(0);
		mapView.setDisplayListMode(0);
	}

	@Override
	public void start(MapView mapView, Context context, ViewGroup viewGroup) {
		this.mapView = mapView;
		this.context = context;
		this.viewGroup = viewGroup;
		this.createUserInterface(viewGroup);
		this.addTerrain3D();
		//Load flight samples?
		if(this.flightSamples==null){
			loadSamples(context);
			this.sampleIndex = 0;
			this.currentSample = nextSample();
		}

		//Zoom in to Mt. Ranier area
		this.mapView.setLocationThatFitsCoordinates(
				new Location(46.816508,-121.849219),
				new Location(46.875096,-121.739944),
				20, 20, this.interval);

		this.addOwnShipBeacon();
		this.addOwnShipMarker();
		this.addFlightPath();
		this.addMarkerMap();
		this.currentSample = nextSample();
		super.start(mapView, context, viewGroup);
	}

	@Override
	public void stop(){
		super.stop();
		this.removeOwnShipBeacon();
		this.removeOwnShipMarker();
		this.removeFlightPath();
		this.removeMarkerMap();
	}

	protected FlightSample nextSample() {
		FlightSample sample = this.flightSamples.get(this.sampleIndex);
		this.sampleIndex++;
		if(this.sampleIndex>this.flightSamples.size()-1){
			this.sampleIndex=0;
		}
		return sample;
	}

	public void updateCamera(){
		this.mapView.setLocation(this.currentSample.getLocation(), this.interval);
		switch(rg.getCheckedRadioButtonId()){
		case 0:
			this.mapView.setCameraOrientation(0, 0, 90, this.interval);
			this.mapView.setTrackUpForwardDistance(0, this.interval);
			break;
		case 1:
			this.mapView.setCameraOrientation(this.currentSample.hdg, 0, 90, this.interval);
			this.mapView.setTrackUpForwardDistance(0, this.interval);
			break;
		case 2:
			this.mapView.setCameraOrientation(this.currentSample.hdg, 0, 90, this.interval);
			this.mapView.setTrackUpForwardDistance(200, this.interval);
			break;
		case 3:
			this.mapView.setCameraOrientation(this.currentSample.hdg,
					this.currentSample.roll, -this.currentSample.pitch, this.interval);
			this.mapView.setCameraAltitude(this.currentSample.alt/3.2808f, this.interval);
			this.mapView.setTrackUpForwardDistance(0, this.interval);
			break;
		}
	}

	@Override
	public void timerTick(){
		this.updateCamera();
		this.updateOwnShipBeacon();
		this.updateOwnShipMarker();
		
		final Location sampleLocation = new Location(this.currentSample.getLocation().latitude,
				this.currentSample.getLocation().longitude);
		this.mapView.convertCoordinate(sampleLocation, new PointReceiver(){
			@Override
			public void receivePoint(PointF screenPoint){
				Log.w("FlightPlayback: Plane marker is at screen point (", + screenPoint.x + ", " + screenPoint.y + ")");
			}});
		
		this.currentSample = nextSample();
	}

	protected void loadSamples(Context context){
		this.flightSamples = new ArrayList<FlightSample>();
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
				this.flightSamples.add(new FlightSample(Double.parseDouble(lon),
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

	@Override
	public void tapOnMarker(String arg0, int arg1, String arg2, float arg3,
			PointF arg4, PointF arg5, PointF arg6) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateMarkerInfo(MarkerInfo markerInfo, String arg1) {

		//Create a label bitmap
		Bitmap labelBitmap = FontUtil.createLabel(markerInfo.metaData, this.labelStyle);

		//Set the marker info
		markerInfo.altitude = 5000;
		markerInfo.setImage(labelBitmap);
		markerInfo.anchorPoint = new PointF(labelBitmap.getWidth()/2,
				labelBitmap.getHeight()/2);
	}

}
