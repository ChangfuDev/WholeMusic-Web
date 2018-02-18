package wholemusic.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.belerweb.social.bean.Result;
import com.belerweb.social.weibo.api.Weibo;
import com.belerweb.social.weibo.bean.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wholemusic.web.model.domain.Action;
import wholemusic.web.model.domain.User;
import wholemusic.web.model.repository.ActionRepository;
import wholemusic.web.model.repository.UserRepository;
import wholemusic.web.util.WeiboAccountHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by haohua on 2018/2/15.
 */
@Controller
@RequestMapping("/oauth/callback")
@SuppressWarnings("unused")
public class OAuthCallbackController extends ControllerWithSession {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActionRepository actionRepository;

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping("/weibo")
    public String weibo(@RequestParam("code") String code, HttpServletRequest request) {
        Weibo weibo = WeiboAccountHelper.getWeibo();
        Result<AccessToken> tokenResult = weibo.getOAuth2().accessToken(code);
        final boolean tokenSuccess = tokenResult.success();
        if (tokenSuccess) {
            AccessToken token = tokenResult.getResult();
            final String weiboUid = token.getUid();
            User user = tryCreateUser(userRepository, request, weibo, weiboUid);
            WeiboAccountHelper.onWeiboLoggedOn(session, token.getToken(), user);
            Action action = createLoginAction(request, user);
            actionRepository.save(action);
        }
        return "redirect:/";
    }

    static Action createLoginAction(HttpServletRequest request, User user) {
        JSONObject json = new JSONObject();
        json.put("user", user);
        //noinspection SpellCheckingInspection
        Action action = Action.create(request, "weiboLogin", json.toJSONString());
        return action;
    }

    static User tryCreateUser(UserRepository userRepository, HttpServletRequest request,
                              Weibo weibo, @SuppressWarnings("SpellCheckingInspection") String weiboUid) {
        List<User> foundUsers = userRepository.findAll(Example.of(new User(weiboUid, null, null)));
        if (foundUsers.size() == 0) {
            final String screenName = fetchWeiboScreenName(weibo, weiboUid);
            User user = new User(weiboUid, screenName, request.getRemoteAddr());
            return userRepository.save(user);
        } else {
            return foundUsers.get(0);
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static String fetchWeiboScreenName(Weibo weibo, String weiboUid) {
        if (weibo == null) {
            return "微博测试用户";
        }
        Result<com.belerweb.social.weibo.bean.User> userResult = weibo.getUser().show(weibo.getClientId(),
                null, weiboUid, null);
        final String screenName;
        if (userResult.success()) {
            screenName = userResult.getResult().getScreenName();
        } else {
            screenName = "微博用户";
        }
        return screenName;
    }
}
