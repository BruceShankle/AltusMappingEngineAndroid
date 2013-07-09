package us.ba3.altusdemo;
import java.util.Arrays;
import android.graphics.Color;
import android.graphics.PointF;
import android.animation.*;
import us.ba3.me.Location;
import us.ba3.me.CoordinateBounds;
import us.ba3.me.VectorMapInfo;
import us.ba3.me.styles.*;

public class VectorShapeTest extends METest implements ValueAnimator.AnimatorUpdateListener {

	String vectorMapName;
	String mapPath;
	CoordinateBounds bounds;
	float alpha;
	int zOrder;
	
	ValueAnimator animator;
	PointF[] parkRouteArray;
	PointF[] townBoundaryArray;
	
	public VectorShapeTest(String name, String mapPath, CoordinateBounds bounds) {
		this(name, mapPath, bounds, 1.0f, 100);
	}
	
	public VectorShapeTest(String name, String mapPath, CoordinateBounds bounds, float alpha) {
		this(name, mapPath, bounds, alpha, 100);
	}
	
	public VectorShapeTest(String name, String mapPath, CoordinateBounds bounds, float alpha, int zOrder) {
		this.name = name;
		this.mapPath = mapPath;
		this.bounds = bounds;
		this.alpha = alpha;
		this.zOrder = zOrder;
		this.vectorMapName = "vectorMap";
		initShapes();
	}

	@Override
	public boolean requiresDownloadableAssets() {
		return true;
	}
	
	public void onAnimationUpdate (ValueAnimator animation) {
		addLine(animation.getAnimatedFraction());
	}

	private void addLine(float t){
		int pointCount = parkRouteArray.length - 1;
		int startIndex = (int)(pointCount * t);
		int endIndex = Math.min(startIndex + 4, pointCount);
		
		PointF[] pointArray = Arrays.copyOfRange(parkRouteArray, startIndex, endIndex + 1);

		//Create a style for the flight path
		LineStyle lineStyle = new LineStyle();
		lineStyle.outlineColor = Color.argb(100, 255, 255, 255);
		lineStyle.outlineWidth = 0;
		lineStyle.strokeColor = Color.WHITE;
		lineStyle.strokeWidth = 14;
		
		//Add the line
		mapView.clearDynamicGeometryFromMap(vectorMapName);
		mapView.addDynamicLineToVectorMap(vectorMapName, "route", pointArray, lineStyle);
	}
	
	@Override
	protected void start() {
		// add a base raster map
        mapView.addMap(name, mapPath + ".sqlite", mapPath + ".map", true);
        mapView.setMapAlpha(name, alpha);
        mapView.setMapZOrder(name, zOrder);
        
        // add the vector map
		VectorMapInfo meVectorMapInfo = new VectorMapInfo();
		meVectorMapInfo.name = vectorMapName;
		meVectorMapInfo.zOrder = 800;
		mapView.addMapUsingMapInfo(meVectorMapInfo);
		
		// add and style a polygon
		PolygonStyle polygonStyle = new PolygonStyle();
		polygonStyle.fillColor = Color.argb(64, 255, 0, 0);
		polygonStyle.outlineColor = polygonStyle.strokeColor = 0;
		mapView.addPolygonToVectorMap(vectorMapName, townBoundaryArray, polygonStyle);
		
		// add a destination lines
		PointF[] lineA = new PointF[] { 
				new PointF(-68.24728259147459f,44.40944831003259f), 
				new PointF(-68.24505339498397f,44.40741561289422f) };
		PointF[] lineB = new PointF[] {
				new PointF(-68.24508098382695f,44.4091956632822f),
				new PointF(-68.24745210157219f,44.40765967267418f) };
		
		LineStyle lineStyle = new LineStyle();
		lineStyle.strokeWidth = 8;
		lineStyle.strokeColor = Color.RED;
		lineStyle.outlineColor = 0;
		mapView.addLineToVectorMap(vectorMapName, lineA, lineStyle);
		mapView.addLineToVectorMap(vectorMapName, lineB, lineStyle);
		
		// animate the line
		animator = ValueAnimator.ofFloat(0f, 1f);
		animator.setDuration(3000);
		animator.addUpdateListener(this);
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.start();

        // zoom to bounds
        if (bounds != null) {
        		mapView.lookAtCoordinates(new Location(44.3760512, -68.1945803),
        								  new Location(44.40938341192684, -68.24611502554615),
        								  200, 200, 0.5);
        }
	}

