package us.ba3.altusdemo.internetmaptests;

import us.ba3.altusdemo.METest;

public class StreamingTerrain extends METest {
		
		public StreamingTerrain(){
			this.name="Streaming Terrain";
		}
		
		@Override
		public void start() {
			mapView.addStreamingTerrainMap(this.name,
					"http://dev1.ba3.us/maps/terrain/pngtiles/{z}/{x}/{y}.png", //Center tiles
					"http://dev1.ba3.us/maps/terrain/pngtiles/altn/{id}.png", //North tiles
					"http://dev1.ba3.us/maps/terrain/pngtiles/alts/{id}.png", //South tiles
					"", 		//Subdomains
					11,			//Max Level
					2,			//zOrder
					3,			//Number of simultaneous downloads
					true		//Use cache
					);
		}

		@Override
		public void stop() {
			mapView.removeMap(this.name, false);
		}
}
