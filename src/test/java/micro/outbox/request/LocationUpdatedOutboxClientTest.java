package micro.outbox.request;

import micro.IntegrationTest;
import micro.client.ClientFixture;
import micro.points.PointsFixture;
import micro.points.location.PointLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseActions;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@IntegrationTest
@TestPropertySource(properties = {
        "location-client.url=http://location-client"
})
class LocationUpdatedOutboxClientTest {

    @Value("${location-client.url}")
    String url;
    @Autowired
    LocationUpdatedOutboxClient client;
    @Autowired
    OutboxRequestRepository repository;
    @Autowired
    RestTemplate restTemplate;
    MockRestServiceServer server;

    final UUID givenId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    @Transactional
    void sendsLocationUpdateAfterCommit() {
        expectRequest()
                .andExpect(content().json(expectedJson()))
                .andRespond(withSuccess());

        client.sendLocationUpdate(givenId, givenLocation());
        commit();

        server.verify();
    }

    @Test
    @Transactional
    void doNotSendsLocationUpdateAfterRollback() {
        expectNoRequest();

        client.sendLocationUpdate(givenId, givenLocation());
        rollback();

        server.verify();
    }

    private void expectNoRequest() {
        // intentionally left blank
    }

    private ResponseActions expectRequest() {
        return server.expect(requestTo(url + "/locations/" + givenId));
    }

    private void commit() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

    private void rollback() {
        TestTransaction.flagForRollback();
        TestTransaction.end();
    }

    private PointLocation givenLocation() {
        return PointsFixture.PointLocations.dusartstraatInAmsterdam();
    }

    private String expectedJson() {
        return ClientFixture.LocationUpdate.requestDusartstraat();
    }
}
