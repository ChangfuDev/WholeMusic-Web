package wholemusic.web.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@SuppressWarnings("SpellCheckingInspection")
@Component
@Scope("session")
public class WeiboAuthInfo implements Serializable {
    public String accessToken;
    public String uid;
}
