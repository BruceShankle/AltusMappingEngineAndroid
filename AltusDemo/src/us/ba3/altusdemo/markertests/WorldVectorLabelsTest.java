package us.ba3.altusdemo.markertests;
import java.io.File;
import android.util.Log;
import us.ba3.altusdemo.METest;
import us.ba3.me.MapType;
import us.ba3.me.markers.*;
import us.ba3.me.util.*;
import us.ba3.me.styles.*;
import android.graphics.*;

public class WorldVectorLabelsTest extends METest implements MarkerMapDelegate {

	File _sqliteFile;
	LabelStyle _countryStyle;
	LabelStyle _stateStyle;
	LabelStyle _cityStyle;
	
	public WorldVectorLabelsTest(String name, String mapPath){
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
	protected void start() {
		//Add marker map
		MarkerMapInfo mapInfo = new MarkerMapInfo();
		mapInfo.name = this.name;
		mapInfo.zOrder = 100;
		mapInfo.mapType = MapType.kMapTypeFileMarker;
		mapInfo.sqliteFileName = _sqliteFile.getAbsolutePath();
		mapInfo.markerMapDelegate = this;
		mapInfo.hitTestingEnabled = true;
		mapInfo.markerImageLoadingStrategy = MarkerImageLoadingStrategy.kMarkerImageLoadingAsynchronous;
		mapView.addMapUsingMapInfo(mapInfo);
	}

	@Override
	public void stop() {
		mapView.removeMap(this.name, false);
	}

	@Override
	public void updateMarkerInfo(MarkerInfo markerInfo, String mapName) {
		String labelText = markerInfo.metaData.substring(1, markerInfo.metaData.length());
		LabelStyle labelStyle = null;
		if(markerInfo.metaData.startsWith("C")) {
			labelStyle = _countryStyle;
		}
		if(markerInfo.metaData.startsWith("S")) {
			labelStyle = _stateStyle;
		}
		if(markerInfo.metaData.startsWith("c")) {
			labelStyle = _cityStyle;
		}
		
		//Create a label bitmap
		Bitmap labelBitmap = FontUtil.createLabel(labelText, labelStyle);
		
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
		Log.w("WorldVectorLabelsTest","Marker was tapped on. uid:" + markerUid + 
				" metaData:" + markerMetaData + 
				" weight:" + markerWeight +
				" location:" + geographicLocation.x + "," + geographicLocation.y +
				" screenPoint:" + screenPoint.x + "," + screenPoint.y +
				" markerPoint:" + markerPoint.x + "," + markerPoint.y);
	}
	
	public LabelStyle createCountryStyle() {
		LabelStyle s = new LabelStyle();
		s.fontName = "Arial";
		s.fontStyle = Typeface.BOLD;
		s.fontSize=17;
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