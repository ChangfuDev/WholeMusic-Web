package wholemusic.web.controller;

/**
 * Created by haohua on 2018/2/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import wholemusic.web.util.AccountHelper;

import javax.servlet.http.HttpSession;

public class ControllerWithSession {
    @Autowired
    protected HttpSession session;

    protected boolean isLoggedOn() {
        return AccountHelper.isLoggedOn(session);
    }
}