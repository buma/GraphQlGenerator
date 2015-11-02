

import java.util.*;

/**
 * Created by mabu on 2.11.2015.
 */
public class FieldInfos {
    List<FieldInfo> fieldInfoList;
    Map<String, String> typeMap;

    private static String fieldDeclaration = "public GraphQLOutputType SCHEMANAME = new GraphQLTypeReference(\"QLANME\");\n";

    public FieldInfos() {
        fieldInfoList = new ArrayList<>(50);
        typeMap = new HashMap<>(20);
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
    }

    public void addField(FieldInfo fieldInfo) {
        fieldInfoList.add(fieldInfo);
    }

    public String toSchema(String className, String qlname) {
        String schemaName = typeMap.get(className);
        if (schemaName == null) {
            throw new RuntimeException("Unknown class: " + className);
        }

        final String mainTemplateContet = new Scanner(getClass().getResourceAsStream("templates/template.stg")).useDelimiter("\\Z").next();
        final String fieldTemplateContent = new Scanner(getClass().getResourceAsStream("templates/field.st")).useDelimiter("\\Z").next();


        StringBuilder fields = new StringBuilder();

        StringJoiner fieldsJoiner = new StringJoiner("\n            ");

        for (final FieldInfo fieldinfo: fieldInfoList) {
            String field = fieldinfo.toGraphQlType(fieldTemplateContent, className);
            fields.append(field);
            fields.append("\n            ");
            fieldsJoiner.add(field);
        }


        return mainTemplateContet.replace("<schemaname>", schemaName)
            .replace("<qlname>", qlname)
            .replace("<fields>", fieldsJoiner.toString());
    }

    public String toSchema(String stats) {
        return toSchema(stats, stats);
    }
}
