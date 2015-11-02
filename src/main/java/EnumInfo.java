import com.github.javaparser.ast.comments.Comment;

/**
 * Created by mabu on 2.11.2015.
 */
public class EnumInfo {
    String name;
    String description;
    public int order;

    private static final String pureEnum = ".value(\"%1$s\", %2$d)";
    private static final String enumWithDescription = ".value(\"%1$s\", %2$d, \"%3$s\")";

    public EnumInfo(String name, Comment comment) {
        this.name = name;
        if (comment != null) {
            description = comment.getContent();
        }
    }

    public String toGraphQlType() {
        if (description != null) {
            return String.format(enumWithDescription, name, order, description);
        } else {
            return String.format(pureEnum, name, order);
        }
    }
}
