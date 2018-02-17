package wholemusic.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wholemusic.core.api.MusicApi;
import wholemusic.core.api.MusicApiFactory;
import wholemusic.core.api.MusicProvider;
import wholemusic.core.api.NotFullyImplementedMusicApi;
import wholemusic.core.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haohua on 2018/2/13.
 */
@Controller
@RequestMapping("/song")
@SuppressWarnings("unused")
public class SongController {
    @GetMapping("/search")
    public String search(@RequestParam("query") String query, ModelMap map) throws IOException {
        ArrayList<Song> result = new ArrayList<>();
        MusicProvider[] providers = MusicProvider.values();
        for (MusicProvider provider : providers) {
            MusicApi api = MusicApiFactory.create(provider);
            if (api != null && !(api instanceof NotFullyImplementedMusicApi)) {
                List<? extends Song> songs = api.searchMusicSync(query, 0, true);
                result.addAll(songs);
            }
        }
        map.addAttribute("songs", result);
        return "song/search";
    }
}
