package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.space.schedule.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
