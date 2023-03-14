package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.space.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {


}
