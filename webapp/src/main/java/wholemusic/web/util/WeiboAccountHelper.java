package wholemusic.web.util;

import com.belerweb.social.weibo.api.OAuth2;
import com.belerweb.social.weibo.api.Weibo;
import org.springframework.core.env.Environment;
import wholemusic.web.config.SessionKey;
import wholemusic.web.model.domain.User;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class WeiboAccountHelper {

    public static final String FAKE_WEIBO_UID = "88888888";

    @SuppressWarnings("SpellCheckingInspection")
    public static String getWeiboLoginLink(Environment env) {
        return getWeiboOAuth2(env).authorize();
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static OAuth2 getWeiboOAuth2(Environment env) {
        return getWeibo(env).getOAuth2();
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static Weibo getWeibo(Environment env) {
        Weibo weibo = new Weibo(env.getProperty("oauth.weibo_client_id"), env.getProperty("oauth.weibo_client_secret"),
                env.getProperty("oauth.weibo_callback"));
        return weibo;
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static void onWeiboLoggedOn(HttpSession session, String accessToken, User user) {
        session.setAttribute(SessionKey.WEIBO_ACCESS_TOKEN, accessToken);
        session.setAttribute(SessionKey.WEIBO_UID, user.getWeiboUid());
    }

    public static boolean isAdminUser(User user) {
        if (user != null) {
            String uid = user.getWeiboUid();
            List<String> admins = Arrays.asList("1173510540", FAKE_WEIBO_UID);
            return admins.contains(uid);
        } else {
            return false;
        }
    }
}
