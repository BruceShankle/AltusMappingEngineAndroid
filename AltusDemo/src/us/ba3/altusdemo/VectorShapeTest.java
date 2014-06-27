package us.ba3.altusdemo;
import java.util.Arrays;
import android.graphics.Color;
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
	Location[] parkRouteArray;
	Location[] townBoundaryArray;
	
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
		
		Location[] pointArray = Arrays.copyOfRange(parkRouteArray, startIndex, endIndex + 1);

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
	public void start() {
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
		Location[] lineA = new Location[] { 
				new Location(44.40944831003259f, -68.24728259147459f), 
				new Location(44.40741561289422f, -68.24505339498397f) };
		Location[] lineB = new Location[] {
				new Location(44.4091956632822f, -68.24508098382695f),
				new Location(44.40765967267418f, -68.24745210157219f) };
		
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
        		mapView.setLocationThatFitsCoordinates(new Location(44.3760512, -68.1945803),
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
		parkRouteArray = new Location[] {
				new Location(44.3876475939765, -68.20427664633465),
				new Location(44.38764573794926, -68.20432920072335),
				new Location(44.3870805628603, -68.20657730647608),
				new Location(44.38655704460421, -68.20870652812002),
				new Location(44.38631957495898, -68.21029170184414),
				new Location(44.38603108929864, -68.21207924538474),
				new Location(44.38601075715042, -68.21318076092939),
				new Location(44.38656370779266, -68.21345157728328),
				new Location(44.38781387385528, -68.21392773876104),
				new Location(44.3883590909554, -68.21436396706643),
				new Location(44.38888131261904, -68.21530549650363),
				new Location(44.38928346058832, -68.21704004287085),
				new Location(44.38973514851259, -68.21812318788386),
				new Location(44.39010521389975, -68.2183569192163),
				new Location(44.3908570374663, -68.21875877174242),
				new Location(44.39155569640457, -68.21926604388855),
				new Location(44.39216570481639, -68.21993108340044),
				new Location(44.3930095519913, -68.22065982008499),
				new Location(44.39368288804165, -68.22130764342167),
				new Location(44.39396836224729, -68.22189699677823),
				new Location(44.39420657257293, -68.22268801595031),
				new Location(44.39473225509082, -68.22322741654055),
				new Location(44.39575275160491, -68.22508696269918),
				new Location(44.39664169462471, -68.22673904484827),
				new Location(44.39815432249696, -68.22851872114322),
				new Location(44.39907046580123, -68.22980653667256),
				new Location(44.39981473482776, -68.23098363732377),
				new Location(44.40062845705702, -68.23172087319101),
				new Location(44.40235754908735, -68.23301749943816),
				new Location(44.40349618651489, -68.23363167301508),
				new Location(44.40409719242412, -68.23413479325721),
				new Location(44.40497648248251, -68.2348403233084),
				new Location(44.4057868151862, -68.23555617785962),
				new Location(44.40681529127857, -68.23672969169932),
				new Location(44.40758883891702, -68.23823920996895),
				new Location(44.40817897501709, -68.23928028619329),
				new Location(44.40855813435164, -68.24002318960348),
				new Location(44.40892056915745, -68.2416095290509),
				new Location(44.40918888722236, -68.24268107045482),
				new Location(44.40957623681533, -68.24393943869319),
				new Location(44.41006905041365, -68.24544589207569),
				new Location(44.41040873175643, -68.24596589499939),
				new Location(44.41020674403372, -68.24606527038655),
				new Location(44.40962735839786, -68.24608389037408),
				new Location(44.4093834119268, -68.24611502554615)
		};
		
		townBoundaryArray = new Location[] {
				new Location(44.38597335085543,-68.21352141614736),	
				new Location(44.38570141358703,-68.21337862182395),	
				new Location(44.38456522724085,-68.21305122252194),	
				new Location(44.38336830205846,-68.21288121138903),	
				new Location(44.38281455323425,-68.21297909867805),	
				new Location(44.38232749957384,-68.21331062468992),	
				new Location(44.38192320100152,-68.21309471886592),	
				new Location(44.38152933484604,-68.21317259550918),	
				new Location(44.38052728254657,-68.21298534661503),	
				new Location(44.380149621624,-68.21306883312128),
				new Location(44.37962786840572,-68.21329088433838),	
				new Location(44.3787671176668,-68.21410038852453),
				new Location(44.3786829158765,-68.21382980375058),
				new Location(44.37877192421485,-68.21306466043062),	
				new Location(44.37891793811548,-68.21257917477568),	
				new Location(44.37895673665587,-68.21155575048108),	
				new Location(44.37891429754403,-68.20976863739152),	
				new Location(44.37889993774353,-68.20871139743177),	
				new Location(44.37914175984675,-68.20801580592826),	
				new Location(44.3794305824514,-68.20784072208136),
				new Location(44.37998019407353,-68.20741451344159),	
				new Location(44.38038785199788,-68.20628150138769),	
				new Location(44.38037739941629,-68.20476279946753),	
				new Location(44.379993858718,-68.20372485974269),
				new Location(44.37975059313016,-68.20239025923772),	
				new Location(44.37933268432145,-68.20168157940478),	
				new Location(44.3777767823304,-68.20039707733696),
				new Location(44.37707368627714,-68.19965932538859),	
				new Location(44.37592775131883,-68.19898442569355),	
				new Location(44.37498127440006,-68.19851808094127),	
				new Location(44.3749430521423,-68.198325978196),
				new Location(44.37498002920953,-68.19781984738781),	
				new Location(44.37468206685431,-68.19676279494266),	
				new Location(44.37491970320814,-68.19605127035301),	
				new Location(44.37527678277869,-68.19596083074774),	
				new Location(44.37553276291521,-68.19564173730292),	
				new Location(44.37626274460346,-68.19438593133509),	
				new Location(44.37636905930779,-68.19449507452083),	
				new Location(44.37660136236751,-68.19457643404149),	
				new Location(44.37719780235312,-68.19447353605443),	
				new Location(44.37759261491829,-68.19381753676439),	
				new Location(44.37815378556111,-68.19343210854511),	
				new Location(44.37861598185273,-68.19275278814776),	
				new Location(44.37900338665031,-68.19264767754677),	
				new Location(44.37976322484509,-68.19214115872302),	
				new Location(44.38000228227349,-68.1924754389146),	
				new Location(44.3797952585633,-68.19291111482428),
				new Location(44.37989012232944,-68.19310498500489),	
				new Location(44.37986714831873,-68.19351955532268),	
				new Location(44.3794561334244,-68.19343528811623),
				new Location(44.37957921700429,-68.19425500414256),	
				new Location(44.38005752537379,-68.19491278267735),	
				new Location(44.3799289197514,-68.19587155360729),
				new Location(44.37941595280454,-68.19612246010024),	
				new Location(44.37925736044867,-68.19660907745521),	
				new Location(44.37978373254254,-68.1975743791901),	
				new Location(44.38026167232112,-68.19781660385846),	
				new Location(44.38047239889218,-68.19753524471771),	
				new Location(44.38067241531066,-68.19761073047094),	
				new Location(44.38128148347935,-68.19803940982435),	
				new Location(44.38139867520494,-68.1975715460768),	
				new Location(44.381099078795,-68.19667122486166),
				new Location(44.38171238678719,-68.19557826931388),	
				new Location(44.38269798972518,-68.19550652943471),	
				new Location(44.38346630245226,-68.19584978059153),	
				new Location(44.38394205240895,-68.19596427331018),	
				new Location(44.38455911196009,-68.19636348415114),	
				new Location(44.38524430081956,-68.19642224676109),	
				new Location(44.38656480925508,-68.19665289372065),	
				new Location(44.38818740912624,-68.19788909917312),	
				new Location(44.38880115048024,-68.19896871283592),	
				new Location(44.39006149676809,-68.20013181341291),	
				new Location(44.39122937579526,-68.20091023078101),	
				new Location(44.39203138051192,-68.2023894262031),	
				new Location(44.39225028097516,-68.20418350491866),	
				new Location(44.39194380845424,-68.20497314221974),	
				new Location(44.39237875173991,-68.20615271161633),	
				new Location(44.39248672418546,-68.20725996601487),	
				new Location(44.39203422445925,-68.20838343962545),	
				new Location(44.39214051723392,-68.2096468592498),	
				new Location(44.39169465497055,-68.21074123488151),	
				new Location(44.39102368153955,-68.2126389751519),	
				new Location(44.39050527606892,-68.21411154164342),	
				new Location(44.39036500727978,-68.21500230843256),	
				new Location(44.391635121127,-68.21538033051971),	
				new Location(44.39224359671685,-68.21636935585683),	
				new Location(44.39314532461331,-68.21687376228542),	
				new Location(44.39380632766429,-68.21739799266982),	
				new Location(44.3944248351868,-68.21828829834077),
				new Location(44.39480065687572,-68.21921124933148),	
				new Location(44.39529872478117,-68.21948049364646),	
				new Location(44.39571427937975,-68.2198193282096),	
				new Location(44.39574768145568,-68.22041535296363),	
				new Location(44.3964442498066,-68.22115049952423),
				new Location(44.39690765557314,-68.22218831673236),	
				new Location(44.39731352989885,-68.22307752462453),	
				new Location(44.39754372222209,-68.22408480166133),	
				new Location(44.3976869826781,-68.2244752249557),
				new Location(44.39836551021664,-68.22547597078395),	
				new Location(44.39899642168468,-68.22457883955347),	
				new Location(44.3996848041658,-68.22375626158178),
				new Location(44.40024308743239,-68.22400272732159),	
				new Location(44.39942323595061,-68.22597988185386),	
				new Location(44.39925687317488,-68.22674753893827),	
				new Location(44.40075184167826,-68.22841783811961),	
				new Location(44.40077033925155,-68.22926673123229),	
				new Location(44.40109553371845,-68.22968213365623),	
				new Location(44.40140536403977,-68.2295403236979),	
				new Location(44.40219622556045,-68.23083916720854),	
				new Location(44.40281964008795,-68.23135531895473),	
				new Location(44.40327502336442,-68.23126042776435),	
				new Location(44.40445174395627,-68.23212294817468),	
				new Location(44.40518155575443,-68.23305942547024),	
				new Location(44.40537793215474,-68.23400778879036),	
				new Location(44.40500936450835,-68.23491724484285),	
				new Location(44.40439742178999,-68.2345291455344),	
				new Location(44.40391956906429,-68.23419913465395),	
				new Location(44.40338860995293,-68.23352069533425),	
				new Location(44.40246512919305,-68.23300084997746),	
				new Location(44.40153677138375,-68.23250895995611),	
				new Location(44.40084825775796,-68.23179368101181),	
				new Location(44.39968220443276,-68.23098272023103),	
				new Location(44.39917833828482,-68.23012002731301),	
				new Location(44.3985070651403,-68.22912413028637),
				new Location(44.39790653925089,-68.2282693116687),	
				new Location(44.39683889141085,-68.22697649513754),	
				new Location(44.39650895180694,-68.22684721367308),	
				new Location(44.39607697528334,-68.22662920141504),	
				new Location(44.39527054237274,-68.22622357204632),	
				new Location(44.39450630054749,-68.22640448031079),	
				new Location(44.39398736977305,-68.22673894071842),	
				new Location(44.39364526066754,-68.22714871843399),	
				new Location(44.39335230260476,-68.22705544813833),	
				new Location(44.39309570800446,-68.22649453737588),	
				new Location(44.39340300515502,-68.22592859493794),	
				new Location(44.39364827586828,-68.22593518931359),	
				new Location(44.39392276381523,-68.22595131128384),	
				new Location(44.39432429915657,-68.22543451277443),	
				new Location(44.39397675888942,-68.22468894082967),	
				new Location(44.39366138158893,-68.22475862463376),	
				new Location(44.39343605033554,-68.22500818651265),	
				new Location(44.39310913611428,-68.22502179117311),	
				new Location(44.39258274965529,-68.22505319355702),	
				new Location(44.39227973838012,-68.22462727788442),	
				new Location(44.39199630598281,-68.22500834205115),	
				new Location(44.39153267677494,-68.22523016488391),	
				new Location(44.39125711366282,-68.22491256073822),	
				new Location(44.39081020224449,-68.22499407543877),	
				new Location(44.39061438823961,-68.22532057856914),	
				new Location(44.39041867754649,-68.22535993317821),	
				new Location(44.38968684623178,-68.22583221263484),	
				new Location(44.38961413306566,-68.22484766112657),	
				new Location(44.38909504791347,-68.22455590058908),	
				new Location(44.38860211131131,-68.22410571369635),	
				new Location(44.38789894295299,-68.22359851576047),	
				new Location(44.38752663551884,-68.2234100638684),	
				new Location(44.38687820190103,-68.22373606870839),	
				new Location(44.38637358861563,-68.2239791144639),	
				new Location(44.38598592615907,-68.22489236976149),	
				new Location(44.3856163993119,-68.22583550063095),
				new Location(44.3848593111203,-68.22590695463225),
				new Location(44.38362306815236,-68.22593090221699),	
				new Location(44.38300680024589,-68.22570735588803),	
				new Location(44.38267532695436,-68.22576180750653),	
				new Location(44.38194746794321,-68.22606581031656),	
				new Location(44.38078567926006,-68.22626484239993),	
				new Location(44.3806042308727,-68.2251499189727),
				new Location(44.38047950360791,-68.22446021143311),	
				new Location(44.3805764016242,-68.2230756647856),
				new Location(44.38088376174517,-68.22186008057417),	
				new Location(44.38139917138314,-68.22162154101601),	
				new Location(44.382583778857,-68.22118085253108),
				new Location(44.3839705392597,-68.22028405278836),
				new Location(44.38523927880658,-68.21887856426467),	
				new Location(44.38632994219911,-68.21774760766286),	
				new Location(44.3867603328768,-68.21651858496749),
				new Location(44.38674612316736,-68.2148234319519),	
				new Location(44.38625759885858,-68.21359396647124),	
				new Location(44.38597335085543,-68.21352141614736)
		};
	}

}
