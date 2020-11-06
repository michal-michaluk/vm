package micro.points.location;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Data
@Embeddable
@Accessors(chain = true)
public class GeoLocation {
    @NotBlank
    private String latitude;
    @NotBlank
    private String longitude;
}
