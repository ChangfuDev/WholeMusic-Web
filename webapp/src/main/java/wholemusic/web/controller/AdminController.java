package wholemusic.web.controller;

/**
 * Created by haohua on 2018/2/14.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wholemusic.web.model.domain.Action;
import wholemusic.web.model.domain.Album;
import wholemusic.web.model.domain.Music;
import wholemusic.web.model.domain.User;
import wholemusic.web.model.repository.ActionRepository;
import wholemusic.web.model.repository.AlbumRepository;
import wholemusic.web.model.repository.MusicRepository;
import wholemusic.web.model.repository.UserRepository;
import wholemusic.web.util.WeiboAccountHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
            return userRepository.findOne(id);
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

    @GetMapping("/testCreate")
    public User testCreate() {
        User user = getCurrentUser();
        if (WeiboAccountHelper.isAdminUser(user)) {
            Music music1 = new Music();
            music1.setName("1.歌曲1");
            Music music2 = new Music();
            music2.setName("1.歌曲2");
            HashSet<Music> musics = new HashSet<>();
            musics.add(music1);
            musics.add(music2);
            user.setMusics(musics);
            userRepository.save(user);
            return null;
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
}