package micro.points;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface PointRepository extends PagingAndSortingRepository<Point, UUID> {
    Optional<Point> findByName(String name);
}
