package micro.points.location;

import lombok.Value;
import micro.DomainEvent;

import java.util.UUID;

@Value
public class LocationUpdated implements DomainEvent {
    UUID pointId;
    PointLocation location;
}
