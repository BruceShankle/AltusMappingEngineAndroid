package us.ba3.altusdemo.demos.camera;
import us.ba3.altusdemo.demos.*;
import us.ba3.me.*;

public class SpinningGlobe extends DemoWithTimer {
	
	Location3D _location;
	
	public SpinningGlobe(){
		//this.interval = 0.25f;
		this._name = "Spinning Globe";
		_location = new Location3D();
		_location.latitude = 0f;
		_location.longitude = 180f;
		_location.altitude = 25000000;
	}

	@Override
	public void timerTick(){
		this._mapView.setLocation3D(_location, this.interval);
		_location.longitude = _location.longitude - 20;
		if(_location.longitude<-180){
			_location.longitude += 360;
		}

	}
}
