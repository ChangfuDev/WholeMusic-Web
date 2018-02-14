package wholemusic.web.controller;

/**
 * Created by haohua on 2018/2/14.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wholemusic.web.model.domain.Action;
import wholemusic.web.model.repository.ActionRepository;

import java.util.List;

@RestController
@SuppressWarnings("unused")
public class ActionController {
    @Autowired
    private ActionRepository actionRepository;

    @GetMapping("/getActions")
    public List<Action> getUsers() {
        return actionRepository.findAll();
    }
}