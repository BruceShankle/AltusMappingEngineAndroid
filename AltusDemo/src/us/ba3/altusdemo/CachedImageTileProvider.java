package us.ba3.altusdemo;
import us.ba3.me.virtualmaps.*;

public class CachedImageTileProvider implements TileProvider {
	
	public String cachedImageName;
	
	public CachedImageTileProvider(String cachedImageName){
		this.cachedImageName = cachedImageName;
	}
	
	public void requestTile(TileProviderRequest request) {
		request.responseType = TileProviderResponse.kTileResponseRenderNamedCachedImage;
		request.cachedImageName = this.cachedImageName;
	}
}
