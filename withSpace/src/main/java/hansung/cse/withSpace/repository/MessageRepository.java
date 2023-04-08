package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
