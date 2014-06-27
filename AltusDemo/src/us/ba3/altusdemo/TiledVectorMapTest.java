	package us.ba3.altusdemo;
	
	import us.ba3.me.Location;
	import us.ba3.me.styles.LineStyle;
	import us.ba3.me.styles.PolygonStyle;
	import us.ba3.me.virtualmaps.TileProvider;
	import us.ba3.me.virtualmaps.TileProviderRequest;
	import us.ba3.me.virtualmaps.TileProviderResponse;
	import us.ba3.me.virtualmaps.VirtualMapInfo;
	import us.ba3.me.vector.*;
	import android.graphics.Color;
	import android.os.Looper;
	import android.util.Log;
	
	public class TiledVectorMapTest extends METest implements TileProvider {
	
		public TiledVectorMapTest(String name) {
			this.name = name;
		}
	
		@Override
		public void start() {
			VirtualMapInfo mapInfo = new VirtualMapInfo();
			mapInfo.name = name;
			mapInfo.zOrder = 100;
			mapInfo.isVector = true;
			mapInfo.setTileProvider(this);
			mapView.addMapUsingMapInfo(mapInfo);
			mapView.setMapZOrder(name, 100);
	
			LineStyle lineStyle = new LineStyle();
			lineStyle.strokeWidth = 5;
			lineStyle.strokeColor = Color.argb(128, 150, 200, 150);
			lineStyle.outlineWidth = 0;
			mapView.setLineStyleOnVectorMap(name, 1, lineStyle);
	
			PolygonStyle polygonStyle = new PolygonStyle();
			polygonStyle.strokeWidth = 2;
			polygonStyle.strokeColor = Color.argb(128, 200, 200, 200);
			polygonStyle.outlineWidth = 0;
			polygonStyle.fillColor = Color.argb(128, 150, 200, 150);
			mapView.setPolygonStyleOnVectorMap(name, 5, polygonStyle);
	
			PolygonStyle style = new PolygonStyle();
			style.strokeWidth = 0;
			style.fillColor = Color.argb(255, 150, 150, 200);
			mapView.setPolygonStyleOnVectorMap(name, 2, style);
		}
	
		@Override
		public void stop() {
			mapView.removeMap(this.name, false);
		}
	
		@Override
		public void requestTile(TileProviderRequest request) {
	
			Location min = request.bounds.min;
			Location max = request.bounds.max;
	
			GeometryGroup geometry = new GeometryGroup();
	
			// add a line
			geometry.lines = new Line[1];
			geometry.lines[0] = new Line();
			geometry.lines[0].shapeId = 1;
			geometry.lines[0].coordinates = new Location[] {
					min,
					max
			};
	
			double width = max.longitude - min.longitude;
			double height = max.latitude - min.latitude;
			Location size = new Location(height, width);	
	
			geometry.polygons = new Polygon[2];
	
			// add a polygon that covers the tile
			geometry.polygons[0] = new Polygon();
			geometry.polygons[0].shapeId = 2;
			geometry.polygons[0].shell = new Location[] {
					min,
					new Location(min.latitude + size.latitude, min.longitude),
					max,
					new Location(min.latitude, min.longitude + size.longitude),
					min
			};
	
			// add a polygon that is in the center of the tile
			Location start = new Location(min.latitude + height/4.0, min.longitude + width/4.0);
			geometry.polygons[1] = new Polygon();
			geometry.polygons[1].shapeId = 5;
			geometry.polygons[1].shell = new Location[] {
					start,
					new Location(start.latitude + size.latitude/2.0, start.longitude),
					new Location(start.latitude + size.latitude/2.0, start.longitude + size.longitude/2.0),
					new Location(start.latitude, start.longitude + size.longitude/2.0),
					start
			};
	
			// call this on the main thread to finish a tile request
			mapView.vectorTileLoadComplete(request, geometry);
		}
	}
