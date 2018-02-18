package wholemusic.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.web.config.SessionKey;

/**
 * Created by haohua on 2018/2/15.
 */
@Controller
@RequestMapping("/logout")
@SuppressWarnings("unused")
public class LogoutController extends ControllerWithSession {
    @GetMapping()
    public String logout() {
        session.removeAttribute(SessionKey.WEIBO_AUTH_INFO);
        session.removeAttribute(SessionKey.USER_INFO);
        return "redirect:/";
    }
}
