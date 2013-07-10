package us.ba3.altusdemo.demos.camera;

import us.ba3.me.*;
import us.ba3.me.markers.MarkerRotationType;
import android.content.Context;

public class FlightPlaybackTrackUp extends FlightPlayback {
	
	public FlightPlaybackTrackUp(){
		this._name="Flight Playback - Track Up";
		_markerRotationType = MarkerRotationType.kMarkerRotationScreenEdgeAligned;
	}
	
	@Override
	public void updateCamera(){
		this._mapView.setLocation(_currentSample.getLocation(), this.interval);
		this._mapView.setCameraOrientation(_currentSample.hdg,
				_currentSample.roll,
				_currentSample.pitch,
				this.interval);
	}
	
	@Override
	public void updateOwnShipMarker(){
		this._mapView.setDynamicMarkerLocation("blueplane",
				"blueplane",
				_currentSample.getLocation(),
				this.interval);
	}
	
	@Override
	public void start(MapView mapView, Context context) {
		mapView.setTrackUp(true);
		super.start(mapView, context);
	}
	
	@Override
	public void stop(){
		_mapView.setCameraOrientation(0,0,0,this.interval);
		_mapView.setTrackUp(false);
		super.stop();
	}
}
