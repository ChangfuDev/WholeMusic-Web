package wholemusic.web.model.domain;

/**
 * Created by haohua on 2018/2/13.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@SuppressWarnings("unused")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @SuppressWarnings("SpellCheckingInspection")
    @Column(unique = true)
    private String weiboUid;
    private String nickname;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_music")
    @JsonIgnore
    private Set<Music> musics;

    private Date time;

    /**
     * 注册时的ip地址
     */
    private String ip;

    /**
     * 默认构造函数是必须的
     */
    public User() {
    }

    public User(@SuppressWarnings("SpellCheckingInspection") String weiboUid, String nickname, String ip) {
        this.weiboUid = weiboUid;
        this.nickname = nickname;
        this.ip = ip;
        this.time = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "nickname " + this.nickname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @SuppressWarnings("SpellCheckingInspection")
    public String getWeiboUid() {
        return weiboUid;
    }

    @SuppressWarnings("SpellCheckingInspection")
    public void setWeiboUid(String weiboUid) {
        this.weiboUid = weiboUid;
    }

    public Set<Music> getMusics() {
        return musics;
    }

    public void setMusics(Set<Music> musics) {
        this.musics = musics;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void addMusic(Music music) {
        if (musics == null) {
            musics = new HashSet<>();
        }
        // add operation will be handled by org.hibernate.collection.internal.PersistentSet
        musics.add(music);
    }
}