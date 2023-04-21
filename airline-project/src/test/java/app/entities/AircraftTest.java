package app.entities;


import app.dto.AircraftDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

public class AircraftTest extends EntityTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void blankAircraftNumberFieldShouldNotValidate() {
        Aircraft aircraft = new Aircraft();
        Set<Seat> newSeat = new HashSet<>();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("");
        aircraft.setModel("boeing354A");
        aircraft.setModelYear(2054);
        aircraft.setFlightRange(250);
        aircraft.setSeatSet(newSeat);
        AircraftDTO aircraftDTO = new AircraftDTO(aircraft);

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, aircraftDTO));
    }

    @Test
    public void notBlankAircraftNumberFieldShouldValidate() {
        Aircraft aircraft = new Aircraft();
        Set<Seat> newSeat = new HashSet<>();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("boeing354A");
        aircraft.setModelYear(2054);
        aircraft.setFlightRange(250);
        aircraft.setSeatSet(newSeat);
        AircraftDTO aircraftDTO = new AircraftDTO(aircraft);

        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, aircraftDTO));
    }

    @Test
    public void blankModelFieldShouldNotValidate() {
        Aircraft aircraft = new Aircraft();
        Set<Seat> newSeat = new HashSet<>();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("");
        aircraft.setModelYear(2054);
        aircraft.setFlightRange(250);
        aircraft.setSeatSet(newSeat);
        AircraftDTO aircraftDTO = new AircraftDTO(aircraft);

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, aircraftDTO));
    }

    @Test
    public void notBlankModelFieldShouldValidate() {
        Aircraft aircraft = new Aircraft();
        Set<Seat> newSeat = new HashSet<>();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("boeing345");
        aircraft.setModelYear(2054);
        aircraft.setFlightRange(250);
        aircraft.setSeatSet(newSeat);
        AircraftDTO aircraftDTO = new AircraftDTO(aircraft);

        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, aircraftDTO));
    }

    @Test
    public void lessThanMinModelYearFieldShouldNotValidate() {
        Aircraft aircraft = new Aircraft() ;
        Set<Seat> newSeat = new HashSet<>();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("boeing435");
        aircraft.setModelYear(2);
        aircraft.setFlightRange(250);
        aircraft.setSeatSet(newSeat);
        AircraftDTO aircraftDTO = new AircraftDTO(aircraft);

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, aircraftDTO));
    }

    @Test
    public void moreThanMinModelYearFieldShouldNotValidate() {
        Aircraft aircraft = new Aircraft();
        Set<Seat> newSeat = new HashSet<>();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("boeing435");
        aircraft.setModelYear(2005);
        aircraft.setFlightRange(250);
        aircraft.setSeatSet(newSeat);
        AircraftDTO aircraftDTO = new AircraftDTO(aircraft);

        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, aircraftDTO));
    }

    @Test
    public void nullFlightRangeFieldShouldNotValidate() {
        Aircraft aircraft = new Aircraft();
        Set<Seat> newSeat = new HashSet<>();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("boeing435");
        aircraft.setModelYear(2005);
        aircraft.setSeatSet(newSeat);
        AircraftDTO aircraftDTO = new AircraftDTO(aircraft);

        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, aircraftDTO));
    }

    @Test
    public void notNullFlightRangeFieldShouldValidate() {
        Aircraft aircraft = new Aircraft();
        Set<Seat> newSeat = new HashSet<>();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("boeing435");
        aircraft.setModelYear(2005);
        aircraft.setFlightRange(200);
        aircraft.setSeatSet(newSeat);
        AircraftDTO aircraftDTO = new AircraftDTO(aircraft);

        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, aircraftDTO));
    }
}