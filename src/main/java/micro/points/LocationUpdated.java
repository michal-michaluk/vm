package micro.points;

import lombok.Value;
import micro.points.location.PointLocation;

import java.util.UUID;

@Value
public class LocationUpdated {
    UUID id;
    PointLocation location;
}
