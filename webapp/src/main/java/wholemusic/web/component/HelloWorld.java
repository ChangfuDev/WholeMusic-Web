package wholemusic.web.component;

import org.springframework.stereotype.Component;
import wholemusic.core.Main;

import java.io.IOException;

/**
 * Created by haohua on 2018/2/13.
 */
@Component
public class HelloWorld {
    public String hello() throws IOException {
        System.out.println("Hello Spring!");
        Main.main(null);
        return "Hello World!";
    }
}