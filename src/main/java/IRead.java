import java.io.File;

/**
 * Created by mabu on 2.11.2015.
 */
public interface IRead {

    default String readFile(File file) throws Exception{
        return readFile(file, null);


    }

    String readFile(File file, String qlName) throws Exception;
}
