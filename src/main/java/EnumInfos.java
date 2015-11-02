import java.util.*;

/**
 * Created by mabu on 2.11.2015.
 */
public class EnumInfos {
    private final Map<String, String> typeMap;

    List<EnumInfo> enumInfoList;
    String description;

    public EnumInfos(Map<String, String> typeMap) {
        enumInfoList = new ArrayList<>(15);
        this.typeMap = typeMap;
    }

    public void addEnum(EnumInfo enumInfo) {
        enumInfo.order = enumInfoList.size();
        enumInfoList.add(enumInfo);
    }

    public String toSchema(String className, String qlname) {
        String schemaName = typeMap.get(className);
        if (schemaName == null) {
            throw new RuntimeException("Unknown class: " + className);
        }

        final String enumTemplateContent = new Scanner(
            getClass().getResourceAsStream("templates/EnumType.txt")).useDelimiter("\\Z").next();

        StringJoiner enumJoiner = new StringJoiner("\n    ");
        for (final EnumInfo enumInfo: enumInfoList) {
            enumJoiner.add(enumInfo.toGraphQlType());
        }
        return String.format(enumTemplateContent, schemaName, qlname, enumJoiner.toString());
    }
}
