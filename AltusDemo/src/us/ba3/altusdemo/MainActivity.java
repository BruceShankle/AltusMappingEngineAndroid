package us.ba3.altusdemo;
import us.ba3.altusdemo.demos.*;
import com.google.common.io.ByteStreams;

import us.ba3.altusdemo.assetmanagement.*;
import us.ba3.me.*;
import us.ba3.me.markers.*;
import us.ba3.me.util.*;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.*;
import android.graphics.*;
import android.content.res.*;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.location.LocationManager;
import android.content.*;
import android.provider.*;
import android.app.*;
import java.io.*;

public class MainActivity extends Activity {	
	protected MyMapView mapView;
	protected METestManager _testManager;
	protected Spinner _mapSpinner;
	protected DemoManager _demoManager;
	protected Spinner _demoSpinner;
	protected MEAssetManager _assetManager;
	protected Switch _gpsSwitch;

	//Set up downloadable assets
	protected void setupAssets() {
		_assetManager = new MEAssetManager(this);

		String mapServerURL = getString(R.string.mapserver_url);

		//National park map
		String targetFolder = "National_Parks";
		String fileName = "Acadia.map";
		long fileSize = 4202689;
		_assetManager.addDownloadableAsset(
				new MEDownloadableAsset(mapServerURL+targetFolder+"/"+fileName,targetFolder,fileName,false, fileSize));
		fileName = "Acadia.sqlite";
		fileSize = 57344;
		_assetManager.addDownloadableAsset(
				new MEDownloadableAsset(mapServerURL+targetFolder+"/"+fileName,targetFolder,fileName,false, fileSize));

		//FAA sectional
		targetFolder = "Sectional";
		fileName = "Charlotte_North.map";
		fileSize = 49648601;
		_assetManager.addDownloadableAsset(
				new MEDownloadableAsset(mapServerURL+targetFolder+"/"+fileName,targetFolder,fileName,false, fileSize));
		fileName = "Charlotte_North.sqlite";
		fileSize = 99328;
		_assetManager.addDownloadableAsset(
				new MEDownloadableAsset(mapServerURL+targetFolder+"/"+fileName,targetFolder,fileName,false, fileSize));

		//Earth terrain map.
		mapServerURL += "android/";
		targetFolder = "BaseMap";
		fileName = "Earth.zip";
		fileSize = 61456421;
		MEDownloadableAsset earthZip = new MEDownloadableAsset(mapServerURL+targetFolder+"/"+fileName,targetFolder,fileName,true,fileSize);
		MEFileAsset earthMap = new MEFileAsset(targetFolder, "Earth.map", 327811072);
		MEFileAsset earthSqlite = new MEFileAsset(targetFolder, "Earth.sqlite", 237568);
		earthZip.addArchiveFile(earthMap);
		earthZip.addArchiveFile(earthSqlite);
		_assetManager.addDownloadableAsset(earthZip);

		//Vector maps
		targetFolder = "NewVector";
		fileName = "World_Style2.zip";
		fileSize = 13983612;
		MEDownloadableAsset worldStyle2Zip = new MEDownloadableAsset(mapServerURL+targetFolder+"/"+fileName,targetFolder,fileName,true,fileSize);
		MEFileAsset worldStyle2Map = new MEFileAsset(targetFolder, "World_Style2.map", 21207728);
		MEFileAsset worldStyle2Sqlite = new MEFileAsset(targetFolder, "World_Style2.sqlite", 1574912);
		worldStyle2Zip.addArchiveFile(worldStyle2Map);
		worldStyle2Zip.addArchiveFile(worldStyle2Sqlite);
		_assetManager.addDownloadableAsset(worldStyle2Zip);

		//Labels for the vector maps
		targetFolder = "Markers";
		fileName = "Places.zip";
		fileSize = 10732925;
		MEDownloadableAsset placesZip = new MEDownloadableAsset(mapServerURL+targetFolder+"/"+fileName,targetFolder,fileName,true, fileSize);
		MEFileAsset placesSqlite = new MEFileAsset(targetFolder,"Places.sqlite", 19482624);
		placesZip.addArchiveFile(placesSqlite);
		_assetManager.addDownloadableAsset(placesZip);
		
		//MBTiles map
		targetFolder = "MBTiles";
		fileName = "open-streets-dc-15.mbtiles";
		fileSize = 6032384;
		_assetManager.addDownloadableAsset(
				new MEDownloadableAsset(mapServerURL+targetFolder+"/"+fileName,targetFolder,fileName,false, fileSize));

		_assetManager.validateAssets();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		this.setTitle("BA3 Altus Mapping Engine Demo - www.ba3.us");
		super.onCreate(savedInstanceState);

		//Set internal files path for tests
		METest.internalFilesPath = this.getFilesDir().getAbsolutePath();

		//Set up downloadable assets
		this.setupAssets();

		RelativeLayout layout = new RelativeLayout(this);

		//If you want to override cache size, you would do that here like so:
		//MapView.CACHESIZE = 90000000; //Bytes
		
		//Add map view.
		mapView = new MyMapView(getApplication());
		
		//If you want to disable the BA3 watermark, set your license key.
		//mapView.setLicenseKey("your license key here");
		
		//Add a ready listener.
		mapView.addReadyListener(new ReadyListener(){
			@Override
			public void engineReady(MapView mapView){
				MainActivity.this._testManager.restartCurrentTest();
				//MainActivity.this.enableLocationLayer(_gpsSwitch.isChecked());
			}
		});

		mapView.setBackgroundColor(Color.GRAY);
		layout.addView(mapView);
		
		//Create container layout for buttons
		LinearLayout buttonRow = new LinearLayout(this);
		buttonRow.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(buttonRow);

		//Create test manager and get test names
		_testManager = new METestManager(mapView, getApplication());
		final String testNames[] = _testManager.getTestNames();

		//Create and add a spinner populated by test names
		_mapSpinner = new Spinner(this);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, testNames);
		_mapSpinner.setAdapter(spinnerArrayAdapter);
		_mapSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// get name of selected item
				TextView view = (TextView)selectedItemView;
				startTest(view.getText().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				Log.w("me", "nothing selected");
			}
		});
		buttonRow.addView(_mapSpinner);

		//Add switch to toggle GPS on / off
		/*
		_gpsSwitch = new Switch(this);
		boolean gpsAvailable = locationServicesAvailable();
		_gpsSwitch.setEnabled(gpsAvailable);
		_gpsSwitch.setText("GPS");
		_gpsSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				MainActivity.this.enableLocationLayer(_gpsSwitch.isChecked());
			}
		});
		buttonRow.addView(_gpsSwitch);*/

		//Create and add a spinner populated by demo names
		_demoManager = new DemoManager(mapView, getApplication());
		final String demoNames[] = _demoManager.getDemoNames();
		_demoSpinner = new Spinner(this);
		ArrayAdapter<String> demoSpinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, demoNames);
		_demoSpinner.setAdapter(demoSpinnerArrayAdapter);
		_demoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// get name of selected item
				TextView view = (TextView)selectedItemView;
				startDemo(view.getText().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				Log.w("me", "nothing selected");
			}
		});
		buttonRow.addView(_demoSpinner);

		//Add another spinner for various tests

		//Initialize utilities
		FontUtil.setContext(this);

		setContentView(layout);

		//Start first test.
		_mapSpinner.setSelection(1);

		//Ask user to turn on GPS
		/*
		if(!gpsAvailable){
			promptToEnableLocationServices();
		}*/
	}

	protected boolean locationServicesAvailable(){
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
				!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			return false;
		}
		return true;
	}

	protected void promptToEnableLocationServices() {
		if(!locationServicesAvailable()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Location Services Not Active");
			builder.setMessage("Please enable Location Services and GPS");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialogInterface, int i) {
					// Show location settings when the user acknowledges the alert dialog
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intent);
				}
			});
			Dialog alertDialog = builder.create();
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();
		}
	}

	public void startDemo(String demoName) {
		_demoManager.startDemo(demoName);
	}

	public void startTest(String testName) {
		METest t = _testManager.findTest(testName);

		if(t==null){
			_testManager.stopCurrentTest();
			return;
		}

		//Some tests may need to download maps
		if(t!=null){
			if(t.requiresDownloadableAssets()){
				if(!_assetManager.assetsValid){
					us.ba3.altusdemo.assetmanagement.MEAssetDownloader d;
					d = new us.ba3.altusdemo.assetmanagement.MEAssetDownloader(MainActivity.this,
							_testManager, _assetManager, testName);
					d.showDialog();
					return;
				}
			}
			//Start the test.
			_testManager.startTest(testName);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override protected void onPause() {
		super.onPause();
	}

	@Override protected void onResume() {
		super.onResume();
		//_gpsSwitch.setEnabled(this.locationServicesAvailable());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public Bitmap getBitmapAsset(String strName) {
		AssetManager assetManager = getAssets();
		InputStream istr;
		Bitmap bitmap = null;
		try {
			istr = assetManager.open(strName);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			Log.w("Bitmap decode error:", e.getMessage());
			return null;
		}
		return bitmap;
	}

	public void enableLocationLayer(boolean enabled) {

		String mapName = "CurrentLocation";
		String imagePath = "images/bluedot.png";
		if(enabled){

			//_meMapView.removeAllMaps(true);

			//Cache blue dot marker image.
			try{
				byte pngData[]=ByteStreams.toByteArray(getAssets().open(imagePath));
				mapView.addCachedPngImage("bluedot", pngData, false);
			}
			catch(Exception ex){
				Log.w("BUG", ex.getMessage());
				return;
			}

			//Add dynamic marker layer
			DynamicMarkerMapInfo mapInfo = new DynamicMarkerMapInfo();
			mapInfo.zOrder = 1000;
			mapInfo.name = mapName;
			mapInfo.hitTestingEnabled = false;
			mapView.addMapUsingMapInfo(mapInfo);

			//Add dynamic marker
			DynamicMarker marker = new DynamicMarker();
			marker.name = "bluedot";
			//marker.cachedImageName = "bluedot";
			marker.location = new Location(35.7719, -78.6389);
			marker.anchorPoint.x = 12;
			marker.anchorPoint.y = 12;
			marker.setImage(getBitmapAsset(imagePath), false);
			mapView.addDynamicMarkerToMap(mapName, marker);

			//Add animated vector circle
			HaloPulse c = new HaloPulse();
			c.name = "locationRing";
			c.location = marker.location;
			c.zOrder = 999;
			c.lineStyle.strokeColor = Color.WHITE;
			c.lineStyle.outlineColor = Color.rgb(30, 144, 255);
			c.lineStyle.outlineWidth = 4;
			mapView.addHaloPulse(c);

		}
		else{
			mapView.removeMap(mapName, false);
			mapView.removeHaloPulse("locationRing");
		}
	}

}
