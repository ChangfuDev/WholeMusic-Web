package wholemusic.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by haohua on 2018/2/13.
 */
@SpringBootApplication
public class WholeMusicWebApp extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WholeMusicWebApp.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WholeMusicWebApp.class, args);
    }
}