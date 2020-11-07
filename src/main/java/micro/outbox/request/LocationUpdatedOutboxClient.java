package micro.outbox.request;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.extern.slf4j.Slf4j;
import micro.client.LocationUpdateRequest;
import micro.points.location.PointLocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
class LocationUpdatedOutboxClient {

    private final String url;
    private final Clock clock;
    private final RestTemplate restTemplate;
    private final OutboxRequestRepository repository;

    LocationUpdatedOutboxClient(
            @Value("${location-client.url}") String url,
            Clock clock,
            RestTemplate restTemplate,
            OutboxRequestRepository repository) {
        this.url = url;
        this.clock = clock;
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    public void sendLocationUpdate(UUID id, PointLocation location) {
        // skonstruuj instancję OutboxRequest z niezbędnymi informacjami dotyczącymi żądania
        // z parametrami: Instant.now(clock), id, LocationUpdateRequest.from(location)
        // zapisz żądanie w repozytorium
        // po pozytywnym zakończeniu transakcji:
        // - prześlij żądanie
        // - usuń zapisane żądanie z repozytorium
    }

    private void executeRequest(UUID id, LocationUpdateRequest location) {
        HttpEntity<LocationUpdateRequest> payload = payload(location);
        Retry.of("location-update-outbox", RetryConfig.custom()
                .retryExceptions(ResourceAccessException.class)
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
