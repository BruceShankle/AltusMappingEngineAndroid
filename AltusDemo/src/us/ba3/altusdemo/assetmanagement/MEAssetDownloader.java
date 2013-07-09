package us.ba3.altusdemo.assetmanagement;
import android.app.Activity;
import android.util.Log;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.*;
import java.io.*;
import java.util.ArrayList;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;
import us.ba3.altusdemo.METestManager;
import us.ba3.altusdemo.MEUnzipper;
import us.ba3.altusdemo.R;

public class MEAssetDownloader {

	protected Activity _activity;
	protected ProgressDialog _progressDialog;
	protected METestManager _testManager;
	protected String _testName;
	protected MEAssetManager _assetManager;
	
	public MEAssetDownloader(Activity activity,
			METestManager testManager,
			MEAssetManager assetManager,
			String testName) {
		_activity = activity;
		_testManager = testManager;
		_assetManager = assetManager;
		_testName = testName;
	}

	public void showDialog() {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(_activity);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(R.string.downloads_dialog_message)
		.setTitle(R.string.downloads_dialog_title);

		builder.setPositiveButton(R.string.downloads_dialog_positive,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				MEAssetDownloader.this.performDownloads();
			}
		});

		builder.setNegativeButton(R.string.downloads_dialog_negative,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public void showDownloadSuccessDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
		builder.setMessage(R.string.downloads_success)
		.setTitle(R.string.downloads_dialog_title);

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public void showDownloadFailDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
		builder.setMessage(R.string.downloads_fail)
		.setTitle(R.string.downloads_dialog_title);

		builder.setPositiveButton(R.string.downloads_dialog_positive,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				MEAssetDownloader.this.performDownloads();
			}
		});

		builder.setNegativeButton(R.string.downloads_dialog_negative,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void performDownloads() {
		_activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   
		_progressDialog = new ProgressDialog(_activity);
		_progressDialog.setMessage("Downloading maps..");
		_progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		_progressDialog.setCancelable(false);
		_progressDialog.show();
		new DownloadAssetsAsync().execute("");
	}

	class DownloadAssetsAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {
				ArrayList<MEDownloadableAsset> assets = _assetManager.getDownloadableAssets();
				int fileCount = 0;
				int totalFiles = assets.size();
				for(MEDownloadableAsset asset : assets) {
					if(!_assetManager.validateDownloadableAsset(asset)){
						//Download the asset.
						downloadAsset(asset);
						fileCount++;
						publishProgress("",""+(int)((fileCount*100)/totalFiles));
					}
				}
			} catch (Exception e) {
				Log.w("", e.getMessage());
			}
			return null;
		}

		public boolean downloadAsset(MEDownloadableAsset asset){
			try{
				//Create folder for file
				File targetFolder = new File(_activity.getExternalFilesDir(null), asset.targetFolder);
				targetFolder.mkdirs();
				File outFile = new File(targetFolder, asset.targetFileName);
				
				//Download the file
				publishProgress("Downloading " + asset.targetFileName);
				//+ "(" + String.format("%.2f", asset.fileSize / 1048576)+" MB)");
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(asset.sourceURL);
				httpClient.execute(httpGet).getEntity().writeTo(new FileOutputStream(outFile));
				
				//Unzip the file?
				if(asset.isArchive) {
					publishProgress("Extracting " + asset.targetFileName + ". Please wait...");
					MEUnzipper unzipper = new MEUnzipper(outFile.getAbsolutePath(),
							targetFolder.getAbsolutePath());
					unzipper.unzip();
					outFile.delete();
				}
			}
			catch(Exception ex){
				return false;
			}
			return true;
		}

		protected void onProgressUpdate(String... progress) {
			_progressDialog.setMessage(progress[0]);
			if(progress.length>1){
				_progressDialog.setProgress(Integer.parseInt(progress[1]));
			}
		}

		@Override
		protected void onPostExecute(String unused) {
			_progressDialog.dismiss();
			if(_assetManager.validateAssets()){
				showDownloadSuccessDialog();
				if(_testManager!=null && _testName !=null) {
					_testManager.startTest(_testName);
				}
			}else{
				showDownloadFailDialog();
			}
		}
	}
}
