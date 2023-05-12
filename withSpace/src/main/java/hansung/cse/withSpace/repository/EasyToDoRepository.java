package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.space.schedule.EasyToDo;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EasyToDoRepository extends JpaRepository<EasyToDo, Long> {
    Optional<EasyToDo> findByEasyMake(UUID easyToDoId);
}
