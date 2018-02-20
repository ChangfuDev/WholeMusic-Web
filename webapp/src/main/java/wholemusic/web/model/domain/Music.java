package wholemusic.web.model.domain;

/**
 * Created by haohua on 2018/2/13.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import wholemusic.core.api.MusicProvider;
import wholemusic.core.model.Song;
import wholemusic.core.util.TextUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@SuppressWarnings("unused")
public class Music implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    private String provider;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("musics")
    private Album album;

    @Column(unique = true)
    private String songId;
    private String name;

    private Date time;

    @ManyToMany(mappedBy = "musics", cascade = CascadeType.ALL)
    @JsonIgnore
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

    public static Music fromSongInterface(Song song) {
        Music music = new Music();
        music.setName(song.getName());
        music.setSongId(song.getSongId());
        music.setProvider(song.getMusicProvider().name());
        music.setTime(new Date());
        return music;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public MusicProvider getMusicProvider() {
        if (!TextUtils.isEmpty(provider)) {
            return MusicProvider.valueOf(provider);
        } else {
            return null;
        }
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}