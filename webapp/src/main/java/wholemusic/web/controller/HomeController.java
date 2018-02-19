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
import wholemusic.web.model.repository.ActionRepository;
import wholemusic.web.model.repository.AlbumRepository;
import wholemusic.web.model.repository.MusicRepository;
import wholemusic.web.model.repository.UserRepository;

import java.util.Set;

@Controller
@RequestMapping("/home")
@SuppressWarnings("unused")
public class HomeController extends ControllerWithSession {
    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @GetMapping
    public String index(ModelMap map) {
        User user = getCurrentUser();
        if (user != null) {
            map.addAttribute("nickname", user.getNickname());
            map.addAttribute("weibo_uid", user.getWeiboUid());
            User dbUser = UniqueHelper.getUniqueUser(userRepository, user);
            Set<Music> musics = musicRepository.findAllByUsers(dbUser);
            map.put("musics", musics);
            return "home/index";
        } else {
            return "redirect:/";
        }
    }
}