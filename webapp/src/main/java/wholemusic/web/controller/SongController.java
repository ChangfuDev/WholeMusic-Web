package wholemusic.web.controller;

import com.alibaba.fastjson.JSONObject;
import freemarker.template.TemplateModelException;
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
import wholemusic.web.util.CommonUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/song")
@SuppressWarnings("unused")
public class SongController extends ControllerWithSession {

    @Autowired
    private ActionRepository actionRepository;

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, HttpServletRequest request, ModelMap map) throws
            TemplateModelException {
        addUserInfo(map, request);
        ArrayList<Object> result = new ArrayList<>();
        MusicProvider[] providers = getEnabledProviders();
        for (MusicProvider provider : providers) {
            final long startTime = System.currentTimeMillis();
            MusicApi api = MusicApiFactory.create(provider);
            if (api != null && !(api instanceof NotFullyImplementedMusicApi)) {
                List<? extends Song> songs = null;
                try {
                    songs = api.searchMusicSync(query, 0, true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                final long timeCost = System.currentTimeMillis() - startTime;
                String message = generateProviderInfoItem(provider, songs, timeCost);
                result.add(message);
                if (songs != null) {
                    result.addAll(songs);
                }
            }
        }
        CommonUtils.insertStatics(map);
        map.addAttribute("songs", result);
        map.addAttribute("query", query);
        JSONObject json = new JSONObject();
        json.put("query", query);
        json.put("result", result);
        json.put("checker", new Checker());
        Action action = Action.create(request, "search", json.toJSONString());
        actionRepository.save(action);
        return "song/search";
    }

    private static String generateProviderInfoItem(MusicProvider provider, List<? extends Song> songs, long timeCost) {
        StringBuilder builder = new StringBuilder();
        builder.append(provider.toString());
        builder.append(" ");
        builder.append("歌曲数: ");
        final long count = songs == null ? 0 : songs.size();
        builder.append(count);
        builder.append(", 耗时: ");
        builder.append(timeCost);
        builder.append("毫秒");
        return builder.toString();
    }

    static MusicProvider[] getEnabledProviders() {
        return MusicProvider.values();
    }

    private static final class Checker {
        public boolean isHeader(Object object) {
            return object instanceof String;
        }
    }
}
