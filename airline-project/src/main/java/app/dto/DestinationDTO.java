package app.dto;

import app.entities.Destination;
import app.enums.Airport;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class DestinationDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private Airport airportCode;

    @NotBlank
    @Size(min = 3, max = 15, message = "Airport name must be between 3 and 15 characters")
    private String airportName;

    @NotBlank
    @Size(min = 3, max = 30, message = "City name must be between 3 and 30 characters")
    private String cityName;

    private String timezone;

    @NotBlank
    @Size(min = 3, max = 30, message = "Country name must be between 3 and 30 characters")
    private String countryName;

    public DestinationDTO(Destination destination) {
        this.id = destination.getId();
        this.airportCode = destination.getAirportCode();
        this.airportName = destination.getAirportName();
        this.cityName = destination.getCityName();
        this.timezone = destination.getTimezone();
        this.countryName = destination.getCountryName();
    }
}
