package wholemusic.web.model.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;

@Entity
@SuppressWarnings("unused")
public class Action implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String ip;
    private Date time;
    private String action;
    private String json;

    /**
     * 默认构造函数是必须的
     */
    public Action() {
    }

    public Action(String ip, Date time, String action, String json) {
        this.ip = ip;
        this.time = time;
        this.action = action;
        this.json = json;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public static Action create(HttpServletRequest request, String action, String json) {
        return new Action(request.getRemoteAddr(), new Date(), action, json);
    }
}
