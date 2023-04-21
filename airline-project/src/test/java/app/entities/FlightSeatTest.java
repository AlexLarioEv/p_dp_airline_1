package app.entities;

import app.dto.FlightSeatDTO;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

public class FlightSeatTest extends EntityTest {

    private Validator validator;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        mapper = new ObjectMapper();
    }

    private JSONObject initJSONObject() {
        JSONObject flightSeatJson = new JSONObject();

        flightSeatJson.put("id", 1L);
        flightSeatJson.put("fare", 1500);
        flightSeatJson.put("isRegistered", true);
        flightSeatJson.put("isSold", true);
        flightSeatJson.put("isBooking", true);
        flightSeatJson.put("flightId", 1);
        flightSeatJson.put("seatId", 1);

        return flightSeatJson;
    }


    @Test
    public void validFlightSeatShouldValidate() {
        FlightSeatDTO testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();

        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }

    @Test
    public void negativeFareShouldNotValidate() {
        FlightSeatDTO testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("fare", -100);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }

    @Test
    public void nullIsRegisteredShouldNotValidate() {
        FlightSeatDTO testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("isRegistered", null);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }
    @Test
    public void nullIsSoldShouldNotValidate() {
        FlightSeatDTO testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("isSold", null);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }

    @Test
    public void nullFlightShouldNotValidate() {
        FlightSeatDTO testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("flightId", null);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }

    @Test
    public void nullSeatShouldNotValidate() {
        FlightSeatDTO testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("seatId", null);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }
}
