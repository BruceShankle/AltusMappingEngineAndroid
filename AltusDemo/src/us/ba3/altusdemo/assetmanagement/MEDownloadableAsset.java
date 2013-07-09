package us.ba3.altusdemo.assetmanagement;
import java.util.ArrayList;

public class MEDownloadableAsset extends MEFileAsset {
	public String sourceURL = "";
	public boolean isArchive = false;
	public ArrayList<MEFileAsset> archiveContents = new ArrayList<MEFileAsset>();
	public MEDownloadableAsset(){}
	public MEDownloadableAsset(String sourceURL, String targetFolder, String targetFileName, boolean isArchive, long fileSize){
		this.sourceURL = sourceURL;
		this.targetFolder = targetFolder;
		this.targetFileName = targetFileName;
		this.isArchive = isArchive;
		this.fileSize = fileSize;
	}
	
	public void addArchiveFile(MEFileAsset fa) {
		archiveContents.add(fa);
	}
}
