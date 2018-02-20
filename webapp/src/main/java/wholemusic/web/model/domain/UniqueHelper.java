package wholemusic.web.model.domain;

import org.springframework.data.domain.Example;
import wholemusic.web.model.repository.AlbumRepository;
import wholemusic.web.model.repository.MusicRepository;
import wholemusic.web.model.repository.UserRepository;

public class UniqueHelper {

    public static User getUniqueUser(UserRepository repo, User user) {
        return getUniqueUserByWeiboId(repo, user.getWeiboUid());
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static User getUniqueUserByWeiboId(UserRepository repo, String weiboUid) {
        User example = new User();
        example.setWeiboUid(weiboUid);
        User one = repo.findOne(Example.of(example));
        return one;
    }

    private static Album getUniqueAlbum(AlbumRepository repo, Album album) {
        Album example = new Album();
        example.setAlbumId(album.getAlbumId());
        example.setProvider(album.getProvider());
        Album one = repo.findOne(Example.of(example));
        return one;
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
        Music one = repo.findOne(Example.of(example));
        return one;
    }
}
