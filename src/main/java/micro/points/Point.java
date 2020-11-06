package micro.points;

import lombok.Data;
import lombok.experimental.Accessors;
import micro.DomainEvent;
import micro.points.location.Location;
import micro.points.location.LocationUpdated;
import micro.points.location.PointLocation;
import micro.points.opening.OpeningHours;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
public class Point {

    @Id
    @GeneratedValue
    private UUID id;
    @NotNull
    @Column(unique = true)
    private String name;
    @CreationTimestamp
    private Instant addedOn;
    @UpdateTimestamp
    private Instant modifiedOn;

    @Valid
    @Embedded
    private Location location;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private OpeningHours openingHours;

    private transient Collection<DomainEvent> events = new ArrayList<>();

    public void updateLocation(Location location) {
        if (!Objects.equals(this.location, location)) {
            setLocation(location);
            events.add(new LocationUpdated(id,
                    PointLocation.from(location)
            ));
        }
    }

    public void updateOpening(OpeningHours opening) {
        setOpeningHours(opening);
    }

    public OpeningHours getOpeningHours() {
        return OpeningHours.alwaysOpenOrGiven(openingHours);
    }

    @DomainEvents
    public Collection<DomainEvent> events() {
        return events;
    }

    @AfterDomainEventPublication
    public void clearEvents() {
        events.clear();
    }
}
