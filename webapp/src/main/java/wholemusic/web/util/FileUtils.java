package wholemusic.web.util;

import org.springframework.boot.ApplicationHome;

import java.io.File;

public class FileUtils {
    public static File getRootDirectory() {
        ApplicationHome home = new ApplicationHome(FileUtils.class);
        // The path is like: /var/lib/tomcat8/webapps/
        File dir = home.getDir().getParentFile().getParentFile().getParentFile();
        return dir;
    }

    public static File getStorageDirectory() {
        return new File(getRootDirectory(), "storage");
    }

    public static File getUploadDir() {
        return new File(getStorageDirectory(), "upload");
    }
}
