package wholemusic.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.core.api.MusicProvider;
import wholemusic.core.util.TextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by haohua on 2018/2/13.
 */
@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class IndexController extends ControllerWithSession {
    @GetMapping
    public String index(ModelMap map, HttpServletRequest request) {
        addUserInfo(map, request);
        map.addAttribute("providers", getProviderNames());
        return "index";
    }

    private static String getProviderNames() {
        ArrayList<String> providerNames = new ArrayList<>();
        for (MusicProvider provider : SongController.getEnabledProviders()) {
            providerNames.add(provider.toString());
        }
        final String readableNames =  TextUtils.join("、", providerNames);
        return readableNames;
    }
}
