package wholemusic.web.controller;


import com.sun.media.jfxmediaimpl.MediaUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.core.api.MusicApi;
import wholemusic.core.api.MusicApiFactory;
import wholemusic.core.api.MusicProvider;
import wholemusic.core.model.Album;
import wholemusic.core.model.Song;
import wholemusic.core.util.SongUtils;
import wholemusic.web.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Controller
@RequestMapping("/disk")
public class DiskController {
    @GetMapping(value = "/{providerName}/{albumId}/{songId}")
    public ResponseEntity<Resource> downloadMusic(@PathVariable("providerName") String providerName, @PathVariable
            ("albumId") String albumId, @PathVariable("songId") String songId) throws IOException {
        MusicProvider provider = MusicProvider.valueOf(providerName);
        if (provider != null) {
            MusicApi api = MusicApiFactory.create(provider);
            if (api != null) {
                Album album = api.getAlbumInfoByIdSync(albumId, true);
                List<? extends Song> songs = album.getSongs();
                for (Song song : songs) {
                    if (Objects.equals(song.getSongId(), songId)) {
                        File downloadDir = FileUtils.getDownloadDir(true);
                        String path = SongUtils.generateSongPath(song);
                        File file = new File(downloadDir, path);
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.valueOf(MediaUtils.CONTENT_TYPE_MPA));
                        FileSystemResource fileSystemResource = new FileSystemResource(file);
                        // TODO: download/play: octet/mp3 and filename
                        return new ResponseEntity<>(fileSystemResource, headers, HttpStatus.OK);
                    }
                }
            }
        }
        return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }
}