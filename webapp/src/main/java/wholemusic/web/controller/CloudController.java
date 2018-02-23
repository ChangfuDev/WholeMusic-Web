package wholemusic.web.controller;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.core.api.HttpEngine;
import wholemusic.core.api.MusicApi;
import wholemusic.core.api.MusicApiFactory;
import wholemusic.core.api.MusicProvider;
import wholemusic.core.model.Song;
import wholemusic.core.util.CommonUtils;
import wholemusic.web.model.domain.Album;
import wholemusic.web.model.domain.Music;
import wholemusic.web.model.domain.UniqueHelper;
import wholemusic.web.model.domain.User;
import wholemusic.web.model.repository.AlbumRepository;
import wholemusic.web.model.repository.MusicRepository;
import wholemusic.web.model.repository.UserRepository;
import wholemusic.web.util.FileUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


/**
 * Created by haohua on 2018/2/17.
 */
@Controller
@RequestMapping("/cloud")
@SuppressWarnings("unused")
public class CloudController extends ControllerWithSession {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private AlbumRepository albumRepository;

    @GetMapping("/download/{providerName}/{albumId}/{songId}")
    public String downloadSong(@PathVariable("providerName") String providerName, @PathVariable("albumId") String
            albumId, @PathVariable("songId") String songId, HttpServletRequest request, ModelMap map) throws
            IOException {
        logger.info("downloadSong");
        User user = getCurrentUser();
        if (user != null) {
            MusicProvider provider = MusicProvider.valueOf(providerName);
            if (provider != null) {
                MusicApi api = MusicApiFactory.create(provider);
                if (api != null) {
                    wholemusic.core.model.Album album = api.getAlbumInfoByIdSync(albumId, true);
                    List<? extends Song> songs = album.getSongs();
                    User dbUser = UniqueHelper.getUniqueUser(userRepository, user);
                    Album albumExample = Album.fromAlbumInterface(album);
                    Album dbAlbum = UniqueHelper.insertOrGetUniqueAlbum(albumRepository, albumExample);
                    for (Song song : songs) {
                        if (Objects.equals(song.getSongId(), songId)) {
                            SongDownloadResult result = tryDownloadSong(dbUser, dbAlbum, album, song);
                            final String message;
                            switch (result) {
                                case Successful:
                                    message = "You successfully downloaded '" + song.getName() + "'";
                                    break;
                                case AlreadyExisted:
                                    message = "You successfully saved '" + song.getName() + "'";
                                    break;
                                case Failed:
                                    message = "Download failed '" + song.getName() + "'";
                                    break;
                                default:
                                    message = "Result unknown";
                            }
                            map.addAttribute("message", message);
                            break;
                        }
                    }
                }
            }
            return "cloud/download/status";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/download/{providerName}/{albumId}")
    public String downloadAlbum(@PathVariable("providerName") String providerName,
                                @PathVariable("albumId") String albumId, HttpServletRequest request, ModelMap map)
            throws IOException {
        logger.info("downloadAlbum");
        User user = getCurrentUser();
        if (user != null) {
            MusicProvider provider = MusicProvider.valueOf(providerName);
            if (provider != null) {
                MusicApi api = MusicApiFactory.create(provider);
                if (api != null) {
                    wholemusic.core.model.Album album = api.getAlbumInfoByIdSync(albumId, true);
                    List<? extends Song> songs = album.getSongs();
                    int successCount = 0;
                    int failedCount = 0;
                    int alreadyExistedCount = 0;
                    User dbUser = UniqueHelper.getUniqueUser(userRepository, user);
                    Album albumExample = Album.fromAlbumInterface(album);
                    Album dbAlbum = UniqueHelper.insertOrGetUniqueAlbum(albumRepository, albumExample);
                    for (Song song : songs) {
                        SongDownloadResult downloadResult = tryDownloadSong(dbUser, dbAlbum, album, song);
                        switch (downloadResult) {
                            case Failed:
                                failedCount++;
                                break;
                            case Successful:
                                successCount++;
                                break;
                            case AlreadyExisted:
                                alreadyExistedCount++;
                                break;
                        }
                    }
                    final String message = alreadyExistedCount + " already existed, " + successCount + " successfully" +
                            " downloaded, " + failedCount + " failed.";
                    map.addAttribute("message", message);
                }
            }
            return "cloud/download/status";
        } else {
            return "redirect:/";
        }
    }

    private boolean downloadSongBlocked(User dbUser, Album dbAlbum, wholemusic.core.model.Album album, Song song)
            throws IOException {
        File downloadedFile = FileUtils.getDownloadedMusicPath(album, song);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(HttpUrl.parse(song.getMusicLink().getUrl()));
        requestBuilder.get();
        // TODO: 网易云音乐海外下载不到
        requestBuilder.addHeader("X-REAL-IP", CommonUtils.generateChinaRandomIP());
        setProxy();
        Response response = HttpEngine.requestSync(requestBuilder.build(), true);
        logger.info("downloading: {}", downloadedFile);
        if (response.code() == HttpStatus.SC_OK
                && response.body().contentType().type().toLowerCase().startsWith("audio")) {
            File downloadTempFile = new File(downloadedFile.getAbsolutePath() + ".tmp");
            File parentDir = downloadedFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            BufferedSink sink = Okio.buffer(Okio.sink(downloadTempFile));
            sink.writeAll(response.body().source());
            sink.close();
            org.apache.commons.io.FileUtils.moveFile(downloadTempFile, downloadedFile);
            return true;
        }
        return false;
    }

    private void setProxy() {
        String host = env.getProperty("proxy.socks5.host");
        int port = env.getProperty("proxy.socks5.port", int.class);
        if (host != null && port > 0) {
            HttpEngine.setProxy(host, port);
        }
    }

    private void updateMusicAndUser(User dbUser, Album dbAlbum, Song song) {
        Music music = Music.fromSongInterface(song);
        music.setAlbum(dbAlbum);
        dbUser.addMusic(music);
        userRepository.save(dbUser);
    }

    private enum SongDownloadResult {
        Successful, AlreadyExisted, Failed
    }

    private SongDownloadResult tryDownloadSong(User dbUser, Album dbAlbum, wholemusic.core.model.Album album, Song
            song) throws IOException {
        Music musicExample = Music.fromSongInterface(song);
        File targetFile = FileUtils.getDownloadedMusicPath(album, song);
        Music dbMusic = UniqueHelper.getUniqueMusic(musicRepository, musicExample);
        if (dbMusic != null && targetFile.exists()) {
            // 判断数据库项目和文件同时存在的情况下，认为歌曲已经存在
            logger.info("File already exists, path = {}", targetFile);
            return SongDownloadResult.AlreadyExisted;
        } else {
            if (targetFile.exists()) {
                // 文件存在但数据库不存在
                updateMusicAndUser(dbUser, dbAlbum, song);
                return SongDownloadResult.AlreadyExisted;
            } else {
                final boolean result = downloadSongBlocked(dbUser, dbAlbum, album, song);
                updateMusicAndUser(dbUser, dbAlbum, song);
                if (result) {
                    return SongDownloadResult.Successful;
                } else {
                    return SongDownloadResult.Failed;
                }
            }
        }
    }
}
