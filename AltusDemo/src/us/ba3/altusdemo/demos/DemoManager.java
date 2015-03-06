package us.ba3.altusdemo.demos;
import java.util.ArrayList;
import us.ba3.me.*;
import android.content.Context;
import android.view.ViewGroup;
import us.ba3.altusdemo.demos.camera.*;
import us.ba3.altusdemo.demos.lighting.AtmosphereExposure;
import us.ba3.altusdemo.demos.lighting.LightWavelengths;
import us.ba3.altusdemo.demos.lighting.Sunrise;

public class DemoManager {
	private Demo currentDemo;
	private ArrayList<Demo> demoList;
	private MapView mapView;
	private Context context;
	protected ViewGroup viewGroup;

	public DemoManager(MapView mapView, Context context, ViewGroup viewGroup) {
		this.mapView = mapView;
		this.context = context;
		this.viewGroup = viewGroup;
		demoList = new ArrayList<Demo>();
		demoList.add(new SpinningGlobe());
		demoList.add(new FlightPlayback());
		demoList.add(new Sunrise());
		demoList.add(new LightWavelengths());
		demoList.add(new AtmosphereExposure());
	}

	public Demo findDemo(String name) {
		for (Demo demo : demoList) {
			if (demo.getName() == name) {
				return demo;
			}
		}
		return null;
	}

	public void startDemo(String name) {
		
		if (currentDemo != null){
			currentDemo.stop();
		}
		
		Demo demo = this.findDemo(name);
		if(demo==null){
			return;
		}
		
		demo.start(mapView, context, viewGroup);
		currentDemo = demo;
	}

	public String[] getDemoNames() {
		String names[] = new String[demoList.size() + 1];
		names[0] = "Demos";
		for (int i = 0; i < demoList.size(); ++i)
			names[i+1] = demoList.get(i).getName();
		return names;
	}
}
