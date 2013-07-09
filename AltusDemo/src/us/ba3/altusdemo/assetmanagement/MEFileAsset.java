package us.ba3.altusdemo.assetmanagement;

public class MEFileAsset {
	public String targetFolder;
	public String targetFileName;
	public long fileSize;
	
	public MEFileAsset(){	
	}
	
	public MEFileAsset(String targetFolder, String targetFileName, long fileSize){
		this.targetFolder = targetFolder;
		this.targetFileName = targetFileName;
		this.fileSize = fileSize;
	}
}
