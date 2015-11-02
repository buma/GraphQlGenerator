import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import util.FileNameUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * Created by mabu on 2.11.2015.
 */
public class ReadEnum implements IRead {

    private final Map<String, String> typeMap;

    public ReadEnum(Map<String, String> typeMap) {
        this.typeMap = typeMap;
    }

    @Override
    public String readFile(File file, String qlName) throws Exception {
        System.out.println("Reading: "+ file);
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream(file);

        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        EnumInfos enumInfos = new EnumInfos(typeMap);
        new EnumVisitor().visit(cu, enumInfos);

        String className = FileNameUtil.getFilenameWithoutExtension(file.getName());
        String graphQLSchema;
        if (qlName == null) {
            graphQLSchema = enumInfos.toSchema(className, className);
        } else {
            graphQLSchema = enumInfos.toSchema(className, qlName);
        }

        System.out.println(graphQLSchema);
        return graphQLSchema;
    }

    private static class EnumVisitor extends VoidVisitorAdapter<EnumInfos> {

        @Override public void visit(EnumConstantDeclaration n, EnumInfos enumInfos) {
            EnumInfo enumInfo = new EnumInfo(n.getName(), n.getComment());
            enumInfos.addEnum(enumInfo);
        }
    }
}
