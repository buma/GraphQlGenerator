import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * Created by mabu on 2.11.2015.
 */
public class EnumInfos {
    List<EnumInfo> enumInfoList;
    String description;

    public EnumInfos() {
        enumInfoList = new ArrayList<>(15);
    }

    public void addEnum(EnumInfo enumInfo) {
        enumInfo.order = enumInfoList.size();
        enumInfoList.add(enumInfo);
    }

    public String toSchema(String className, String qlname) {
        //TODO: implement this
        String schemaName = "absoluteDirectionTypeEnum";

        final String enumTemplateContent = new Scanner(
            getClass().getResourceAsStream("templates/EnumType.txt")).useDelimiter("\\Z").next();

        StringJoiner enumJoiner = new StringJoiner("\n    ");
        for (final EnumInfo enumInfo: enumInfoList) {
            enumJoiner.add(enumInfo.toGraphQlType());
        }
        return String.format(enumTemplateContent, schemaName, qlname, enumJoiner.toString());
    }
}
