import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mabu on 2.11.2015.
 */
public class Main {

    public static void main(String[] args) throws Exception {



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
        typeMap.put("LegMode", "legModeEnum");
        typeMap.put("TransitModes", "transitmodeEnum");
        typeMap.put("ProfileOption", "profileOptionType");
        typeMap.put("BikeRentalStation", "bikeRentalStationType");
        typeMap.put("Elevation", "elevationType");
        typeMap.put("Alert", "alertType");
        typeMap.put("Stop", "stopType");
        typeMap.put("StopCluster", "stopClusterType");
        typeMap.put("Coordinate", "inputCoordinateType");
        typeMap.put("ProfileRequest", "profile");
        typeMap.put("LocalDate", "Scalars.GraphQLString");
        typeMap.put("SearchType", "searchTypeEnum");
        typeMap.put("TransitJourneyID", "transitJourneyIDType");
        typeMap.put("PointToPointConnection", "pointToPointConnectionType");
        typeMap.put("ZonedDateTime", "GraphQLZonedDateTime");
        typeMap.put("Itinerary", "itineraryType");
        typeMap.put("Trip", "tripType");

        /*for (Map.Entry<String, String> typeStuff: typeMap.entrySet()) {
            if (typeStuff.getValue().endsWith("Enum")) {
                File file = new File(args[0], typeStuff.getKey() + ".java");
                IRead readClass = new ReadEnum(typeMap);
                readClass.readFile(file);
            }
        }*/

        File file = new File(args[0],  "api/util/Trip.java");
        IRead readClass = new ReadClass(typeMap, false);
        //IRead readClass = new ReadEnum(typeMap);
        readClass.readFile(file);

        //file = new File(args[0],  "StopCluster.java");
        //readClass.readFile(file);



        // prints the resulting compilation unit to default system output
        //System.out.println(cu.toString());
    }



}
