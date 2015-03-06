package us.ba3.altusdemo.demos;
import java.io.File;

import us.ba3.altusdemo.METestManager;
import us.ba3.me.*;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Demo {
	protected String name;
	protected MapView mapView;
	protected Context context;
	protected ViewGroup viewGroup;
	protected LinearLayout buttonRow;
	public String getName() { 
		return name; 
	}
	
	public void setName(String name){
		this.name = name;
	}

	public void start(MapView mapView, Context context, ViewGroup viewGroup) {
		this.mapView = mapView;
		this.context = context;
		this.viewGroup = viewGroup;
	}
	
	public String getMapPath(String category, String mapName) {
		File mapFolder = new File(context.getExternalFilesDir(null), category);
		String mapPath = mapFolder.getAbsolutePath() + "/" + mapName;
		Log.w("mapPath", mapPath);
		return mapPath;
	}
	
	public void createUserInterface(ViewGroup viewGroup){
		if(this.buttonRow==null){
			this.viewGroup = viewGroup;
			this.buttonRow = new LinearLayout(this.context);
			this.buttonRow.setOrientation(LinearLayout.VERTICAL);
			this.viewGroup.addView(this.buttonRow);
		}
	}

	public void stop(){
		if(this.buttonRow!=null){
			this.viewGroup.removeView(this.buttonRow);
		}
	}
}
