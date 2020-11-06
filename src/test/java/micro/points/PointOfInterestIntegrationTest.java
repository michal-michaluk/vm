package micro.points;

import micro.IntegrationTest;
import micro.client.MockedLocationUpdatedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static micro.points.PoiResponseAssert.assertThat;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@IntegrationTest
class PointOfInterestIntegrationTest {

    @Autowired
    PointOfInterestRequestFixture request;

    @Autowired
    PointRepository repository;

    @Autowired
    MockedLocationUpdatedService client;

    @Test
    void getPointWithoutLocation() {
        //given
        Point point = PointsFixture.example()
                .setLocation(null);
        repository.save(point);

        //when
        ResponseEntity<String> response = request.get(point.getId());

        //then
        assertThat(response).isOK()
                .hasPointAsBody(actual -> actual
                        .hasId(point.getId())
                        .hasNoLocation()
                );
    }

    @Test
    void getPointWithLocation() {
        //given
        Point point = PointsFixture.example();
        repository.save(point);

        //when
        ResponseEntity<String> response = request.get(point.getId());

        //then
        assertThat(response).isOK()
                .hasPointAsBody(actual -> actual
                        .hasId(point.getId())
                        .hasLocation(PointsFixture.Locations.rooseveltlaanInGent())
                );
    }

    @Test
    void patchPointLocation() {
        //given
        Point point = PointsFixture.example().setLocation(null);
        repository.save(point);
        var location = PointsFixture.Locations.rooseveltlaanInGent();

        //and
        client.expectLocationUpdatedRequest(point.getId())
                .andRespond(withSuccess());

        //when
        ResponseEntity<String> response = request.patch(point.getId(), PointOfInterestService.PointUpdate.location(location));

        //then
        assertThat(response).isOK()
                .hasPointAsBody(actual -> actual
                        .hasId(point.getId())
                        .hasLocation(PointsFixture.Locations.rooseveltlaanInGent())
                );
    }

    @Test
    void patchPointWithInvalidLocation() {
        //given
        Point point = PointsFixture.example().setLocation(null);
        repository.save(point);
        var location = PointsFixture.Locations.rooseveltlaanInGent()
                .setCity(null)
                .setCoordinates(null);

        //when
        ResponseEntity<String> response = request.patch(point.getId(), PointOfInterestService.PointUpdate.location(location));

        //then
        assertThat(response)
                .isBadRequest();
    }
}
