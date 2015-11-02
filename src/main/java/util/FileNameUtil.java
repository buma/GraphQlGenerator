package util;

/**
 * Created by mabu on 2.11.2015.
 */
public class FileNameUtil {

    public static String getFilenameWithoutExtension(String fileName) {

        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(0, pos);
        }
        return fileName;
    }
}
