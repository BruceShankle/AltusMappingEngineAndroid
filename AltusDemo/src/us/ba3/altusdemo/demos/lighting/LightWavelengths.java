package us.ba3.altusdemo.demos.lighting;

import us.ba3.me.Location3D;
import us.ba3.me.MapView;
import android.content.Context;
import android.view.ViewGroup;

public class LightWavelengths extends Sunrise {
	private float red;
	private float green;
	private float blue;
	
	public LightWavelengths(){
		this.name = "Light Wavelengths";
	}
	
	@Override
	public void start(MapView mapView, Context context, ViewGroup viewGroup) {
		red = 0.650f;
		green = 0.570f;
		blue = 0.475f;
		super.start(mapView, context, viewGroup);
	}
	
	@Override
	public void timerTick(){
		red = red + 0.05f;
		if(red>=1){
			red = 0;
		}
		mapView.setLightWavelengths(red, green, blue);
		super.timerTick();
	}
}
