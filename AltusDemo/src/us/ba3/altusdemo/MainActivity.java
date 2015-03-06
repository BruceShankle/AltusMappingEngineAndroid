package us.ba3.altusdemo;
import java.io.IOException;
import java.io.InputStream;

import us.ba3.altusdemo.assetmanagement.MEAssetManager;
import us.ba3.altusdemo.assetmanagement.MEDownloadableAsset;
import us.ba3.altusdemo.assetmanagement.MEFileAsset;
import us.ba3.altusdemo.demos.DemoManager;
import us.ba3.me.LightingType;
import us.ba3.me.Location;
import us.ba3.me.LocationType;
import us.ba3.me.MapView;
import us.ba3.me.ReadyListener;
import us.ba3.me.util.FontUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {	
	protected MyMapView mapView;
	protected METestManager _testManager;
	protected Spinner _mapSpinner;
	protected DemoManager _demoManager;
	protected Spinner _demoSpinner;
	protected MEAssetManager _assetManager;

	//Set up downloadable assets
	protected void setupAssets() {

		Log.e("External files dir",getExternalFilesDir(null).toString());


		_assetManager = new MEAssetManager(this);

		String mapServerURL = getString(R.string.mapserver_url);

		//Night map package
		String targetFolder = "PackagedMaps";
		String fileName = "Night.sqlite";
		long fileSize = 5308416;
		_assetManager.addDownloadableAsset(
				new MEDownloadableAsset(mapServerURL+targetFolder+"/"+fileName,targetFolder,fileName,false, fileSize));

		//Earth, terrain, png
		fileSize = 209147904;
		fileName = "TerrainPng.sqlite";
		_assetManager.addDownloadableAsset(
				new MEDownloadableAsset(mapServerURL+targetFolder+"/"+fileName,targetFolder,fileName,false, fileSize));

		//Places.sqlite
		fileSize = 19246080;
		targetFolder = "MarkerMaps";
		fileName = "Places.sqlite";
		_assetManager.addDownloadableAsset(
				new MEDownloadableAsset(mapServerURL+targetFolder+"/"+fileName,targetFolder,fileName,false, fileSize));

		//Earth terrain map.
		/*
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
		 */
		_assetManager.validateAssets();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		//Set title
		this.setTitle("Altus Mapping Engine 2.0 - www.ba3.us");
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
		//mapView.setLicenseKey("AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA");

		//Set lighting type to classic
		mapView.setLightingType(LightingType.kLightingTypeClassic);

		//Set sun relative to observer
		mapView.setSunLocation(new Location(0,0), LocationType.kLocationTypeRelative);

		//Add a ready listener.
		mapView.addReadyListener(new ReadyListener(){
			@Override
			public void engineReady(MapView mapView){
				MainActivity.this._testManager.restartCurrentTest();
			}
		});

		mapView.setBackgroundColor(Color.BLACK);
		layout.addView(mapView);

		//Create container layout for buttons
		//GridLayout grid = new GridLayout(this);
		//grid.add	
		LinearLayout buttonRow = new LinearLayout(this);
		buttonRow.setOrientation(LinearLayout.VERTICAL);
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
				view.setEnabled(false);
				startTest(view.getText().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				Log.w("me", "nothing selected");
			}
		});
		buttonRow.addView(_mapSpinner);

		//Create and add a spinner populated by demo names
		_demoManager = new DemoManager(mapView, getApplication(), buttonRow);
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

		//Initialize utilities
		FontUtil.setContext(this);

		setContentView(layout);

		//Start first test.
		_mapSpinner.setSelection(1);
		
		//Run unit tests
		MathUnitTests.RunTests();
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
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		//Change lighting type
		case R.id.action_realistic_lighting:
			this.mapView.setLightingType(LightingType.kLightingTypeRealistic);
			return true;
		case R.id.action_classic_lighting:
			this.mapView.setLightingType(LightingType.kLightingTypeClassic);
			return true;

			//Toggle sun image
		case R.id.sun_image_on:
			this.mapView.setSunImageEnabled(true);
			return true;
		case R.id.sun_image_off:
			this.mapView.setSunImageEnabled(false);
			return true;

		case R.id.zoom_out:
			this.mapView.performZoomOut();
			return true;
		case R.id.zoom_in:
			this.mapView.performZoomIn();
			return true;
			
		case R.id.zoom_enabled:
			this.mapView.setZoomEnabled(true);
			return true;
		case R.id.zoom_disabled:
			this.mapView.setZoomEnabled(false);
			return true;



		default:
			return super.onOptionsItemSelected(item);
		}
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
}
