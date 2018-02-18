package wholemusic.web.controller;

/**
 * Created by haohua on 2018/2/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import wholemusic.web.model.domain.User;
import wholemusic.web.util.WeiboAccountHelper;

import javax.servlet.http.HttpSession;

public class ControllerWithSession {
    @Autowired
    protected HttpSession session;

    protected boolean isLoggedOn() {
        return WeiboAccountHelper.getCurrentUser(session) != null;
    }

    protected User getCurrentUser() {
        return WeiboAccountHelper.getCurrentUser(session);
    }
}