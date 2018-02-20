package wholemusic.web.model.domain;

/**
 * Created by haohua on 2018/2/13.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@SuppressWarnings("unused")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "album_id"}))
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    private String provider;

    @Column(name = "album_id")
    private String albumId;
    private String name;

    private Date time;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("album")
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

    public static Album fromAlbumInterface(wholemusic.core.model.Album album) {
        Album result = new Album();
        result.setName(album.getName());
        result.setAlbumId(album.getAlbumId());
        result.setProvider(album.getMusicProvider().name());
        result.setTime(new Date());
        return result;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}