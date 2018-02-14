package wholemusic.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wholemusic.core.Main;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by haohua on 2018/2/13.
 */
@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class IndexController {
    @GetMapping
    public String index(ModelMap map) throws IOException {
        // Main.main(null);
        map.addAttribute("key", "value");
        return "index";
    }
}
