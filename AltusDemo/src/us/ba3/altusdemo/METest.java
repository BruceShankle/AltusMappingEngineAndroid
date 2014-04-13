package us.ba3.altusdemo;
import android.util.Log;
import java.io.*;
import us.ba3.me.*;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.common.io.*;

public abstract class METest {
	
	protected String name;
	protected MapView mapView;
	protected Context context;
	public static String internalFilesPath;
	
	public String getName() { 
		return name; 
	}
	
	public void start(MapView mapView, Context context)
	{
		this.mapView = mapView;
		this.context = context;
		//Show version information
		Log.w("Altus Version Tag", mapView.getVersionTag());
		Log.w("Altus Version Hash", mapView.getVersionHash());
		start();
	}
	
	public void cachePngAsset(String assetPath, String imageName, boolean compressTexture){
		try{
			//Load grayGrid.png as a cached image
			byte pngData[]=ByteStreams.toByteArray(context.getAssets().open(assetPath));
			mapView.addCachedPngImage(imageName, pngData, compressTexture);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	//**Extracts an asset to the internal storage, returning the full path of the file, optionally overwriting.*/
	public String extractAssetToInternalStorage(String assetPath, String targetFileName, boolean overWrite){
		File destFile = new File(METest.internalFilesPath, targetFileName);
		if(destFile.exists() && !overWrite){
			return destFile.getAbsolutePath();
		}
		try{
			byte fileData[] = ByteStreams.toByteArray(context.getAssets().open(assetPath));
			Log.w("METest", "Writing " + fileData.length + " bytes to " + destFile.getAbsolutePath());
			com.google.common.io.Files.write(fileData, destFile);
			return destFile.getAbsolutePath();
		} catch(IOException e){
			Log.w("METest", e.getMessage());
			e.printStackTrace();
		}
		return "";
 	}
	
	public Bitmap getBitmapFromAsset(String strName) {
	    AssetManager assetManager = this.context.getAssets();

	    InputStream istr;
	    Bitmap bitmap = null;
	    try {
	        istr = assetManager.open(strName);
	        bitmap = BitmapFactory.decodeStream(istr);
	    } catch (IOException e) {
	        return null;
	    }

	    return bitmap;
	}
	
	public boolean requiresDownloadableAssets() {
		return false;
	}
	
	protected abstract void start();
	public abstract void stop();
}
