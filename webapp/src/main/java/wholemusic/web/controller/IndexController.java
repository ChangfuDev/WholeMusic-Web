package wholemusic.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.web.config.SessionKey;
import wholemusic.web.model.WeiboAuthInfo;
import wholemusic.web.model.domain.User;
import wholemusic.web.util.WeiboAccountHelper;
import wholemusic.web.util.CommonUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by haohua on 2018/2/13.
 */
@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class IndexController extends ControllerWithSession {
    @GetMapping
    public String index(ModelMap map, HttpServletRequest request) {
        if (isLoggedOn()) {
            // 已登录
            //noinspection SpellCheckingInspection
            WeiboAuthInfo auth = (WeiboAuthInfo) session.getAttribute(SessionKey.WEIBO_AUTH_INFO);
            User user = getCurrentUser();
            map.addAttribute("nickname", user.getNickname());
            map.addAttribute("weibo_uid", auth.uid);
            return "index";
        } else {
            // 未登录
            final String loginLink;
            final String linkText;
            if (CommonUtils.isRequestedByLocalHost(request)) {
                loginLink = "/mock/login";
                linkText = "模拟登录";
            } else {
                loginLink = WeiboAccountHelper.getWeiboLoginLink();
                linkText = "微博登录";
            }
            map.addAttribute("login_link", loginLink);
            map.addAttribute("link_text", linkText);
            return "login";
        }
    }
}
