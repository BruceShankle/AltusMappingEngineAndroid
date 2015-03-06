package us.ba3.altusdemo.demos.lighting;

import us.ba3.me.MapView;
import android.content.Context;
import android.view.ViewGroup;

public class AtmosphereExposure extends Sunrise {
	private float rayleigh;
	private float mie;
	private float planet;
	
	public AtmosphereExposure(){
		this.name = "Atmosphere Exposure";
	}
	
	@Override
	public void start(MapView mapView, Context context, ViewGroup viewGroup) {
		rayleigh = 0.4f;
		mie = 0.4f;
		planet = 0.4f;
		super.start(mapView, context, viewGroup);
	}
	
	@Override
	public void timerTick(){
		rayleigh = rayleigh + 0.05f;
		if(rayleigh>=3){
			rayleigh = 0;
		}
		mapView.setExposureAtmosphereRayleigh(rayleigh, mie, planet);
		super.timerTick();
	}
}
