package wholemusic.web.controller;

import com.belerweb.social.bean.Result;
import com.belerweb.social.weibo.api.Weibo;
import com.belerweb.social.weibo.bean.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wholemusic.web.model.domain.Action;
import wholemusic.web.model.domain.UniqueHelper;
import wholemusic.web.model.domain.User;
import wholemusic.web.model.repository.ActionRepository;
import wholemusic.web.model.repository.UserRepository;
import wholemusic.web.util.WeiboAccountHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by haohua on 2018/2/15.
 */
@Controller
@RequestMapping("/oauth/callback")
@SuppressWarnings("unused")
public class OAuthCallbackController extends ControllerWithSession {
    @Autowired
    private Environment env;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActionRepository actionRepository;

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping("/weibo")
    public String weibo(@RequestParam("code") String code, HttpServletRequest request) throws JsonProcessingException {
        Weibo weibo = WeiboAccountHelper.getWeibo(env);
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

    static Action createLoginAction(HttpServletRequest request, User user) throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(user);
        //noinspection SpellCheckingInspection
        Action action = Action.create(request, "weiboLogin", json);
        return action;
    }

    static User tryCreateUser(UserRepository userRepository, HttpServletRequest request,
                              Weibo weibo, @SuppressWarnings("SpellCheckingInspection") String weiboUid) {
        User existedUser = UniqueHelper.getUniqueUserByWeiboId(userRepository, weiboUid);
        if (existedUser == null) {
            final String screenName = fetchWeiboScreenName(weibo, weiboUid);
            User user = new User(weiboUid, screenName, request.getRemoteAddr());
            return userRepository.save(user);
        } else {
            return existedUser;
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
