package us.ba3.altusdemo;

import android.util.Log; 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.util.zip.ZipEntry; 
import java.util.zip.ZipInputStream; 

public class MEUnzipper { 
	private String _zipFile; 
	private String _location; 

	public MEUnzipper(String zipFile, String location) { 
		_zipFile = zipFile; 
		_location = location;
		if(!_location.endsWith("/")){
			_location+="/";
		}
		Log.w("MEUnzipper", _zipFile + "->" + _location);
		_dirChecker(""); 
	} 

	public void unzip() { 
		try  { 
			FileInputStream fin = new FileInputStream(_zipFile); 
			ZipInputStream zin = new ZipInputStream(fin); 
			ZipEntry ze = null; 
			while ((ze = zin.getNextEntry()) != null) { 
				Log.v("Decompress", "Unzipping " + ze.getName()); 

				if(ze.isDirectory()) { 
					_dirChecker(ze.getName()); 
				} else { 
					FileOutputStream fout = new FileOutputStream(_location + ze.getName());
					
					byte[] buffer = new byte[65536];
			        int len;
			        while ((len = zin.read(buffer)) != -1) {
			            fout.write(buffer, 0, len);
			        }
			        
					zin.closeEntry(); 
					fout.close(); 
				} 

			} 
			zin.close(); 
		} catch(Exception e) { 
			Log.e("Decompress", "unzip", e); 
		} 

	} 

	private void _dirChecker(String dir) { 
		File f = new File(_location + dir); 

		if(!f.isDirectory()) { 
			f.mkdirs(); 
		} 
	} 
} 