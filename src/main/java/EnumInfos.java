import com.github.javaparser.ast.comments.Comment;

import java.util.*;

/**
 * Created by mabu on 2.11.2015.
 */
public class EnumInfos {
    private final Map<String, String> typeMap;

    List<EnumInfo> enumInfoList;
    String description;

    private static final String descriptionTemplate = ".description(\"%s\")";

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
        if (description != null) {
            enumJoiner.add(String.format(descriptionTemplate, description));

        }
        for (final EnumInfo enumInfo: enumInfoList) {
            enumJoiner.add(enumInfo.toGraphQlType(className));
        }
        return String.format(enumTemplateContent, schemaName, qlname, enumJoiner.toString());
    }

    public void setDescription(Comment comment) {
        if (comment == null) {
            return;
        }
        description = comment.getContent().trim();

        if (!comment.isLineComment()) {
            //description = description.replaceAll("\\n\\s+\\*/?", "").replaceFirst("/\\*+", "");
            description = description.replace('*', ' ');
            description = description.replaceAll(" +", " ").replace("\n", "").trim();
        }

    }
}
