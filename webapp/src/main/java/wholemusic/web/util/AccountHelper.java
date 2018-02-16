package wholemusic.web.util;

import com.belerweb.social.weibo.api.OAuth2;
import com.belerweb.social.weibo.api.Weibo;
import com.belerweb.social.weibo.bean.AccessToken;
import wholemusic.web.config.Constants;
import wholemusic.web.config.SessionKey;
import wholemusic.web.model.WeiboAuthInfo;

import javax.servlet.http.HttpSession;

public class AccountHelper {
    @SuppressWarnings("SpellCheckingInspection")
    public static String getWeiboLoginLink() {
        return getWeiboOAuth2().authorize();
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static OAuth2 getWeiboOAuth2() {
        return getWeibo().getOAuth2();
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static Weibo getWeibo() {
        Weibo weibo = new Weibo(Constants.WEIBO_CLIENT_ID, Constants.WEIBO_CLIENT_SECRET, Constants.WEIBO_CALLBACK);
        return weibo;
    }

    public static boolean isLoggedOn(HttpSession session) {
        WeiboAuthInfo info = (WeiboAuthInfo) session.getAttribute(SessionKey.WEIBO_AUTH_INFO);
        return info != null;
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static void onWeiboLoggedOn(HttpSession session, String accessToken, String uid) {
        WeiboAuthInfo auth = new WeiboAuthInfo();
        auth.accessToken = accessToken;
        auth.uid =  uid;
        session.setAttribute(SessionKey.WEIBO_AUTH_INFO, auth);
    }
}
