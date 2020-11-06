package micro.points;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import micro.points.location.Location;
import micro.points.location.PointLocation;
import micro.points.opening.OpeningHours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointOfInterestService {

    private final PointRepository repository;
    private final LocationUpdateClient client;

    @Transactional
    public Point update(UUID id, PointUpdate update) {
        Point point = repository.findById(id).orElseThrow(NotExists::new);
        if (update.getLocation() != null) {
            point.setOpeningHours(update.getOpeningHours());
        }
        if (update.getLocation() != null && !Objects.equals(point.getLocation(), update.getLocation())) {
            point.setLocation(update.getLocation());
            client.sendLocationUpdate(id, PointLocation.from(point.getLocation()));
        }
        repository.save(point);
        return point;
    }

    @Transactional(readOnly = true)
    Page<Point> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    Point findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(NotExists::new);
    }

    @Value
    @Builder
    public static class PointUpdate {
        @Valid
        Location location;
        @Valid
        OpeningHours openingHours;

        public static PointUpdate location(Location location) {
            return builder().location(location).build();
        }

        public static PointUpdate opening(OpeningHours opening) {
            return builder().openingHours(opening).build();
        }
    }
}
