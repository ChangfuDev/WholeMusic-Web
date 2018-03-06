package wholemusic.web.model.domain;

import org.springframework.data.domain.Example;
import wholemusic.web.model.repository.AlbumRepository;
import wholemusic.web.model.repository.MusicRepository;
import wholemusic.web.model.repository.UserRepository;

import java.util.Optional;

public class UniqueHelper {

    public static User getUniqueUser(UserRepository repo, User user) {
        return getUniqueUserByWeiboId(repo, user.getWeiboUid());
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static User getUniqueUserByWeiboId(UserRepository repo, String weiboUid) {
        User example = new User();
        example.setWeiboUid(weiboUid);
        Optional<User> result = repo.findOne(Example.of(example));
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    private static Album getUniqueAlbum(AlbumRepository repo, Album album) {
        Album example = new Album();
        example.setAlbumId(album.getAlbumId());
        example.setProvider(album.getProvider());
        Optional<Album> result = repo.findOne(Example.of(example));
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public static Album insertOrGetUniqueAlbum(AlbumRepository repo, Album albumExample) {
        Album dbExistedAlbum = getUniqueAlbum(repo, albumExample);
        if (dbExistedAlbum == null) {
            dbExistedAlbum = repo.save(albumExample);
        }
        return dbExistedAlbum;
    }

    public static Music getUniqueMusic(MusicRepository repo, Music music) {
        Music example = new Music();
        example.setSongId(music.getSongId());
        example.setProvider(music.getProvider());
        Optional<Music> result = repo.findOne(Example.of(example));
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }
}
