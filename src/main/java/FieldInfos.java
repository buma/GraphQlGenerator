

import com.github.javaparser.ast.comments.Comment;

import java.util.*;

/**
 * Created by mabu on 2.11.2015.
 */
public class FieldInfos {
    List<FieldInfo> fieldInfoList;
    Map<String, String> typeMap;
    String description;

    private static String fieldDeclaration = "public GraphQLOutputType SCHEMANAME = new GraphQLTypeReference(\"QLANME\");\n";
    private static final String descriptionTemplate = ".description(\"%s\")";

    public FieldInfos(Map<String, String> typeMap) {
        fieldInfoList = new ArrayList<>(50);
        this.typeMap = typeMap;
    }

    public void addField(FieldInfo fieldInfo) {
        fieldInfoList.add(fieldInfo);
    }

    public String toSchema(boolean arguments, String className, String qlname) {
        String schemaName = typeMap.get(className);
        if (schemaName == null) {
            throw new RuntimeException("Unknown class: " + className);
        }

        String mainTemplatePath, fieldTemplatePath;
        if (arguments) {
            mainTemplatePath = "templates/OperationField.txt";
            fieldTemplatePath = "templates/OperationArgument.txt";
        } else {
            mainTemplatePath = "templates/template.stg";
            fieldTemplatePath = "templates/field.st";
        }

        final String mainTemplateContet = new Scanner(getClass().getResourceAsStream(mainTemplatePath)).useDelimiter("\\Z").next();
        final String fieldTemplateContent = new Scanner(getClass().getResourceAsStream(fieldTemplatePath)).useDelimiter("\\Z").next();

        StringJoiner fieldsJoiner = new StringJoiner("\n            ");

        if (description != null) {
            fieldsJoiner.add(String.format(descriptionTemplate, description));

        }
        for (final FieldInfo fieldinfo: fieldInfoList) {
            String field = fieldinfo.toGraphQlType(fieldTemplateContent, className, arguments);
            fieldsJoiner.add(field);
        }


        return mainTemplateContet.replace("<schemaname>", schemaName)
            .replace("<qlname>", qlname)
            .replace("<fields>", fieldsJoiner.toString());
    }

    public String toSchema(boolean arguments, String stats) {
        return toSchema(arguments, stats, stats);
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
