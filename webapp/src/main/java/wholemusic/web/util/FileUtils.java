package wholemusic.web.util;

import org.springframework.boot.system.ApplicationHome;
import wholemusic.core.model.Song;
import wholemusic.core.util.SongUtils;

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

    /**
     * 通过专辑和歌曲对象获得本地音乐文件绝对路径
     *
     * @param album
     * @param song
     * @return
     */
    public static File getDownloadedMusicPath(wholemusic.core.model.Album album, Song song) {
        File downloadDir = getDownloadDir(true);
        String path = SongUtils.generateSongPath(album, song);
        File downloadedFile = new File(downloadDir, path);
        return downloadedFile;
    }
}
