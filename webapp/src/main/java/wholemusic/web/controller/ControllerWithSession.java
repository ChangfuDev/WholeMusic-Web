package wholemusic.web.controller;

/**
 * Created by haohua on 2018/2/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import wholemusic.core.util.TextUtils;
import wholemusic.web.config.SessionKey;
import wholemusic.web.model.domain.UniqueHelper;
import wholemusic.web.model.domain.User;
import wholemusic.web.model.repository.UserRepository;

import javax.servlet.http.HttpSession;

public class ControllerWithSession {
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
}