package micro.points;

import micro.points.location.PointLocation;

import java.util.UUID;

public interface LocationUpdateClient {
    void sendLocationUpdate(UUID id, PointLocation location);
}
