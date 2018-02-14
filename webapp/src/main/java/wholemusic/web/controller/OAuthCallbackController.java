package wholemusic.web.controller;

import com.belerweb.social.bean.Result;
import com.belerweb.social.weibo.api.User;
import com.belerweb.social.weibo.api.Weibo;
import com.belerweb.social.weibo.bean.AccessToken;
import com.belerweb.social.weibo.bean.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wholemusic.web.config.Constants;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by haohua on 2018/2/15.
 */
@RestController
@RequestMapping("/oauth/callback")
@SuppressWarnings("unused")
public class OAuthCallbackController {
    @Autowired
    private HttpSession session;

    @SuppressWarnings("SpellCheckingInspection")
    @GetMapping("/weibo")
    public HashMap<String, Object> weibo(@RequestParam("code") String code) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        Weibo weibo = new Weibo(Constants.WEIBO_CLIENT_ID, Constants.WEIBO_CLIENT_SECRET, Constants.WEIBO_CALLBACK);
        User userApi = weibo.getUser();
        Result<AccessToken> tokenResult = weibo.getOAuth2().accessToken(code);
        final boolean tokenSuccess = tokenResult.success();
        map.put("access_token_success", tokenSuccess);
        map.put("session_access_token", session.getAttribute("access_token"));
        if (tokenSuccess) {
            AccessToken token = tokenResult.getResult();
            session.setAttribute("access_token", token.getToken());
            map.put("access_token", token.getJsonObject().toString());
            Result<TokenInfo> tokenInfoResult = weibo.getOAuth2().getTokenInfo(token.getToken());
            if (tokenInfoResult.success()) {
                TokenInfo tokenInfo = tokenInfoResult.getResult();
                map.put("token_info", tokenInfo.getJsonObject().toString());
            }
        }
        return map;
    }
}
