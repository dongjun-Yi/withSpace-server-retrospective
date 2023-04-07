package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.chat.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
