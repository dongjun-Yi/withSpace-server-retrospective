package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.space.schedule.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
}
