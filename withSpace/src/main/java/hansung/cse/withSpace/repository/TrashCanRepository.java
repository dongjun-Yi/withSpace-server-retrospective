package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.domain.space.TrashCan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashCanRepository  extends JpaRepository<TrashCan, Long> {
}
