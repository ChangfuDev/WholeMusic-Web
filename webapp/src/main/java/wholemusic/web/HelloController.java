package wholemusic.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by haohua on 2018/2/13.
 */
@RestController("/helloworld")
public class HelloController {

    private HelloWorld helloWorld;

    public HelloController(HelloWorld helloWorld) {
        this.helloWorld = helloWorld;
    }

    @GetMapping
    public String hello() throws IOException {
        return helloWorld.hello();
    }
}
