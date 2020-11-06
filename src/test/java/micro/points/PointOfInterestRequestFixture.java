package micro.points;

import lombok.AllArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class PointOfInterestRequestFixture {

    private final TestRestTemplate testRestTemplate;

    public ResponseEntity<String> get() {
        return testRestTemplate.exchange(
                "/poi/", HttpMethod.GET,
                new HttpEntity<>(jsonHeaders()), String.class);
    }

    public ResponseEntity<String> get(UUID id) {
        return testRestTemplate.exchange(
                "/poi/{poi}", HttpMethod.GET,
                new HttpEntity<>(jsonHeaders()), String.class,
                id);
    }

    public ResponseEntity<String> patch(UUID poi, PointOfInterestService.PointUpdate update) {
        return testRestTemplate.exchange(
                "/poi/{poi}", HttpMethod.PATCH,
                new HttpEntity<>(update, jsonHeaders()), String.class,
                poi
        );
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
