package com.example.mbtiles;

import java.io.File;
import us.ba3.me.ImageDataType;
import us.ba3.me.Location;
import us.ba3.me.MapLoadingStrategy;
import us.ba3.me.MapView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Extract the mbtiles map asset if needed
		String sourceAssetName = "open-streets-dc.mbtiles";
		String targetFileName = this.getFilesDir().getAbsolutePath() + File.separator + sourceAssetName;
		File destFile = new File(targetFileName);
		if(!destFile.exists()) {
			AssetExtractor extractor = new AssetExtractor(this,sourceAssetName,targetFileName);
			extractor.extractAsset(false);
		}
		
		//Add the mb tiles map.
		MapView mapView = (MapView)this.findViewById(R.id.mapView1);
		
		mapView.addMBTilesMap("DC", targetFileName, "grayGrid", ImageDataType.kImageDataTypePNG, false, 2,
				MapLoadingStrategy.kLowestDetailFirst);

        	mapView.setLocationThatFitsCoordinates(
        			new Location(38.872325,-77.102248),
        			new Location(38.920421,-76.965606),
        			0, 0);
        	
        	
        	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
