import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mabu on 2.11.2015.
 */
public class Main {

    public static void main(String[] args) throws IOException, ParseException {

        File file = new File(args[0], "PolylineGeometry.java");
        System.out.println(file);
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

        String graphQLSchema = fieldInfos.toSchema("PolylineGeometry");

        System.out.println(graphQLSchema);

        // prints the resulting compilation unit to default system output
        //System.out.println(cu.toString());
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
