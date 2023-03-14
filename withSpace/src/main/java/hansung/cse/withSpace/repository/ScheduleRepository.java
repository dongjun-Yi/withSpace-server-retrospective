package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.space.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
