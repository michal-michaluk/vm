package micro.points.location;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class PointLocation {
    String city;
    String houseNumber;
    String street;
    String zipcode;
    String country;
    BigDecimal longitude;
    BigDecimal latitude;

    public static PointLocation from(Location location) {
        return new PointLocation(
                location.getCity(),
                location.getHouseNumber(),
                location.getStreet(),
                location.getPostalCode(),
                location.getCountryISO(),
                new BigDecimal(location.getCoordinates().getLongitude()),
                new BigDecimal(location.getCoordinates().getLatitude())
        );
    }

    public Location toLocation() {
        return new Location()
                .setStreet(street)
                .setHouseNumber(houseNumber)
                .setCity(city)
                .setPostalCode(zipcode)
                .setCountryISO(country)
                .setCoordinates(toGeoLocation());
    }

    GeoLocation toGeoLocation() {
        if (latitude == null || longitude == null) {
            return null;
        }
        return new GeoLocation()
                .setLatitude(latitude.toPlainString())
                .setLongitude(longitude.toPlainString());
    }
}
