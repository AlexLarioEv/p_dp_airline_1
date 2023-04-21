package app.entities;

import app.dto.SeatDTO;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


public class SeatTest extends EntityTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void wrongSeatNumberTest() {
        SeatDTO seat;
        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("seatNumber", "4");
        jsonObject.put("isNearEmergencyExit", false);
        jsonObject.put("isLockedBack", true);

        JSONObject jsonCategory = new JSONObject();
        jsonCategory.put("id", 1);
        jsonCategory.put("categoryType", "FIRST");
        jsonObject.put("category", jsonCategory);

        jsonObject.put("aircraftId", 1);
        String testJSON = jsonObject.toString();

        try {
            seat = mapper.readValue(testJSON, SeatDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, seat));
    }

    @Test
    public void wrongSeatIsNearEmergencyExitTest() {
        SeatDTO seat;
        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("seatNumber", "4A");
        jsonObject.put("isNearEmergencyExit", null);
        jsonObject.put("isLockedBack", true);

        JSONObject jsonCategory = new JSONObject();
        jsonCategory.put("id", 1);
        jsonCategory.put("categoryType", "FIRST");

        jsonObject.put("category", jsonCategory);
        jsonObject.put("aircraftId", 1);
        String testJSON = jsonObject.toString();

        try {
            seat = mapper.readValue(testJSON, SeatDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, seat));
    }

    @Test
    public void wrongSeatIsLockedBackTest() {
        SeatDTO seat;
        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("seatNumber", "4A");
        jsonObject.put("isNearEmergencyExit", false);
        jsonObject.put("isLockedBack", null);

        JSONObject jsonCategory = new JSONObject();
        jsonCategory.put("id", 1);
        jsonCategory.put("categoryType", "FIRST");

        jsonObject.put("category", jsonCategory);

        jsonObject.put("aircraftId", 1);
        String testJSON = jsonObject.toString();

        try {
            seat = mapper.readValue(testJSON, SeatDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, seat));

    }
}
