package us.ba3.altusdemo.demos;
import java.util.ArrayList;
import us.ba3.me.*;
import android.content.Context;
import us.ba3.altusdemo.demos.camera.*;

public class DemoManager {
	private Demo currentDemo;
	private ArrayList<Demo> demoList;
	private MapView mapView;
	private Context context;

	public DemoManager(MapView mapView, Context context) {
		this.mapView = mapView;
		this.context = context;
		demoList = new ArrayList<Demo>();
		demoList.add(new SpinningGlobe());
		demoList.add(new FlightPlayback());
		demoList.add(new FlightPlaybackTrackUp());
		demoList.add(new FlightPlaybackTrackUpForward());
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
		
		demo.start(mapView, context);
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
