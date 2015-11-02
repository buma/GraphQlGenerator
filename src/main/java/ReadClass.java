import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by mabu on 2.11.2015.
 */
public class ReadClass {


    public String readFile(File file) throws Exception{
        return readFile(file, null);


    }

    private static String getFilenameWithoutExtension(String fileName) {

        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(0, pos);
        }
        return fileName;
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


        FieldInfos fieldInfos = new FieldInfos();
        new MethodVisitor().visit(cu, fieldInfos);



        String className = getFilenameWithoutExtension(file.getName());
        String graphQLSchema;
        if (qlName == null) {
            graphQLSchema = fieldInfos.toSchema(className);
        } else {
            graphQLSchema = fieldInfos.toSchema(className, qlName);
        }

        System.out.println(graphQLSchema);
        return graphQLSchema;
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MethodVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(FieldDeclaration n, Object arg) {
            FieldInfos fieldInfos = (FieldInfos)arg;
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
    }
}
