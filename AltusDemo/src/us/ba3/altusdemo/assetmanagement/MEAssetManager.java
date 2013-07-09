package us.ba3.altusdemo.assetmanagement;

import android.app.Activity;
import android.util.Log;
import java.io.*;
import java.util.*;

public class MEAssetManager {
	protected Activity _activity;
	protected ArrayList<MEDownloadableAsset> _downloadableAssets = new ArrayList<MEDownloadableAsset>();
	public boolean assetsValid = false;
	public MEAssetManager(Activity activity){
		_activity  = activity;
	}
	
	public ArrayList<MEDownloadableAsset> getDownloadableAssets() {
		return _downloadableAssets;
	}
	
	public void addDownloadableAsset(MEDownloadableAsset asset) {
		_downloadableAssets.add(asset);
	}
	
	public boolean validateAssets() {
		for (MEDownloadableAsset asset : _downloadableAssets) {
			if(!validateDownloadableAsset(asset)){
				this.assetsValid = false;
				return false;
			}
		}
		this.assetsValid = true;
		return true;
	}
	
	public boolean validateDownloadableAsset(MEDownloadableAsset asset) {
		if(asset.isArchive) {
			for(MEFileAsset internalAsset : asset.archiveContents) {
				Log.w("i0",internalAsset.targetFileName);
				if(!validateAsset(internalAsset)){
					Log.w("i1",internalAsset.targetFileName);
					return false;
				}
			}
			return true;
		}
		else{
			Log.w("foo",asset.targetFileName);
			return validateAsset(asset);
		}
	}
	
	public boolean validateAsset(MEFileAsset asset) {
		File targetFolder = new File(_activity.getExternalFilesDir(null), asset.targetFolder);
		if(!targetFolder.exists()) {
			Log.w("i2",asset.targetFileName);
			return false;
		}
		File targetFile = new File(targetFolder, asset.targetFileName);
		if(!targetFile.exists()) {
			Log.w("i3",asset.targetFileName);
			return false;
		}
		if(targetFile.length()!=asset.fileSize){
			Log.w("i4",asset.targetFileName + " Expected" + asset.fileSize + " bytes");
			//Delete failed download.
			//targetFile.delete();
			return false;
		}
		return true;
	}
	
	
}