	@Override
	public void stop() {
		animator.end();
		mapView.removeMap(name, true);
		mapView.removeMap(vectorMapName, true);
	}
	
	private void initShapes() {
		parkRouteArray = new PointF[] {
				new PointF( -68.20425034080486f,44.38764284878996f),
				new PointF( -68.20427664633465f,44.38764375939765f),
				new PointF( -68.20432920072335f,44.38764573794926f),
				new PointF( -68.20657730647608f,44.3870805628603f),
				new PointF( -68.20870652812002f,44.38655704460421f),
				new PointF( -68.21029170184414f,44.38631957495898f),
				new PointF( -68.21207924538474f,44.38603108929864f),
				new PointF( -68.21318076092939f,44.38601075715042f),
				new PointF( -68.21345157728328f,44.38656370779266f),
				new PointF( -68.21392773876104f,44.38781387385528f),
				new PointF( -68.21436396706643f,44.3883590909554f),
				new PointF( -68.21530549650363f,44.38888131261904f),
				new PointF( -68.21704004287085f,44.38928346058832f),
				new PointF( -68.21812318788386f,44.38973514851259f),
				new PointF( -68.2183569192163f,44.39010521389975f),
				new PointF( -68.21875877174242f,44.3908570374663f),
				new PointF( -68.21926604388855f,44.39155569640457f),
				new PointF( -68.21993108340044f,44.39216570481639f),
				new PointF( -68.22065982008499f,44.3930095519913f),
				new PointF( -68.22130764342167f,44.39368288804165f),
				new PointF( -68.22189699677823f,44.39396836224729f),
				new PointF( -68.22268801595031f,44.39420657257293f),
				new PointF( -68.22322741654055f,44.39473225509082f),
				new PointF( -68.22508696269918f,44.39575275160491f),
				new PointF( -68.22673904484827f,44.39664169462471f),
				new PointF( -68.22851872114322f,44.39815432249696f),
				new PointF( -68.22980653667256f,44.39907046580123f),
				new PointF( -68.23098363732377f,44.39981473482776f),
				new PointF( -68.23172087319101f,44.40062845705702f),
				new PointF( -68.23301749943816f,44.40235754908735f),
				new PointF( -68.23363167301508f,44.40349618651489f),
				new PointF( -68.23413479325721f,44.40409719242412f),
				new PointF( -68.2348403233084f,44.40497648248251f),
				new PointF( -68.23555617785962f,44.4057868151862f),
				new PointF( -68.23672969169932f,44.40681529127857f),
				new PointF( -68.23823920996895f,44.40758883891702f),
				new PointF( -68.23928028619329f,44.40817897501709f),
				new PointF( -68.24002318960348f,44.40855813435164f),
				new PointF( -68.2416095290509f,44.40892056915745f),
				new PointF( -68.24268107045482f,44.40918888722236f),
				new PointF( -68.24393943869319f,44.40957623681533f),
				new PointF( -68.24544589207569f,44.41006905041365f),
				new PointF( -68.24596589499939f,44.41040873175643f),
				new PointF( -68.24606527038655f,44.41020674403372f),
				new PointF( -68.24608389037408f,44.40962735839786f),
				new PointF( -68.24611502554615f,44.40938341192684f)
		};
		
		townBoundaryArray = new PointF[] {
				new PointF(-68.21352141614736f,44.38597335085543f), 
				new PointF(-68.21337862182395f,44.38570141358703f), 
				new PointF(-68.21305122252194f,44.38456522724085f), 
				new PointF(-68.21288121138903f,44.38336830205846f), 
				new PointF(-68.21297909867805f,44.38281455323425f), 
				new PointF(-68.21331062468992f,44.38232749957384f), 
				new PointF(-68.21309471886592f,44.38192320100152f), 
				new PointF(-68.21317259550918f,44.38152933484604f), 
				new PointF(-68.21298534661503f,44.38052728254657f), 
				new PointF(-68.21306883312128f,44.380149621624f), 
				new PointF(-68.21329088433838f,44.37962786840572f), 
				new PointF(-68.21410038852453f,44.3787671176668f), 
				new PointF(-68.21382980375058f,44.3786829158765f), 
				new PointF(-68.21306466043062f,44.37877192421485f), 
				new PointF(-68.21257917477568f,44.37891793811548f), 
				new PointF(-68.21155575048108f,44.37895673665587f), 
				new PointF(-68.20976863739152f,44.37891429754403f), 
				new PointF(-68.20871139743177f,44.37889993774353f), 
				new PointF(-68.20801580592826f,44.37914175984675f), 
				new PointF(-68.20784072208136f,44.3794305824514f), 
				new PointF(-68.20741451344159f,44.37998019407353f), 
				new PointF(-68.20628150138769f,44.38038785199788f), 
				new PointF(-68.20476279946753f,44.38037739941629f), 
				new PointF(-68.20372485974269f,44.379993858718f), 
				new PointF(-68.20239025923772f,44.37975059313016f), 
				new PointF(-68.20168157940478f,44.37933268432145f), 
				new PointF(-68.20039707733696f,44.3777767823304f), 
				new PointF(-68.19965932538859f,44.37707368627714f), 
				new PointF(-68.19898442569355f,44.37592775131883f), 
				new PointF(-68.19851808094127f,44.37498127440006f), 
				new PointF(-68.198325978196f,44.3749430521423f), 
				new PointF(-68.19781984738781f,44.37498002920953f), 
				new PointF(-68.19676279494266f,44.37468206685431f), 
				new PointF(-68.19605127035301f,44.37491970320814f), 
				new PointF(-68.19596083074774f,44.37527678277869f), 
				new PointF(-68.19564173730292f,44.37553276291521f), 
				new PointF(-68.19438593133509f,44.37626274460346f), 
				new PointF(-68.19449507452083f,44.37636905930779f), 
				new PointF(-68.19457643404149f,44.37660136236751f), 
				new PointF(-68.19447353605443f,44.37719780235312f), 
				new PointF(-68.19381753676439f,44.37759261491829f), 
				new PointF(-68.19343210854511f,44.37815378556111f), 
				new PointF(-68.19275278814776f,44.37861598185273f), 
				new PointF(-68.19264767754677f,44.37900338665031f), 
				new PointF(-68.19214115872302f,44.37976322484509f), 
				new PointF(-68.1924754389146f,44.38000228227349f), 
				new PointF(-68.19291111482428f,44.3797952585633f), 
				new PointF(-68.19310498500489f,44.37989012232944f), 
				new PointF(-68.19351955532268f,44.37986714831873f), 
				new PointF(-68.19343528811623f,44.3794561334244f), 
				new PointF(-68.19425500414256f,44.37957921700429f), 
				new PointF(-68.19491278267735f,44.38005752537379f), 
				new PointF(-68.19587155360729f,44.3799289197514f), 
				new PointF(-68.19612246010024f,44.37941595280454f), 
				new PointF(-68.19660907745521f,44.37925736044867f), 
				new PointF(-68.1975743791901f,44.37978373254254f), 
				new PointF(-68.19781660385846f,44.38026167232112f), 
				new PointF(-68.19753524471771f,44.38047239889218f), 
				new PointF(-68.19761073047094f,44.38067241531066f), 
				new PointF(-68.19803940982435f,44.38128148347935f), 
				new PointF(-68.1975715460768f,44.38139867520494f), 
				new PointF(-68.19667122486166f,44.381099078795f), 
				new PointF(-68.19557826931388f,44.38171238678719f), 
				new PointF(-68.19550652943471f,44.38269798972518f), 
				new PointF(-68.19584978059153f,44.38346630245226f), 
				new PointF(-68.19596427331018f,44.38394205240895f), 
				new PointF(-68.19636348415114f,44.38455911196009f), 
				new PointF(-68.19642224676109f,44.38524430081956f), 
				new PointF(-68.19665289372065f,44.38656480925508f), 
				new PointF(-68.19788909917312f,44.38818740912624f), 
				new PointF(-68.19896871283592f,44.38880115048024f), 
				new PointF(-68.20013181341291f,44.39006149676809f), 
				new PointF(-68.20091023078101f,44.39122937579526f), 
				new PointF(-68.2023894262031f,44.39203138051192f), 
				new PointF(-68.20418350491866f,44.39225028097516f), 
				new PointF(-68.20497314221974f,44.39194380845424f), 
				new PointF(-68.20615271161633f,44.39237875173991f), 
				new PointF(-68.20725996601487f,44.39248672418546f), 
				new PointF(-68.20838343962545f,44.39203422445925f), 
				new PointF(-68.2096468592498f,44.39214051723392f), 
				new PointF(-68.21074123488151f,44.39169465497055f), 
				new PointF(-68.2126389751519f,44.39102368153955f), 
				new PointF(-68.21411154164342f,44.39050527606892f), 
				new PointF(-68.21500230843256f,44.39036500727978f), 
				new PointF(-68.21538033051971f,44.391635121127f), 
				new PointF(-68.21636935585683f,44.39224359671685f), 
				new PointF(-68.21687376228542f,44.39314532461331f), 
				new PointF(-68.21739799266982f,44.39380632766429f), 
				new PointF(-68.21828829834077f,44.3944248351868f), 
				new PointF(-68.21921124933148f,44.39480065687572f), 
				new PointF(-68.21948049364646f,44.39529872478117f), 
				new PointF(-68.2198193282096f,44.39571427937975f), 
				new PointF(-68.22041535296363f,44.39574768145568f), 
				new PointF(-68.22115049952423f,44.3964442498066f), 
				new PointF(-68.22218831673236f,44.39690765557314f), 
				new PointF(-68.22307752462453f,44.39731352989885f), 
				new PointF(-68.22408480166133f,44.39754372222209f), 
				new PointF(-68.2244752249557f,44.3976869826781f), 
				new PointF(-68.22547597078395f,44.39836551021664f), 
				new PointF(-68.22457883955347f,44.39899642168468f), 
				new PointF(-68.22375626158178f,44.3996848041658f), 
				new PointF(-68.22400272732159f,44.40024308743239f), 
				new PointF(-68.22597988185386f,44.39942323595061f), 
				new PointF(-68.22674753893827f,44.39925687317488f), 
				new PointF(-68.22841783811961f,44.40075184167826f), 
				new PointF(-68.22926673123229f,44.40077033925155f), 
				new PointF(-68.22968213365623f,44.40109553371845f), 
				new PointF(-68.2295403236979f,44.40140536403977f), 
				new PointF(-68.23083916720854f,44.40219622556045f), 
				new PointF(-68.23135531895473f,44.40281964008795f), 
				new PointF(-68.23126042776435f,44.40327502336442f), 
				new PointF(-68.23212294817468f,44.40445174395627f), 
				new PointF(-68.23305942547024f,44.40518155575443f), 
				new PointF(-68.23400778879036f,44.40537793215474f), 
				new PointF(-68.23491724484285f,44.40500936450835f), 
				new PointF(-68.2345291455344f,44.40439742178999f), 
				new PointF(-68.23419913465395f,44.40391956906429f), 
				new PointF(-68.23352069533425f,44.40338860995293f), 
				new PointF(-68.23300084997746f,44.40246512919305f), 
				new PointF(-68.23250895995611f,44.40153677138375f), 
				new PointF(-68.23179368101181f,44.40084825775796f), 
				new PointF(-68.23098272023103f,44.39968220443276f), 
				new PointF(-68.23012002731301f,44.39917833828482f), 
				new PointF(-68.22912413028637f,44.3985070651403f), 
				new PointF(-68.2282693116687f,44.39790653925089f), 
				new PointF(-68.22697649513754f,44.39683889141085f), 
				new PointF(-68.22684721367308f,44.39650895180694f), 
				new PointF(-68.22662920141504f,44.39607697528334f), 
				new PointF(-68.22622357204632f,44.39527054237274f), 
				new PointF(-68.22640448031079f,44.39450630054749f), 
				new PointF(-68.22673894071842f,44.39398736977305f), 
				new PointF(-68.22714871843399f,44.39364526066754f), 
				new PointF(-68.22705544813833f,44.39335230260476f), 
				new PointF(-68.22649453737588f,44.39309570800446f), 
				new PointF(-68.22592859493794f,44.39340300515502f), 
				new PointF(-68.22593518931359f,44.39364827586828f), 
				new PointF(-68.22595131128384f,44.39392276381523f), 
				new PointF(-68.22543451277443f,44.39432429915657f), 
				new PointF(-68.22468894082967f,44.39397675888942f), 
				new PointF(-68.22475862463376f,44.39366138158893f), 
				new PointF(-68.22500818651265f,44.39343605033554f), 
				new PointF(-68.22502179117311f,44.39310913611428f), 
				new PointF(-68.22505319355702f,44.39258274965529f), 
				new PointF(-68.22462727788442f,44.39227973838012f), 
				new PointF(-68.22500834205115f,44.39199630598281f), 
				new PointF(-68.22523016488391f,44.39153267677494f), 
				new PointF(-68.22491256073822f,44.39125711366282f), 
				new PointF(-68.22499407543877f,44.39081020224449f), 
				new PointF(-68.22532057856914f,44.39061438823961f), 
				new PointF(-68.22535993317821f,44.39041867754649f), 
				new PointF(-68.22583221263484f,44.38968684623178f), 
				new PointF(-68.22484766112657f,44.38961413306566f), 
				new PointF(-68.22455590058908f,44.38909504791347f), 
				new PointF(-68.22410571369635f,44.38860211131131f), 
				new PointF(-68.22359851576047f,44.38789894295299f), 
				new PointF(-68.2234100638684f,44.38752663551884f), 
				new PointF(-68.22373606870839f,44.38687820190103f), 
				new PointF(-68.2239791144639f,44.38637358861563f), 
				new PointF(-68.22489236976149f,44.38598592615907f), 
				new PointF(-68.22583550063095f,44.3856163993119f), 
				new PointF(-68.22590695463225f,44.3848593111203f), 
				new PointF(-68.22593090221699f,44.38362306815236f), 
				new PointF(-68.22570735588803f,44.38300680024589f), 
				new PointF(-68.22576180750653f,44.38267532695436f), 
				new PointF(-68.22606581031656f,44.38194746794321f), 
				new PointF(-68.22626484239993f,44.38078567926006f), 
				new PointF(-68.2251499189727f,44.3806042308727f), 
				new PointF(-68.22446021143311f,44.38047950360791f), 
				new PointF(-68.2230756647856f,44.3805764016242f), 
				new PointF(-68.22186008057417f,44.38088376174517f), 
				new PointF(-68.22162154101601f,44.38139917138314f), 
				new PointF(-68.22118085253108f,44.382583778857f), 
				new PointF(-68.22028405278836f,44.3839705392597f), 
				new PointF(-68.21887856426467f,44.38523927880658f), 
				new PointF(-68.21774760766286f,44.38632994219911f), 
				new PointF(-68.21651858496749f,44.3867603328768f), 
				new PointF(-68.2148234319519f,44.38674612316736f), 
				new PointF(-68.21359396647124f,44.38625759885858f), 
				new PointF(-68.21352141614736f,44.38597335085543f), 
		};
	}

}
