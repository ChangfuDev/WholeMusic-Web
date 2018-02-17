package wholemusic.web.controller;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.core.api.HttpEngine;
import wholemusic.core.api.MusicApi;
import wholemusic.core.api.MusicApiFactory;
import wholemusic.core.api.MusicProvider;
import wholemusic.core.model.Album;
import wholemusic.core.model.Song;
import wholemusic.core.util.CommonUtils;
import wholemusic.core.util.SongUtils;
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
public class CloudController {
    @GetMapping("/download/{providerName}/{albumId}/{songId}")
    public String downloadSong(@PathVariable("providerName") String providerName, @PathVariable("albumId") String
            albumId,
                               @PathVariable("songId") String songId, HttpServletRequest request,
                               ModelMap map) throws IOException {
        MusicProvider provider = MusicProvider.valueOf(providerName);
        if (provider != null) {
            MusicApi api = MusicApiFactory.create(provider);
            if (api != null) {
                Album album = api.getAlbumInfoByIdSync(albumId, true);
                List<? extends Song> songs = album.getSongs();
                for (Song song : songs) {
                    if (Objects.equals(song.getSongId(), songId)) {
                        downloadSongBlocked(album, song);
                        map.addAttribute("message",
                                "You successfully downloaded '" + song.getName() + "'");
                        break;
                    }
                }
            }
        }
        return "cloud/download/status";
    }

    @GetMapping("/download/{providerName}/{albumId}")
    public String downloadAlbum(@PathVariable("providerName") String providerName,
                                @PathVariable("albumId") String albumId, HttpServletRequest request, ModelMap map)
            throws IOException {
        MusicProvider provider = MusicProvider.valueOf(providerName);
        if (provider != null) {
            MusicApi api = MusicApiFactory.create(provider);
            if (api != null) {
                Album album = api.getAlbumInfoByIdSync(albumId, true);
                List<? extends Song> songs = album.getSongs();
                int successCount = 0;
                int failedCount = 0;
                for (Song song : songs) {
                    final boolean result = downloadSongBlocked(album, song);
                    if (result) {
                        successCount++;
                    } else {
                        failedCount++;
                    }
                }
                final String message = successCount + " successfully downloaded, " + failedCount + " failed.";
                map.addAttribute("message", message);
            }
        }
        return "cloud/download/status";
    }

    private static boolean downloadSongBlocked(Album album, Song song) throws IOException {
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
            return true;
        }
        return false;
    }
}
