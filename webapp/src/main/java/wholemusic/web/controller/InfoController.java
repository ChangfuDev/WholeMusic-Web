package wholemusic.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wholemusic.web.util.CommonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by haohua on 2018/2/13.
 */
@RestController
@RequestMapping("/info")
@SuppressWarnings("unused")
public class InfoController extends ControllerWithSession {
    @GetMapping
    public HashMap<String, Object> index(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("disk", getDiskUsageInfoMap());
        map.put("runtime", getRuntimeInfoMap());
        map.put("request", CommonUtils.getRequestInfoMap(request));
        if (isRequestedByLocalHost(request)) {
            map.put("session_info", getSessionInfo(session));
        }
        return map;
    }

    private static HashMap<String, Object> getSessionInfo(HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();
        Enumeration<String> names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            final String name = names.nextElement();
            map.put(name, session.getAttribute(name));
        }
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

    private static boolean isRequestedByLocalHost(HttpServletRequest request) {
        String host = request.getHeader("host");
        return Objects.equals(request.getRemoteAddr(), request.getLocalAddr())
                && host != null && host.contains("localhost");
    }
}
