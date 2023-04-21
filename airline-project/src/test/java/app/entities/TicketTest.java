package app.entities;

import app.dto.TicketDTO;
import app.enums.Airport;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

public class TicketTest extends EntityTest {

    private Validator validator;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    private JSONObject initJsonObject() {

        JSONObject ticketJson = new JSONObject();
        ticketJson.put("id", 1);
        ticketJson.put("ticketNumber", "SD-2222");
        ticketJson.put("passengerId", 4);
        ticketJson.put("firstName", "TestName");
        ticketJson.put("lastName", "TestLastName");
        ticketJson.put("flightId", 1);
        ticketJson.put("code", "XXX");
        ticketJson.put("from", Airport.AER);
        ticketJson.put("to", Airport.AAQ);
        ticketJson.put("departureDateTime", "2023-01-20T17:02:05.003992");
        ticketJson.put("arrivalDateTime", "2023-01-20T17:02:05.003992");
        ticketJson.put("flightSeatId", 1);
        ticketJson.put("seatNumber", "1A");
        return ticketJson;
    }

    @Test
    public void validTicketShouldValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullTicketNumberFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("ticketNumber", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullPassengerIdFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("passengerId", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullPassengerFirstNameFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("firstName", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void shortPassengerFirstNameFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("firstName", "I");
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullPassengerLastNameFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("lastName", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void shortPassengerLastNameFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("lastName", "I");
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullFlightIdFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("flightId", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullCodeFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("code", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void shortCodeFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("code", "1");
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullFromFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("from", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullToFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("to", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullDepartureDateTimeFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("departureDateTime", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullArrivalDateTimeFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("arrivalDateTime", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullFlightSeatIdFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("flightSeatId", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullSeatNumberFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("seatNumber", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void shortSeatNumberFieldShouldNotValidate() {

        TicketDTO ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("seatNumber", "1");
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }
}
