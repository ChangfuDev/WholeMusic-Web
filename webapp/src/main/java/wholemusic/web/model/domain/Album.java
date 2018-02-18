package wholemusic.web.model.domain;

/**
 * Created by haohua on 2018/2/13.
 */

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@SuppressWarnings("unused")
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    private String provider;
    private String albumId;
    private String name;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private Set<Music> musics;

    /**
     * 默认构造函数是必须的
     */
    public Album() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Music> getMusics() {
        return musics;
    }

    public void setMusics(Set<Music> musics) {
        this.musics = musics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}