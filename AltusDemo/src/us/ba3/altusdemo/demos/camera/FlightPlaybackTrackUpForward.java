package us.ba3.altusdemo.demos.camera;

import android.content.Context;
import us.ba3.me.*;
import us.ba3.me.markers.MarkerRotationType;

public class FlightPlaybackTrackUpForward extends FlightPlaybackTrackUp {

	public FlightPlaybackTrackUpForward(){
		this._name="Flight Playback - Track Up Forward";
		_markerRotationType = MarkerRotationType.kMarkerRotationScreenEdgeAligned;
	}
	
	@Override
	public void start(MapView mapView, Context context) {
		super.start(mapView, context);
		mapView.setTrackUpForwardDistance(200, this.interval);
	}
	
	@Override
	public void stop(){
		_mapView.setTrackUpForwardDistance(0, this.interval);
		_mapView.setCameraOrientation(0,0,0,this.interval);
		_mapView.setTrackUpForwardDistance(0, this.interval);
		super.stop();
	}
}
