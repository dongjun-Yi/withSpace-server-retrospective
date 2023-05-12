package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.space.schedule.EasyToDo;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EasyToDoRepository extends JpaRepository<EasyToDo, Long> {
}
