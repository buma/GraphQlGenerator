import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.TypeDeclarationStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import util.FileNameUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * Created by mabu on 2.11.2015.
 */
public class ReadClass implements IRead {

    private final Map<String, String> typeMap;
    boolean arguments = false;

    public ReadClass(Map<String, String> typeMap) {
        this.typeMap = typeMap;
    }

    public ReadClass(Map<String, String> typeMap, boolean arguments) {
        this.typeMap = typeMap;
        this.arguments = arguments;
    }

    public String readFile(File file, String qlName) throws Exception{
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


        FieldInfos fieldInfos = new FieldInfos(typeMap);
        new MethodVisitor().visit(cu, fieldInfos);



        String className = FileNameUtil.getFilenameWithoutExtension(file.getName());
        String graphQLSchema;
        if (qlName == null) {
            graphQLSchema = fieldInfos.toSchema(arguments, className);
        } else {
            graphQLSchema = fieldInfos.toSchema(arguments, className, qlName);
        }

        System.out.println(graphQLSchema);
        return graphQLSchema;
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MethodVisitor extends VoidVisitorAdapter<FieldInfos> {

        @Override
        public void visit(FieldDeclaration n, FieldInfos fieldInfos) {
            for (final VariableDeclarator variable : n.getVariables()) {
                Type type = n.getType();
                if (ModifierSet.isPublic(n.getModifiers())) {
                    FieldInfo fieldInfo = new FieldInfo(type, variable, n.getComment(), fieldInfos.typeMap);
                /*System.out.println("Type: " + type+ "Type1: " + type.getClass().getName());
                System.out.println("Vars: "+ variable);
                System.out.println("Jdoc: " +n.getComment());
                System.out.println(fieldInfo);
                System.out.println("");*/
                    fieldInfos.addField(fieldInfo);
                }
            }

            //super.visit(n, arg);
        }

        @Override public void visit(ClassOrInterfaceDeclaration n, FieldInfos fieldInfos) {
            fieldInfos.setDescription(n.getComment());
            super.visit(n, fieldInfos);
        }
    }
}
