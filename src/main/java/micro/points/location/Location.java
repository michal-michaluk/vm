package micro.points.location;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@Embeddable
public class Location {
    @NotBlank
    private String city;
    @NotBlank
    private String street;
    @NotBlank
    private String houseNumber;
    @NotBlank
    private String postalCode;
    private String state;
    @NotBlank
    @Size(min = 2, max = 3)
    private String countryISO;
    @Valid
    @NotNull
    private GeoLocation coordinates;
}
