import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;


import java.util.Map;

/**
 * Information about one field
 * Created by mabu on 2.11.2015.
 */
public class FieldInfo {
    public String name;

    @Deprecated
    public String defaultValue;

    public String description;

    /**
     * GraphQLType
     */
    public String type;

    private boolean isDouble = false;

    public boolean notNull = false;

    private static final String dataFetcher = ".dataFetcher(environment -> ((%1$s) environment.getSource()).%2$s)";
    private static final String dataFetcherDouble = ".dataFetcher(environment -> (Float)((%1$s) environment.getSource()).%2$s)";
    private static final String notNullString = "new GraphQLNonNull(%s)";
    private static final String descriptionTemplate = ".description(\"%s\")\n";

    private String setPrimitiveType(PrimitiveType primitiveType) {
        String local_type;
        switch (primitiveType.getType()) {
        case Boolean:
            local_type = "Scalars.GraphQLBoolean";
            break;
        case Int:
        case Short:
            local_type = "Scalars.GraphQLInt";
            break;
        case Long:
            local_type = "Scalars.GraphQLLong";
            break;
        case Float:
            local_type = "Scalars.GraphQLFloat";
            break;
        case Double:
            local_type = "Scalars.GraphQLFloat";
            isDouble = true;
            break;
        case Char:
            local_type = "Scalars.GraphQLString";
            break;
        default:
            throw new RuntimeException(
                "Unsupported primitive type: " + primitiveType.getType());
        }
        return local_type;
    }

    /**
     * Sets name type and notNull from comment
     * @param variableType
     * @param variable gets name of variable
     * @param comment
     * @param typeMap
     */
    public FieldInfo(Type variableType, VariableDeclarator variable, Comment comment,
        Map<String, String> typeMap) {
        //Primitive types int, float, boolean etc
        if (variableType instanceof PrimitiveType) {

            PrimitiveType primitiveType = (PrimitiveType) variableType;
            type = setPrimitiveType(primitiveType);

            //Reference types String, Int, Float, List<T>
        } else if(variableType instanceof ReferenceType) {
            ReferenceType referenceType = (ReferenceType) variableType;
            if (referenceType.getType() instanceof ClassOrInterfaceType) {
                ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) referenceType
                    .getType();
                //Boxed types: Int, Float, Double, Boolean
                if (classOrInterfaceType.isBoxedType()) {
                    type = setPrimitiveType(classOrInterfaceType.toUnboxedType());
                } else if (classOrInterfaceType.getName().equals("String")) {
                    type = "Scalars.GraphQLString";
                    //List<T> it is assumed that T is some class that is in typeMap so we just replace
                    //className with variable where it should be defined
                } else if (classOrInterfaceType.getName().equals("List")) {
                    type = "new GraphQLList(";
                    String primitiveType = classOrInterfaceType.getTypeArgs().get(0).toString();
                    String typeName = typeMap.get(primitiveType);
                    if (typeName != null) {
                     type+=typeName+")";
                    } else {
                        throw new RuntimeException(
                            "Unknown list element type: " + primitiveType
                        );
                    }
                } else {
                    throw new RuntimeException(
                        "Unsupported classOrInterfaceType: " + classOrInterfaceType
                    );
                }
            } else {
                throw new RuntimeException(
                    "Unsupported subreference type: " + referenceType.getType()
                );
            }

        } else {
            throw new RuntimeException(
                "Unsupported type:" + variableType + " [" + variableType.getClass().getSimpleName() + "]"
            );
        }

        name = variable.getId().getName();

        if (comment != null) {
            readComments(comment);
        }


    }

    /**
     * Sets description and notNull from comment
     * @param comment
     */
    private void readComments(Comment comment) {
        description = comment.getContent().trim();
        if (description.contains("notnull")) {
            notNull = true;
            description = description.replace("@notnull", "");
        }
        int defaultIndex = description.indexOf("@default");
        if (defaultIndex >= 0) {

            //int endofline = description.indexOf('\n', defaultIndex);
            defaultValue = description.substring(defaultIndex+"@default:".length()).trim();
            description = description.substring(0, defaultIndex);
        }
        if (comment.isLineComment()) {
            description = description.replaceFirst("//", "");
        } else {
            //description = description.replaceAll("\\n\\s+\\*/?", "").replaceFirst("/\\*+", "");
            description = description.replace('*', ' ');
            description = description.trim().replaceAll(" +", " ");
        }
    }

    @Override public String toString() {

        //ClassLoader cl = this.getClass().getClassLoader();

        final StringBuilder sb = new StringBuilder("FieldInfo{");
        sb.append("name='").append(name).append('\'');
        if (defaultValue != null) {
            sb.append(", defaultValue='").append(defaultValue).append('\'');
        }
        if (description != null) {
            sb.append(", description='").append(description).append('\'');
        }
        sb.append(", type=").append(type);
        sb.append(", isDouble=").append(isDouble);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Createss graphQl field declaration with name, type, description and dataFetcher
     *
     * DataFetcher assumes that fields are public
     * @param templateContent
     * @param className
     * @return
     */
    public String toGraphQlType(final String templateContent, final String className) {
        StringBuilder rest = new StringBuilder();
        String fullType = type;

        if (notNull) {
            fullType = String.format(notNullString, type);
        }
        if (description != null) {
            rest.append(String.format(descriptionTemplate, description));
            rest.append("                ");
        }

        if (isDouble) {
            rest.append(String.format(dataFetcherDouble, className, name));
        } else {
            rest.append(String.format(dataFetcher, className, name));
        }
        return templateContent.replace("<name>", name)
            .replace("<rest>", rest.toString())
            .replace("<type>", fullType);
    }
}
