import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mabu on 2.11.2015.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        File file = new File(args[0], "NonTransitMode.java");

        Map<String, String> typeMap = new HashMap<>(20);
        typeMap.put("Stats", "statsType");
        typeMap.put("RouteShort", "routeType");
        typeMap.put("SegmentPattern", "segmentPatternType");
        typeMap.put("Fare", "fareType");
        typeMap.put("TransitSegment", "transitSegmentType");
        typeMap.put("StreetSegment", "streetSegmentType");
        typeMap.put("StreetEdgeInfo", "streetEdgeInfoType");
        typeMap.put("PolylineGeometry","polylineGeometryType");
        typeMap.put("RelativeDirection", "relativeDirectionEnum");
        typeMap.put("AbsoluteDirection", "absoluteDirectionEnum");
        typeMap.put("NonTransitMode", "nonTransitModeEnum");

        IRead readClass = new ReadEnum(typeMap);
        readClass.readFile(file);

        // prints the resulting compilation unit to default system output
        //System.out.println(cu.toString());
    }



}
