package wholemusic.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import wholemusic.web.model.domain.Action;
import wholemusic.web.model.repository.ActionRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haohua on 2018/2/13.
 */
@Controller
@RequestMapping("/song")
@SuppressWarnings("unused")
public class SongController extends ControllerWithSession {

    @Autowired
    private ActionRepository actionRepository;

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, HttpServletRequest request, ModelMap map) {
        addUserInfo(map, request);
        ArrayList<Song> result = new ArrayList<>();
        MusicProvider[] providers = getEnabledProviders();
        for (MusicProvider provider : providers) {
            MusicApi api = MusicApiFactory.create(provider);
            if (api != null && !(api instanceof NotFullyImplementedMusicApi)) {
                try {
                    List<? extends Song> songs = api.searchMusicSync(query, 0, true);
                    result.addAll(songs);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        map.addAttribute("songs", result);
        map.addAttribute("query", query);
        JSONObject json = new JSONObject();
        json.put("query", query);
        json.put("result", result);
        Action action = Action.create(request, "search", json.toJSONString());
        actionRepository.save(action);
        return "song/search";
    }

    private MusicProvider[] getEnabledProviders() {
        return MusicProvider.values();
    }
}
