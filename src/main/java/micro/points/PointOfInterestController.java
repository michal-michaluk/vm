package micro.points;

import lombok.RequiredArgsConstructor;
import micro.points.PointOfInterestService.PointUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
class PointOfInterestController {

    private final PointOfInterestService service;

    @GetMapping(value = "/poi", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Point> get(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping(value = "/poi/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Point get(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PatchMapping(value = "/poi/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Point patch(@PathVariable UUID id,
                       @RequestBody @Valid PointUpdate update) {
        return service.update(id, update);
    }
}
