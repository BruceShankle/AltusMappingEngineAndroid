package us.ba3.altusdemo.tileworkertests;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import us.ba3.me.BitmapSimple;
import us.ba3.me.virtualmaps.*;

public class BitmapTileWorker extends TileWorker{

	private Bitmap circleBitmap;
	BitmapTileWorker(){
		//Create a red circle bitmap
		this.circleBitmap = CreateCircle(256, 0xffff0000);
	}
	
	/**Creates a circle of the specified diameter and color.*/
	public Bitmap CreateCircle(int diameter, int color){
		Bitmap bitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		Paint p = new Paint();
		Path circlePath = new Path();
		p.setAntiAlias(true);
		p.setColor(color);
		circlePath.addCircle(diameter/2.0f, diameter/2.0f, diameter/2.0f, Path.Direction.CW);
		c.drawPath(circlePath,p);
		return bitmap;
	}
	
	@Override
	public void doWork(TileProviderRequest request){
		request.image = new BitmapSimple(this.circleBitmap);
		request.responseType = TileProviderResponse.kTileResponseRenderImage;
	}
}
