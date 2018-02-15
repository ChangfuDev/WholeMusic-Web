package wholemusic.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wholemusic.web.config.SessionKey;

import java.util.HashMap;

/**
 * Created by haohua on 2018/2/15.
 */
@RestController
@RequestMapping("/logout")
@SuppressWarnings("unused")
public class LogoutController extends ControllerWithSession {
    @GetMapping()
    public HashMap<String, Object> logout() {
        session.removeAttribute(SessionKey.WEIBO_AUTH_INFO);
        HashMap<String, Object> map = new HashMap<>();
        return map;
    }
}
