package wholemusic.web.controller;

/**
 * Created by haohua on 2018/2/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.ModelMap;
import wholemusic.core.util.TextUtils;
import wholemusic.web.config.SessionKey;
import wholemusic.web.model.domain.UniqueHelper;
import wholemusic.web.model.domain.User;
import wholemusic.web.model.repository.UserRepository;
import wholemusic.web.util.CommonUtils;
import wholemusic.web.util.WeiboAccountHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ControllerWithSession {
    @Autowired
    private Environment env;

    @Autowired
    protected HttpSession session;

    @Autowired
    protected UserRepository userRepository;

    protected User getCurrentUser() {
        //noinspection SpellCheckingInspection
        String weiboUid = (String) session.getAttribute(SessionKey.WEIBO_UID);
        if (!TextUtils.isEmpty(weiboUid)) {
            return UniqueHelper.getUniqueUserByWeiboId(userRepository, weiboUid);
        } else {
            return null;
        }
    }

    protected void addUserInfo(ModelMap map, HttpServletRequest request) {
        User user = getCurrentUser();
        map.addAttribute("is_logged_in", user != null);
        if (user != null) {
            // 已登录
            //noinspection SpellCheckingInspection
            map.addAttribute("nickname", user.getNickname());
            map.addAttribute("weibo_uid", user.getWeiboUid());
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
        }
    }
}