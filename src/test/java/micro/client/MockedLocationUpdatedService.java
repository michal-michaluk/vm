package micro.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseActions;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@Component
@ConditionalOnProperty(
        name = "location-client.mock",
        havingValue = "true",
        matchIfMissing = true)
public class MockedLocationUpdatedService {

    @Value("${location-client.url}")
    String url;

    private final MockRestServiceServer server;

    public MockedLocationUpdatedService(RestTemplate restTemplate) {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    public ResponseActions expectLocationUpdatedRequest(UUID id) {
        return server.expect(requestTo(url + "/locations/" + id));
    }
}
