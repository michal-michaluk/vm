package micro.client;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.extern.slf4j.Slf4j;
import micro.points.LocationUpdateClient;
import micro.points.location.PointLocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
class LocationUpdatedClient implements LocationUpdateClient {

    private final String url;
    private final RestTemplate restTemplate;

    LocationUpdatedClient(
            @Value("${location-client.url}") String url,
            RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendLocationUpdate(UUID id, PointLocation location) {
        HttpEntity<LocationUpdateRequest> payload = payload(LocationUpdateRequest.from(location));
        Retry.of("location-update", RetryConfig.custom()
                .retryExceptions(ResourceAccessException.class)
                .maxAttempts(3)
                .intervalFunction(IntervalFunction.ofExponentialBackoff(Duration.ofMillis(100), 10.0))
                .build()
        ).executeRunnable(() ->
                restTemplate.exchange(url + "/locations/{id}",
                        HttpMethod.POST, payload, Void.class,
                        Map.of("id", id)
                )
        );
    }

    private <T> HttpEntity<T> payload(T body) {
        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity<>(body, headers);
    }

}
