package us.ba3.altusdemo.markertests;
import java.io.File;

import us.ba3.altusdemo.METest;
import us.ba3.me.MapType;
import us.ba3.me.PointReceiver;
import us.ba3.me.markers.MarkerImageLoadingStrategy;
import us.ba3.me.markers.MarkerInfo;
import us.ba3.me.markers.MarkerInfoReceiver;
import us.ba3.me.markers.MarkerMapDelegate;
import us.ba3.me.markers.MarkerMapInfo;
import us.ba3.me.styles.LabelStyle;
import us.ba3.me.util.FontUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.util.Log;

public class Places extends METest implements MarkerMapDelegate {

	File _sqliteFile;
	LabelStyle _countryStyle;
	LabelStyle _stateStyle;
	LabelStyle _cityStyle;

	public Places(String name, String mapPath){
		this.name=name;
		_sqliteFile = new File(mapPath + ".sqlite");
		_countryStyle = this.createCountryStyle();
		_stateStyle = this.createStateStyle();
		_cityStyle = this.createCityStyle();	
	}

	@Override
	public boolean requiresDownloadableAssets() {
		return true;
	}

	@Override
	public void start() {
		//Add marker map
		MarkerMapInfo mapInfo = new MarkerMapInfo();
		mapInfo.name = this.name;
		mapInfo.zOrder = 200;
		mapInfo.mapType = MapType.kMapTypeFileMarker;
		mapInfo.sqliteFileName = _sqliteFile.getAbsolutePath();
		mapInfo.markerMapDelegate = this;
		mapInfo.hitTestingEnabled = true;
		mapInfo.fadeEnabled = false;
		mapInfo.markerImageLoadingStrategy = MarkerImageLoadingStrategy.kMarkerImageLoadingAsynchronous;
		mapView.addMapUsingMapInfo(mapInfo);
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}

	@Override
	public void updateMarkerInfo(MarkerInfo markerInfo, String mapName) {

		//Create a label bitmap
		Bitmap labelBitmap = FontUtil.createLabel(markerInfo.metaData, _countryStyle);

		//Set the marker info
		markerInfo.setImage(labelBitmap);
		markerInfo.anchorPoint = new PointF(labelBitmap.getWidth()/2,
				labelBitmap.getHeight()/2);

	}

	@Override
	public void tapOnMarker(	String mapName,
			int markerUid,
			String markerMetaData,
			float markerWeight,
			PointF geographicLocation,
			PointF screenPoint,
			PointF markerPoint){

		Log.w("Places","Marker was tapped on. uid:" + markerUid + 
				" metaData:" + markerMetaData + 
				" weight:" + markerWeight +
				" location:" + geographicLocation.x + "," + geographicLocation.y +
				" screenPoint:" + screenPoint.x + "," + screenPoint.y +
				" markerPoint:" + markerPoint.x + "," + markerPoint.y);

		//Get visible markers
		Log.w("getVisibleMarkers","Being called");
		mapView.getVisibleMarkers(mapName, new MarkerInfoReceiver(){
			public void receiveMarkerInfo(MarkerInfo markerInfoArray[]){
				if(markerInfoArray!=null){
					Log.w("Visible Marker Count", ""+markerInfoArray.length);
					for(int i=0; i<markerInfoArray.length; i++){
						Log.w("Marker: " + markerInfoArray[i].uid, markerInfoArray[i].metaData);
					}
				}
			}});

	}

	public LabelStyle createCountryStyle() {
		LabelStyle s = new LabelStyle();
		s.fontName = "Arial";
		s.fontStyle = Typeface.BOLD;
		s.fontSize=20;
		s.strokeVisible = true;
		s.strokeColor = Color.WHITE;
		s.fillColor = Color.BLACK;
		return s;
	}

	public LabelStyle createStateStyle() {
		LabelStyle s = new LabelStyle();
		s.fontName = "Arial";
		s.fontStyle = Typeface.NORMAL;
		s.fontSize=14;
		s.strokeVisible = true;
		s.strokeColor = Color.WHITE;
		s.fillColor = Color.rgb(130, 130, 130);
		return s;
	}

	public LabelStyle createCityStyle() {
		LabelStyle s = new LabelStyle();
		s.fontName = "Arial";
		s.fontStyle = Typeface.NORMAL;
		s.fontSize=13;
		s.strokeVisible = true;
		s.strokeColor = Color.WHITE;
		s.fillColor = Color.BLACK;
		return s;
	}

}