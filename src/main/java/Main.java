import java.io.File;

/**
 * Created by mabu on 2.11.2015.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        File file = new File(args[0], "PolylineGeometry.java");

        System.out.println(file.getName());
        ReadClass readClass = new ReadClass();
        readClass.readFile(file);

        // prints the resulting compilation unit to default system output
        //System.out.println(cu.toString());
    }



}
