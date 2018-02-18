package wholemusic.web.model.domain;

/**
 * Created by haohua on 2018/2/13.
 */

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@SuppressWarnings("unused")
public class Music implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    private String provider;
    @ManyToOne
    private Album album;
    private String songId;
    private String name;

    @ManyToMany(mappedBy = "musics", cascade = CascadeType.ALL)
    private Set<User> users;

    /**
     * 默认构造函数是必须的
     */
    public Music() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}