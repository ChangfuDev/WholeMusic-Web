package wholemusic.web.controller;

/**
 * Created by haohua on 2018/2/14.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wholemusic.web.model.domain.*;
import wholemusic.web.model.repository.ActionRepository;
import wholemusic.web.model.repository.AlbumRepository;
import wholemusic.web.model.repository.MusicRepository;
import wholemusic.web.model.repository.UserRepository;
import wholemusic.web.util.CommonUtils;
import wholemusic.web.util.WeiboAccountHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/admin")
@SuppressWarnings("unused")
public class AdminController extends ControllerWithSession {
    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @GetMapping("/getActions")
    public List<Action> getActions() {
        User user = getCurrentUser();
        if (WeiboAccountHelper.isAdminUser(user)) {
            return actionRepository.findAll();
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/getUsers")
    public List<User> getUsers() {
        User user = getCurrentUser();
        if (WeiboAccountHelper.isAdminUser(user)) {
            return userRepository.findAll();
        } else {
            return new ArrayList<>();
        }
    }

    @RequestMapping("/getUser/{id}")
    public User getUser(@PathVariable Long id) {
        User user = getCurrentUser();
        if (WeiboAccountHelper.isAdminUser(user)) {
            return userRepository.getOne(id);
        } else {
            return null;
        }
    }

    @GetMapping("/getMusics")
    public List<Music> getMusics() {
        User user = getCurrentUser();
        if (WeiboAccountHelper.isAdminUser(user)) {
            return musicRepository.findAll();
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/getMusic/{id}")
    public Music getMusic(@PathVariable Long id) {
        User user = getCurrentUser();
        if (WeiboAccountHelper.isAdminUser(user)) {
            return musicRepository.getOne(id);
        } else {
            return null;
        }
    }

    @GetMapping("/getAlbums")
    public List<Album> getAlbums() {
        User user = getCurrentUser();
        if (WeiboAccountHelper.isAdminUser(user)) {
            return albumRepository.findAll();
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/getAlbum/{id}")
    public Album getAlbum(@PathVariable Long id) {
        User user = getCurrentUser();
        if (WeiboAccountHelper.isAdminUser(user)) {
            return albumRepository.getOne(id);
        } else {
            return null;
        }
    }

    @GetMapping("/info")
    public HashMap<String, Object> info(HttpServletRequest request) {
        User user = getCurrentUser();
        HashMap<String, Object> map = new HashMap<>();
        if (WeiboAccountHelper.isAdminUser(user)) {
            map.put("disk", getDiskUsageInfoMap());
            map.put("runtime", getRuntimeInfoMap());
            map.put("request", CommonUtils.getRequestInfoMap(request));
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
}