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

    private static File getStorageDirectory() {
        return new File(getRootDirectory(), "storage");
    }

    public static File getUploadDir(boolean mkdirs) {
        File dir = new File(getStorageDirectory(), "upload");
        if (mkdirs && !dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getDownloadDir(boolean mkdirs) {
        File dir = new File(getStorageDirectory(), "download");
        if (mkdirs && !dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
}
