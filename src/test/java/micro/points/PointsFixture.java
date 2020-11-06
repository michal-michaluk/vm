package micro.points;

import micro.points.location.GeoLocation;
import micro.points.location.Location;
import micro.points.location.PointLocation;
import micro.points.opening.OpeningHours;

import java.util.UUID;

public class PointsFixture {

    public static Point example() {
        return example("Name of point " + UUID.randomUUID());
    }

    public static Point example(String name) {
        return new Point()
                .setName(name)
                .setLocation(Locations.rooseveltlaanInGent())
                .setOpeningHours(Opening.alwaysOpen());
    }

    public static class PointLocations {
        public static PointLocation rooseveltlaanInGent() {
            return PointLocation.from(Locations.rooseveltlaanInGent());
        }

        public static PointLocation dusartstraatInAmsterdam() {
            return PointLocation.from(Locations.dusartstraatInAmsterdam());
        }
    }

    public static class Locations {
        public static Location rooseveltlaanInGent() {
            return new Location()
                    .setStreet("F.Rooseveltlaan")
                    .setHouseNumber("3A")
                    .setCity("Gent")
                    .setPostalCode("9000")
                    .setCountryISO("BEL")
                    .setCoordinates(new GeoLocation()
                            .setLatitude("51.047599")
                            .setLongitude("3.729944")
                    );
        }

        public static Location dusartstraatInAmsterdam() {
            return new Location()
                    .setStreet("Dusartstraat")
                    .setHouseNumber("3")
                    .setCity("Amsterdam")
                    .setPostalCode("1072HS")
                    .setCountryISO("NLD")
                    .setCoordinates(new GeoLocation()
                            .setLatitude("52.352206")
                            .setLongitude("4.809561")
                    );
        }
    }

    public static class Opening {
        public static OpeningHours alwaysOpen() {
            return OpeningHours.alwaysOpen();
        }

        public static OpeningHours openAtWorkWeek() {
            return OpeningHours.openAt(
                    OpeningHours.OpeningTime.opened(8, 17),
                    OpeningHours.OpeningTime.opened(8, 17),
                    OpeningHours.OpeningTime.opened(8, 17),
                    OpeningHours.OpeningTime.opened(8, 17),
                    OpeningHours.OpeningTime.opened(8, 17),
                    OpeningHours.OpeningTime.closed(),
                    OpeningHours.OpeningTime.closed()
            );
        }
    }
}