package wholemusic.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by haohua on 2018/2/13.
 */
@RestController
@RequestMapping("/song")
@SuppressWarnings("unused")
public class SongController {

    @GetMapping("/")
    public String index() {
        return "song index";
    }

    @GetMapping("/search/{query}")
    public String search(String query) {
        return "song search: " + query;
    }
}
