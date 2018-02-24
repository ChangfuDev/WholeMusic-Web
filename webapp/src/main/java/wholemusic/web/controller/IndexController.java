package wholemusic.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by haohua on 2018/2/13.
 */
@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class IndexController extends ControllerWithSession {
    @GetMapping
    public String index(ModelMap map, HttpServletRequest request) {
        addUserInfo(map, request);
        return "index";
    }
}
