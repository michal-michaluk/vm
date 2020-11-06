package micro.points;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import micro.points.location.Location;
import micro.points.opening.OpeningHours;
import org.assertj.core.api.Assertions;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@AllArgsConstructor
public class PointJsonAssert {
    private final Object response;

    public static PointJsonAssert assertThat(Object response) {
        return new PointJsonAssert(response);
    }

    private String getId() {
        return JsonPath.read(response, "$.id");
    }

    public PointJsonAssert hasId(UUID expected) {
        Assertions.assertThat(JsonPath.<String>read(response, "$.id"))
                .as("$.id of point %s", getId())
                .isEqualTo(expected.toString());
        return this;
    }

    public PointJsonAssert hasNoLocation() {
        Assertions.assertThat(JsonPath.<Object>read(response, "$.location")).isNull();
        return this;
    }

    public PointJsonAssert hasLocation(Location location) {
        Assertions.assertThat(JsonPath.<Object>read(response, "$.location"))
                .as("$.location of point %s", getId())
                .isNotNull();
        Assertions.assertThat(JsonPath.<String>read(response, "$.location.city"))
                .as("$.location.city of point %s", getId())
                .isEqualTo(location.getCity());
        Assertions.assertThat(JsonPath.<String>read(response, "$.location.street"))
                .as("$.location.street of point %s", getId())
                .isEqualTo(location.getStreet());
        Assertions.assertThat(JsonPath.<String>read(response, "$.location.postalCode"))
                .as("$.location.postalCode of point %s", getId())
                .isEqualTo(location.getPostalCode());
        Assertions.assertThat(JsonPath.<String>read(response, "$.location.state"))
                .as("$.location.state of point %s", getId())
                .isEqualTo(location.getState());
        Assertions.assertThat(JsonPath.<String>read(response, "$.location.countryISO"))
                .as("$.location.countryISO of point %s", getId())
                .isEqualTo(location.getCountryISO());
        Assertions.assertThat(JsonPath.<Object>read(response, "$.location.coordinates")).isNotNull();
        Assertions.assertThat(JsonPath.<String>read(response, "$.location.coordinates.latitude"))
                .as("$.location.coordinates.latitude of point %s", getId())
                .isEqualTo(location.getCoordinates().getLatitude());
        Assertions.assertThat(JsonPath.<String>read(response, "$.location.coordinates.longitude"))
                .as("$.location.coordinates.longitude of point %s", getId())
                .isEqualTo(location.getCoordinates().getLongitude());
        return this;
    }

    public PointJsonAssert hasOpeningHours(OpeningHours openingHours) {
        Assertions.assertThat(JsonPath.<Object>read(response, "$.openingHours"))
                .as("$.openingHours of point %s", getId())
                .isNotNull();
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.alwaysOpen"))
                .as("$.openingHours.alwaysOpen of point %s", getId())
                .isEqualTo(openingHours.isAlwaysOpen());

        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.monday.open24h"))
                .as("$.openingHours.opened.monday.open24h of point %s", getId())
                .isEqualTo(openingHours.getOpened().getMonday().isOpen24h());
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.tuesday.open24h"))
                .as("$.openingHours.opened.tuesday.open24h of point %s", getId())
                .isEqualTo(openingHours.getOpened().getTuesday().isOpen24h());
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.wednesday.open24h"))
                .as("$.openingHours.opened.wednesday.open24h of point %s", getId())
                .isEqualTo(openingHours.getOpened().getWednesday().isOpen24h());
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.thursday.open24h"))
                .as("$.openingHours.opened.thursday.open24h of point %s", getId())
                .isEqualTo(openingHours.getOpened().getThursday().isOpen24h());
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.friday.open24h"))
                .as("$.openingHours.opened.friday.open24h of point %s", getId())
                .isEqualTo(openingHours.getOpened().getFriday().isOpen24h());
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.saturday.open24h"))
                .as("$.openingHours.opened.saturday.open24h of point %s", getId())
                .isEqualTo(openingHours.getOpened().getSaturday().isOpen24h());
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.sunday.open24h"))
                .as("$.openingHours.opened.sunday.open24h of point %s", getId())
                .isEqualTo(openingHours.getOpened().getSunday().isOpen24h());

        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.monday.closed"))
                .as("$.openingHours.opened.monday.closed of point %s", getId())
                .isEqualTo(openingHours.getOpened().getMonday().isClosed());
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.tuesday.closed"))
                .as("$.openingHours.opened.tuesday.closed of point %s", getId())
                .isEqualTo(openingHours.getOpened().getTuesday().isClosed());
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.wednesday.closed"))
                .as("$.openingHours.opened.wednesday.closed of point %s", getId())
                .isEqualTo(openingHours.getOpened().getWednesday().isClosed());
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.thursday.closed"))
                .as("$.openingHours.opened.thursday.closed of point %s", getId())
                .isEqualTo(openingHours.getOpened().getThursday().isClosed());
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.friday.closed"))
                .as("$.openingHours.opened.friday.closed of point %s", getId())
                .isEqualTo(openingHours.getOpened().getFriday().isClosed());
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.saturday.closed"))
                .as("$.openingHours.opened.saturday.closed of point %s", getId())
                .isEqualTo(openingHours.getOpened().getSaturday().isClosed());
        Assertions.assertThat(JsonPath.<Boolean>read(response, "$.openingHours.opened.sunday.closed"))
                .as("$.openingHours.opened.sunday.closed of point %s", getId())
                .isEqualTo(openingHours.getOpened().getSunday().isClosed());

        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.monday.open"))
                .as("$.openingHours.opened.monday.open of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getMonday().getOpen()));
        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.tuesday.open"))
                .as("$.openingHours.opened.tuesday.open of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getTuesday().getOpen()));
        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.wednesday.open"))
                .as("$.openingHours.opened.wednesday.open of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getWednesday().getOpen()));
        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.thursday.open"))
                .as("$.openingHours.opened.thursday.open of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getThursday().getOpen()));
        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.friday.open"))
                .as("$.openingHours.opened.friday.open of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getFriday().getOpen()));
        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.saturday.open"))
                .as("$.openingHours.opened.saturday.open of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getSaturday().getOpen()));
        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.sunday.open"))
                .as("$.openingHours.opened.sunday.open of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getSunday().getOpen()));

        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.monday.close"))
                .as("$.openingHours.opened.monday.close of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getMonday().getClose()));
        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.tuesday.close"))
                .as("$.openingHours.opened.tuesday.close of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getTuesday().getClose()));
        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.wednesday.close"))
                .as("$.openingHours.opened.wednesday.close of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getWednesday().getClose()));
        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.thursday.close"))
                .as("$.openingHours.opened.thursday.close of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getThursday().getClose()));
        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.friday.close"))
                .as("$.openingHours.opened.friday.close of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getFriday().getClose()));
        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.saturday.close"))
                .as("$.openingHours.opened.saturday.close of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getSaturday().getClose()));
        Assertions.assertThat(JsonPath.<String>read(response, "$.openingHours.opened.sunday.close"))
                .as("$.openingHours.opened.sunday.close of point %s", getId())
                .isEqualTo(asString(openingHours.getOpened().getSunday().getClose()));

        return this;
    }

    private Object asString(LocalTime time) {
        return time == null ? null : time.format(DateTimeFormatter.ISO_TIME);
    }
}
