package wholemusic.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.web.util.AccountHelper;

/**
 * Created by haohua on 2018/2/13.
 */
@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class IndexController extends ControllerWithSession {
    @GetMapping
    public String index(ModelMap map) {
        if (isLoggedOn()) {
            // 已登录
            return "index";
        } else {
            // 未登录
            map.addAttribute("login_link", AccountHelper.getWeiboLoginLink());
            return "login";
        }
    }
}
