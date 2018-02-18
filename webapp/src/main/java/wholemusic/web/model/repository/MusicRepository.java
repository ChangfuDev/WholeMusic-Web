package wholemusic.web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wholemusic.web.model.domain.Music;

/**
 * Created by haohua on 2018/2/14.
 */
public interface MusicRepository extends JpaRepository<Music, Long> {
}