package micro.client;

import micro.points.LocationUpdateClient;
import micro.points.PointsFixture;
import micro.points.location.PointLocation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseActions;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.web.client.HttpServerErrorException;

import java.net.ConnectException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest(LocationUpdatedClient.class)
@AutoConfigureWebClient(registerRestTemplate = true)
@TestPropertySource(properties = {
        "location-client.url=http://location-client"
})
class LocationUpdateClientTest {

    @Value("${location-client.url}")
    String url;

    @Autowired
    LocationUpdateClient client;

    @Autowired
    MockRestServiceServer server;

    final UUID givenId = UUID.randomUUID();

    @Test
    void successfulRequest() {
        expectRequest()
                .andExpect(content().json(expectedJson()))
                .andRespond(withSuccess());

        client.sendLocationUpdate(givenId, givenLocation());
        server.verify();
    }

    @Test
    void internalServerError() {
        expectRequest()
                .andExpect(content().json(expectedJson()))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThatThrownBy(() -> client.sendLocationUpdate(givenId, givenLocation()))
                .isExactlyInstanceOf(HttpServerErrorException.InternalServerError.class)
                .hasMessageStartingWith("500 Internal Server Error");
        server.verify();
    }


    private ResponseActions expectRequest() {
        return server.expect(requestTo(url + "/locations/" + givenId));
    }

    private ResponseCreator withConnectionIssue() {
        return withException(new ConnectException("Connection reset"));
    }

    private PointLocation givenLocation() {
        return PointsFixture.PointLocations.dusartstraatInAmsterdam();
    }

    private String expectedJson() {
        return ClientFixture.LocationUpdate.requestDusartstraat();
    }
}
