package micro.points;

import micro.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;

import static micro.points.PointsFixture.Opening.openAtWorkWeek;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class PointRepositoryIntegrationTest {

    @Autowired
    PointRepository repository;

    @Test
    public void persistPoint() {

        // given
        Point point = PointsFixture.example();

        // when
        repository.save(point);
        Optional<Point> saved = repository.findByName(point.getName());

        // then
        Assertions.assertThat(saved).map(Point::getId).isNotNull();
        Assertions.assertThat(saved).map(Point::getAddedOn).isNotNull();
        Assertions.assertThat(saved).map(Point::getModifiedOn).isNotNull();
    }

    @Test
    public void updatesModifiedOnInstants() {

        // given
        Point point = PointsFixture.example();
        repository.save(point);
        Instant modifiedOn1 = repository.findByName(point.getName()).map(Point::getModifiedOn).orElse(null);

        // when
        point.setOpeningHours(openAtWorkWeek());
        repository.save(point);
        Instant modifiedOn2 = repository.findByName(point.getName()).map(Point::getModifiedOn).orElse(null);

        // then
        assertThat(modifiedOn1).isBefore(modifiedOn2);
    }

    @Test
    public void savesLocation() {

        // given
        Point point = PointsFixture.example()
                .setLocation(PointsFixture.Locations.rooseveltlaanInGent());

        // when
        repository.save(point);
        Point saved = repository.findByName(point.getName()).orElse(null);

        // then
        assertThat(saved).isNotNull()
                .extracting(Point::getLocation)
                .isEqualTo(
                        PointsFixture.Locations.rooseveltlaanInGent()
                );
    }

    @Test
    public void savesOpeningHoursAsJson() {

        // given
        Point point = PointsFixture.example()
                .setOpeningHours(openAtWorkWeek());

        // when
        repository.save(point);
        Point saved = repository.findByName(point.getName()).orElse(null);

        // then
        assertThat(saved).isNotNull()
                .extracting(Point::getOpeningHours)
                .isEqualTo(openAtWorkWeek());
    }
}
