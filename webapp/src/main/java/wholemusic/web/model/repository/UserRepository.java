package wholemusic.web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wholemusic.web.model.domain.User;

/**
 * Created by haohua on 2018/2/13.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}