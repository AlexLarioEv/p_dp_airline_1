package app.entities;

import app.dto.DestinationDTO;
import app.enums.Airport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

public class DestinationTest extends EntityTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void lessThan3CharAirportNameSizeShouldNotValidate() {
        Destination testDestination = new Destination(1L, Airport.AER, "Ad", "Adler", "UTC + 3", "RUSSIA");
        DestinationDTO testDestinationDTO = new DestinationDTO(testDestination);

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testDestinationDTO));
    }

    @Test
    public void between3And15CharAirportNameSizeShouldValidate() {
        Destination testDestination = new Destination(1L, Airport.AER, "Adler", "Adler", "UTC + 3", "RUSSIA");
        DestinationDTO testDestinationDTO = new DestinationDTO(testDestination);

        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testDestinationDTO));
    }

    @Test
    public void moreThan15CharAirportNameSizeShouldNotValidate() {
        Destination testDestination = new Destination(1L, Airport.AER, "AdlerAdlerAdlerAdler", "Adler", "UTC + 3", "RUSSIA");
        DestinationDTO testDestinationDTO = new DestinationDTO(testDestination);

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testDestinationDTO));
    }

    @Test
    public void lessThan3CharCityNameSizeShouldNotValidate() {
        Destination testDestination = new Destination(1L, Airport.AER, "Adler", "A", "UTC + 3", "RUSSIA");
        DestinationDTO testDestinationDTO = new DestinationDTO(testDestination);

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testDestinationDTO));
    }

    @Test
    public void between3And15CharCityNameSizeShouldValidate() {
        Destination testDestination = new Destination(1L, Airport.AER, "Adler", "Adler", "UTC + 3", "RUSSIA");
        DestinationDTO testDestinationDTO = new DestinationDTO(testDestination);

        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testDestinationDTO));
    }

    @Test
    public void moreThan15CharCityNameSizeShouldNotValidate() {
        Destination testDestination = new Destination(1L, Airport.AER, "Adler", "GOROD_SKAZKA_GOROD_MECHTA_POPADAESH_V_EGO_SETI", "UTC + 3", "RUSSIA");
        DestinationDTO testDestinationDTO = new DestinationDTO(testDestination);

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testDestinationDTO));
    }

    @Test
    public void lessThan3CharCountryNameSizeShouldNotValidate() {
        Destination testDestination = new Destination(1L, Airport.AER, "Adler", "Adler", "UTC + 3", "RU");
        DestinationDTO testDestinationDTO = new DestinationDTO(testDestination);

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testDestinationDTO));
    }

    @Test
    public void between3And30CharCountryNameSizeShouldValidate() {
        Destination testDestination = new Destination(1L, Airport.AER, "Adler", "Adler", "UTC + 3", "RUSSIA");
        DestinationDTO testDestinationDTO = new DestinationDTO(testDestination);

        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testDestinationDTO));
    }

    @Test
    public void moreThan30CharCountryNameSizeShouldNotValidate() {
        Destination testDestination = new Destination(1L, Airport.AER, "Adler", "Adler", "UTC + 3",
                                            "RUSSIASHA_RUSSIASHA_RUSSIASHA_RUSSIASHA");
        DestinationDTO testDestinationDTO = new DestinationDTO(testDestination);

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testDestinationDTO));
    }

}
