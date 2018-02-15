package wholemusic.web.controller;

import com.belerweb.social.bean.Result;
import com.belerweb.social.weibo.api.User;
import com.belerweb.social.weibo.api.Weibo;
import com.belerweb.social.weibo.bean.AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wholemusic.web.util.AccountHelper;

/**
 * Created by haohua on 2018/2/15.
 */
@RestController
@RequestMapping("/oauth/callback")
@SuppressWarnings("unused")
public class OAuthCallbackController extends ControllerWithSession {
    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping("/weibo")
    public String weibo(@RequestParam("code") String code) {
        Weibo weibo = AccountHelper.getWeibo();
        User userApi = weibo.getUser();
        Result<AccessToken> tokenResult = weibo.getOAuth2().accessToken(code);
        final boolean tokenSuccess = tokenResult.success();
        if (tokenSuccess) {
            AccountHelper.onWeiboLoggedOn(session, tokenResult.getResult());
        }
        return "redirect:/";
    }
}
