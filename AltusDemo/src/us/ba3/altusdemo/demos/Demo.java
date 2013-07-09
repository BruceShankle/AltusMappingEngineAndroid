package us.ba3.altusdemo.demos;
import us.ba3.me.*;
import android.content.Context;

public class Demo {
	protected String _name;
	protected MapView _mapView;
	protected Context _context;
	
	public String getName() { 
		return _name; 
	}
	
	public void setName(String name){
		this._name = name;
	}

	public void start(MapView mapView, Context context) {
		this._mapView = mapView;
		this._context = context;
	}

	public void stop(){
	}
}
