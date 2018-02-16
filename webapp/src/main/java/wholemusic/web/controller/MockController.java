package wholemusic.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.web.util.AccountHelper;
import wholemusic.web.util.CommonUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by haohua on 2018/2/15.
 */
@Controller
@RequestMapping("/mock/")
@SuppressWarnings("unused")
public class MockController extends ControllerWithSession {
    @GetMapping("login")
    public String login(HttpServletRequest request) {
        if (CommonUtils.isRequestedByLocalHost(request)) {
            AccountHelper.onWeiboLoggedOn(session, "fake_access_token", "88888888");
        }
        return "redirect:/";
    }
}
