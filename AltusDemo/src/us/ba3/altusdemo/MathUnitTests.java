package us.ba3.altusdemo;
import android.graphics.PointF;
import android.util.Log;
import junit.framework.Assert;
import us.ba3.me.*;
import us.ba3.me.Math;
public class MathUnitTests {
	
	public static void RunTests(){
		
		PointF coord3857 = Math.mercatorTo3857(new PointF(45,-45));
		Log.w("Math.mercatorTo3857(45,-45)= ",""+coord3857.y+", "+coord3857.x);
		
		Assert.assertEquals(180.0, Math.toDegrees(java.lang.Math.PI));
		Log.w("Math.toDegrees(java.lang.Math.PI)=", ""+Math.toDegrees(java.lang.Math.PI));
		
		Assert.assertEquals(java.lang.Math.PI, Math.toRadians(180.0));
		Log.w("Math.toRadians(180.0)= ",""+ Math.toRadians(180.0));
		
		//Distance
		double radianDist = Math.distanceBetween(0, 0, 0, 0);
		Log.w("Math.distanceBetween(0, 0, 0, 0)= ",""+radianDist);
		
		radianDist = Math.distanceBetween(0, 0, java.lang.Math.PI/2, 0);
		Log.w("Math.distanceBetween(0, 0, PI/2, 0)= ",""+radianDist);
		
		double nauticalMilesDist = Math.nauticalMilesBetween(new PointF(-45,0), new PointF(-44,0));
		Log.w("Math.nauticalMilesBetween(new PointF(-45,0), new Pointf(-44,0))= ",""+nauticalMilesDist);
		
		//1 nautical mile is 1852 meters
		Assert.assertEquals(1852.0, Math.nauticalMilesToMeters(1));
		Log.w("Math.nauticalMilesToMeters(1)=",""+Math.nauticalMilesToMeters(1));
		
		//1852 meters is 1 nautical mile
		Assert.assertEquals(1.0, Math.metersToNauticalMiles(1852), 0.0001);
		Log.w("Math.metersToNauticalMiles(1852)= ",""+Math.metersToNauticalMiles(1852));
		
		PointF pointOnRadial = Math.pointOnRadial(new PointF(0,0), 90, 60);
		Log.w("Math.pointOnRadial(new PointF(0,0), 90, 60)= ",""+pointOnRadial.x + "," +pointOnRadial.y);
		
		Location locationOnRadial=Math.locationOnRadial(new Location(0,0), 90, 60);
		Log.w("Math.locationOnRadial(new Location(0,0), 90, 60)",""+locationOnRadial.longitude + "," + locationOnRadial.latitude);
		
		double course = Math.courseFromPoint(new PointF(0,0), new PointF(1,1));
		Log.w("Math.courseFromPoint(new PointF(0,0), new PointF(1,1))= ", ""+course+" degrees");
		
		course = Math.courseFromLocation(new Location(0,0), new Location(1,1));
		Log.w("Math.courseFromLocation(new Location(0,0), new Location(1,1))= ", ""+course+" degrees");
		
		PointF pointBetween = Math.pointBetween(new PointF(0,0), new PointF(0,1), 0.5);
		Log.w("Math.pointBetween(new PointF(0,0), new PointF(0,1), 0.5)= ",""+pointBetween.x + "," +pointBetween.y);
	
		Log.w("Math.getEarthCircumferenceInMeters()= ",""+Math.getEarthCircumferenceInMeters());
		Log.w("Math.getEarthCircumferenceInNauticalMiles()= ", ""+Math.getEarthCircumferenceInNauticalMiles());
		Log.w("Math.getEarthRadiusInMeters()= ",""+Math.getEarthRadiusInMeters());
	}
}
