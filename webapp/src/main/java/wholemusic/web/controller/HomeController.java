package wholemusic.web.controller;

import freemarker.template.TemplateModelException;
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
import wholemusic.web.util.CommonUtils;

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
    public String index(ModelMap map, HttpServletRequest request) throws TemplateModelException {
        addUserInfo(map, request);
        User user = getCurrentUser();
        if (user != null) {
            User dbUser = UniqueHelper.getUniqueUser(userRepository, user);
            Set<Music> songs = musicRepository.findAllByUsers(dbUser);
            CommonUtils.insertStatics(map);
            map.put("songs", songs);
            map.put("isCloudDisk", true);
            return "home/index";
        } else {
            return "redirect:/";
        }
    }
}