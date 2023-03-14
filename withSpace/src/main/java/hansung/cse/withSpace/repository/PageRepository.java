package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.space.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
}
