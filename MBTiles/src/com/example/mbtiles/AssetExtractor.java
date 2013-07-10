package com.example.mbtiles;
import android.app.Activity;
import android.util.Log;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.*;
import java.io.*;
import com.google.common.io.ByteStreams;

public class AssetExtractor {

	protected Activity activity;
	protected String sourceAssetName;
	protected String targetFileName;
	protected AlertDialog dialog;
	protected boolean overwriteExistingFile;
	
	public AssetExtractor(Activity activity,
			String sourceAssetName,
			String targetFileName) {
		this.activity = activity;
		this.sourceAssetName = sourceAssetName;
		this.targetFileName = targetFileName;
	}

	public void extractAsset(boolean overwriteExistingFile) {
		
		this.overwriteExistingFile = overwriteExistingFile;
		
		//Make sure activity stays on...
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		//  Create a dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
		builder.setMessage("Extracting " + sourceAssetName + ". Please wait...")
			.setTitle("Extracting Asset");
		this.dialog = builder.create();
		this.dialog.show();
		
		//Extract the asset
		new ExtractAsync().execute("");
	}

	class ExtractAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {
				extractAssetToInternalStorage(overwriteExistingFile);
			} catch (Exception e) {
				Log.w("", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String unused) {
			dialog.dismiss();
		}

		//**Extracts an asset to the internal storage, returning the full path of the file, optionally overwriting.*/
		public void extractAssetToInternalStorage(boolean overWrite){
			File destFile = new File(targetFileName);
			if(destFile.exists() && !overWrite){
				return;
			}
			try{
				byte fileData[] = ByteStreams.toByteArray(activity.getAssets().open(sourceAssetName));
				com.google.common.io.Files.write(fileData, destFile);
				return;
			} catch(IOException e){
				Log.w("Asset extraction failed.", e.getMessage());
			}
		}
	}
}
