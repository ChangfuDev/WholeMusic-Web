package wholemusic.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.core.api.MusicApi;
import wholemusic.core.api.MusicApiFactory;
import wholemusic.core.api.MusicProvider;
import wholemusic.core.model.Album;

import java.io.IOException;

/**
 * Created by haohua on 2018/2/17.
 */
@Controller
@RequestMapping("/album")
@SuppressWarnings("unused")
public class AlbumController {
    @GetMapping("/{providerName}/{albumId}")
    public String search(@PathVariable("providerName") String providerName, @PathVariable("albumId") String albumId,
                         ModelMap map) throws IOException {
        MusicProvider provider = MusicProvider.valueOf(providerName);
        if (provider != null) {
            MusicApi api = MusicApiFactory.create(provider);
            if (api != null) {
                Album album = api.getAlbumInfoByIdSync(albumId, true);
                map.addAttribute("album", album);
            }
        }
        return "album/detail";
    }
}
