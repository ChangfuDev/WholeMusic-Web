package wholemusic.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.web.model.domain.User;
import wholemusic.web.util.CommonUtils;
import wholemusic.web.util.WeiboAccountHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by haohua on 2018/2/13.
 */
@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class IndexController extends ControllerWithSession {
    @Autowired
    private Environment env;

    @GetMapping
    public String index(ModelMap map, HttpServletRequest request) {
        User user = getCurrentUser();
        if (user != null) {
            // 已登录
            //noinspection SpellCheckingInspection
            map.addAttribute("nickname", user.getNickname());
            map.addAttribute("weibo_uid", user.getWeiboUid());
            return "index";
        } else {
            // 未登录
            final String loginLink;
            final String linkText;
            if (CommonUtils.isRequestedByLocalHost(request)) {
                loginLink = "/mock/login";
                linkText = "模拟登录";
            } else {
                loginLink = WeiboAccountHelper.getWeiboLoginLink(env);
                linkText = "微博登录";
            }
            map.addAttribute("login_link", loginLink);
            map.addAttribute("link_text", linkText);
            return "login";
        }
    }
}
