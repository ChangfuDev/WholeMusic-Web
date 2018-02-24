package wholemusic.web.controller;

/**
 * Created by haohua on 2018/2/19.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.web.model.domain.Music;
import wholemusic.web.model.domain.UniqueHelper;
import wholemusic.web.model.domain.User;
import wholemusic.web.model.repository.MusicRepository;
import wholemusic.web.model.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Controller
@RequestMapping("/home")
@SuppressWarnings("unused")
public class HomeController extends ControllerWithSession {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MusicRepository musicRepository;

    @GetMapping
    public String index(ModelMap map, HttpServletRequest request) {
        addUserInfo(map, request);
        User user = getCurrentUser();
        if (user != null) {
            User dbUser = UniqueHelper.getUniqueUser(userRepository, user);
            Set<Music> musics = musicRepository.findAllByUsers(dbUser);
            map.put("musics", musics);
            return "home/index";
        } else {
            return "redirect:/";
        }
    }
}