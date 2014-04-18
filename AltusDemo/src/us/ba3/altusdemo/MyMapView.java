package us.ba3.altusdemo;

import us.ba3.me.ConvertPointCallback;
import us.ba3.me.Location;
import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

public class MyMapView extends us.ba3.me.MapView {

	public MyMapView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	
		//This is how you capture touch events....
		
		//This is how you look up the geographic location of a touch event
		/*
		super.getLocationForPoint(new PointF(event.getX(), event.getY()), new ConvertPointCallback(){
			@Override
			public void convertComplete(Location loc) {
				Log.w("onTouch", "lon:" + loc.longitude + " lat:" + loc.latitude);
			}});
		*/
		
		//Always pass to pass class....
		return super.onTouchEvent(event);
	}

}
