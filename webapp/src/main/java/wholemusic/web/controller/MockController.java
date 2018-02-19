package wholemusic.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.web.model.domain.Action;
import wholemusic.web.model.domain.User;
import wholemusic.web.model.repository.ActionRepository;
import wholemusic.web.model.repository.UserRepository;
import wholemusic.web.util.CommonUtils;
import wholemusic.web.util.WeiboAccountHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by haohua on 2018/2/15.
 */
@Controller
@RequestMapping("/mock/")
@SuppressWarnings("unused")
public class MockController extends ControllerWithSession {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActionRepository actionRepository;

    @GetMapping("login")
    public String login(HttpServletRequest request) throws JsonProcessingException {
        if (CommonUtils.isRequestedByLocalHost(request)) {
            //noinspection SpellCheckingInspection
            User user = OAuthCallbackController.tryCreateUser(userRepository, request, null,
                    WeiboAccountHelper.FAKE_WEIBO_UID);
            WeiboAccountHelper.onWeiboLoggedOn(session, "fake_access_token", user);
            Action action = OAuthCallbackController.createLoginAction(request, user);
            actionRepository.save(action);
        }
        return "redirect:/";
    }
}
