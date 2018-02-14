package wholemusic.web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wholemusic.web.model.domain.Action;

/**
 * Created by haohua on 2018/2/14.
 */
public interface ActionRepository extends JpaRepository<Action, Long> {
}