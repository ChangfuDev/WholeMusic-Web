package wholemusic.web.controller;

/**
 * Created by haohua on 2018/2/13.
 */

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wholemusic.web.model.domain.Action;
import wholemusic.web.model.domain.User;
import wholemusic.web.model.repository.ActionRepository;
import wholemusic.web.model.repository.UserRepository;


@RestController
@SuppressWarnings("unused")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActionRepository actionRepository;

    @GetMapping("/getUsers")
    public List<User> getUsers() {
        actionRepository.save(new Action("ip", System.currentTimeMillis(), new Date(), "getUsers", "{}"));
        return userRepository.findAll();
    }

    @RequestMapping("/getUser")
    public User getUser(Long id) {
        return userRepository.findOne(id);
    }

    @RequestMapping("/createUser")
    public User createUser() {
        actionRepository.save(new Action("ip", System.currentTimeMillis(), new Date(), "createUser", "{}"));
        User user = new User();
        user.setUsername(String.valueOf(System.currentTimeMillis()));
        return userRepository.save(user);
    }
}