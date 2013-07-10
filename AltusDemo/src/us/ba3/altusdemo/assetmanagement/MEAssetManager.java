package us.ba3.altusdemo.assetmanagement;

import android.app.Activity;
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
				if(!validateAsset(internalAsset)){
					return false;
				}
			}
			return true;
		}
		else{
			return validateAsset(asset);
		}
	}
	
	public boolean validateAsset(MEFileAsset asset) {
		File targetFolder = new File(_activity.getExternalFilesDir(null), asset.targetFolder);
		if(!targetFolder.exists()) {
			return false;
		}
		File targetFile = new File(targetFolder, asset.targetFileName);
		if(!targetFile.exists()) {
			return false;
		}
		if(targetFile.length()!=asset.fileSize){
			return false;
		}
		return true;
	}
	
	
}
