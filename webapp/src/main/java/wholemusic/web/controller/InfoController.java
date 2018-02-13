package wholemusic.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wholemusic.web.util.CommonUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by haohua on 2018/2/13.
 */
@RestController
@RequestMapping("/info")
@SuppressWarnings("unused")
public class InfoController {
    @GetMapping()
    public HashMap<String, String> index() {
        File dir = new File(".");
        HashMap<String, String> map = new HashMap<>();
        map.put("total_space", CommonUtils.humanReadableByteCount(dir.getTotalSpace()));
        map.put("free_space", CommonUtils.humanReadableByteCount(dir.getFreeSpace()));
        map.put("usable_space", CommonUtils.humanReadableByteCount(dir.getUsableSpace()));
        return map;
    }
}
