package wholemusic.web.controller;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
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
import wholemusic.core.util.SongUtils;
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
                    Album dbAlbum = UniqueHelper.getUniqueAlbum(albumRepository, albumExample);
                    if (dbAlbum == null) {
                        dbAlbum = albumRepository.save(albumExample);
                    }
                    for (Song song : songs) {
                        if (Objects.equals(song.getSongId(), songId)) {
                            Music musicExample = Music.fromSongInterface(song);
                            Music dbMusic = UniqueHelper.getUniqueMusic(musicRepository, musicExample);
                            final String message;
                            if (dbMusic == null) {
                                downloadSongBlocked(dbUser, dbAlbum, song);
                                message = "You successfully downloaded '" + song.getName() + "'";
                            } else {
                                // TODO: add into user
                                message = "You successfully saved '" + song.getName() + "'";
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
                    Album dbAlbum = Album.fromAlbumInterface(album);
                    if (UniqueHelper.getUniqueAlbum(albumRepository, dbAlbum) == null) {
                        dbAlbum = albumRepository.save(dbAlbum);
                    }
                    for (Song song : songs) {
                        Music musicExample = Music.fromSongInterface(song);
                        Music dbMusic = UniqueHelper.getUniqueMusic(musicRepository, musicExample);
                        if (dbMusic == null) {
                            // TODO: song param ?
                            final boolean result = downloadSongBlocked(dbUser, dbAlbum, song);
                            if (result) {
                                successCount++;
                            } else {
                                failedCount++;
                            }
                        } else {
                            // TODO: add into user
                            alreadyExistedCount++;
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

    private boolean downloadSongBlocked(User dbUser, Album dbAlbum, Song song) throws IOException {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(HttpUrl.parse(song.getMusicLink().getUrl()));
        requestBuilder.get();
        // TODO: 网易云音乐海外下载不到
        requestBuilder.addHeader("X-REAL-IP", CommonUtils.generateChinaRandomIP());
        Response response = HttpEngine.requestSync(requestBuilder.build());
        if (response.code() == HttpStatus.SC_OK
                && response.body().contentType().type().toLowerCase().startsWith("audio")) {
            File downloadDir = FileUtils.getDownloadDir(true);
            String path = SongUtils.generateSongPath(song);
            File downloadedFile = new File(downloadDir, path);
            File parentDir = downloadedFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
            sink.writeAll(response.body().source());
            sink.close();
            updateMusicAndUser(dbUser, dbAlbum, song);
            return true;
        }
        return false;
    }

    private void updateMusicAndUser(User dbUser, Album dbAlbum, Song song) {
        Music music = Music.fromSongInterface(song);
        music.setAlbum(dbAlbum);
        dbUser.addMusic(music);
        userRepository.save(dbUser);
    }
}
