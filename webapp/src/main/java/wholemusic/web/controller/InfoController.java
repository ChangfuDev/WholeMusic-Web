package wholemusic.web.controller;

import com.belerweb.social.weibo.api.Weibo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wholemusic.web.config.Constants;
import wholemusic.web.util.CommonUtils;

import javax.servlet.http.HttpServletRequest;
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
    public HashMap<String, Object> index(HttpServletRequest request) {
        Weibo weibo = new Weibo(Constants.WEIBO_CLIENT_ID, Constants.WEIBO_CLIENT_SECRET, Constants.WEIBO_CALLBACK);
        HashMap<String, Object> map = new HashMap<>();
        map.put("disk", getDiskUsageInfoMap());
        map.put("runtime", getRuntimeInfoMap());
        map.put("request", CommonUtils.getRequestInfoMap(request));
        map.put("auth", weibo.getOAuth2().authorize());
        return map;
    }

    private static HashMap<String, Object> getRuntimeInfoMap() {
        HashMap<String, Object> map = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        map.put("free_memory", CommonUtils.humanReadableByteCount(runtime.freeMemory()));
        map.put("total_memory", CommonUtils.humanReadableByteCount(runtime.totalMemory()));
        map.put("max_memory", CommonUtils.humanReadableByteCount(runtime.maxMemory()));
        map.put("available_processors", runtime.availableProcessors());
        return map;
    }

    private static HashMap<String, Object> getDiskUsageInfoMap() {
        HashMap<String, Object> map = new HashMap<>();
        File dir = new File(".");
        map.put("total_space", CommonUtils.humanReadableByteCount(dir.getTotalSpace()));
        map.put("free_space", CommonUtils.humanReadableByteCount(dir.getFreeSpace()));
        map.put("usable_space", CommonUtils.humanReadableByteCount(dir.getUsableSpace()));
        return map;
    }
}
