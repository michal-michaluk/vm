package micro.outbox.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import micro.client.LocationUpdateRequest;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class OutboxRequest {

    @Id
    private UUID id;
    private Instant occurrenceTime;
    private UUID pointId;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private LocationUpdateRequest payload;

    public OutboxRequest(Instant occurrenceTime, UUID pointId, LocationUpdateRequest request) {
        this.id = UUID.randomUUID();
        this.occurrenceTime = occurrenceTime;
        this.pointId = pointId;
        this.payload = request;
    }
}
