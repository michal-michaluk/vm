package micro.client;

import micro.points.location.PointLocation;

@lombok.Value
public
class LocationUpdateRequest {
    PointLocation location;

    public static LocationUpdateRequest from(PointLocation location) {
        return new LocationUpdateRequest(location);
    }
}
