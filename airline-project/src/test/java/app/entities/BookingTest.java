
package app.entities;

import app.dto.BookingDTO;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class BookingTest extends EntityTest {

    private Validator validator;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }


    private JSONObject initJSONObject() {
        JSONObject bookingDtoJSON = new JSONObject();
        bookingDtoJSON.put("id", 10000L);
        bookingDtoJSON.put("bookingNumber", "BK-111111");
        bookingDtoJSON.put("bookingData", "2023-01-20T17:02:05.003992");
        bookingDtoJSON.put("passenger", new JSONObject());
        bookingDtoJSON.put("flightId", 1L);
        bookingDtoJSON.put("categoryType", "BUSINESS");

        return bookingDtoJSON;
    }


    @Test
    public void validBookingShouldValidate() {
        BookingDTO testBooking;
        JSONObject bookingJson = initJSONObject();

        try {
            testBooking = mapper.readValue(bookingJson.toString(), BookingDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testBooking));
    }


    @Test
    public void nullBookingNumberShouldNotValidate() {
        BookingDTO testBooking;
        JSONObject bookingJson = initJSONObject();
        bookingJson.replace("bookingNumber", null);
        try {
            testBooking = mapper.readValue(bookingJson.toString(), BookingDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testBooking));
    }


    @Test
    public void allWhitespaceCharactersBookingNumberShouldNotValidate() {
        BookingDTO testBooking;
        JSONObject bookingJson = initJSONObject();
        bookingJson.replace("bookingNumber", "         ");
        try {
            testBooking = mapper.readValue(bookingJson.toString(), BookingDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testBooking));
    }

    @Test
    public void lessThan9CharBookingNumberShouldNotValidate() {
        BookingDTO testBooking;
        JSONObject bookingJson = initJSONObject();
        bookingJson.replace("bookingNumber", "BK-1");
        try {
            testBooking = mapper.readValue(bookingJson.toString(), BookingDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testBooking));
    }

    @Test
    public void moreThan9CharBookingNumberShouldNotValidate() {
        BookingDTO testBooking;
        JSONObject bookingJson = initJSONObject();
        bookingJson.replace("bookingNumber", "BK-11111122");
        try {
            testBooking = mapper.readValue(bookingJson.toString(), BookingDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testBooking));
    }

    @Test
    public void nullPassengerShouldNotValidate() {
        BookingDTO testBooking;
        JSONObject bookingJson = initJSONObject();
        bookingJson.replace("passenger", null);
        try {
            testBooking = mapper.readValue(bookingJson.toString(), BookingDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testBooking));
    }

    @Test
    public void nullFlightShouldNotValidate() {
        BookingDTO testBooking;
        JSONObject bookingJson = initJSONObject();
        bookingJson.replace("flightId", null);
        try {
            testBooking = mapper.readValue(bookingJson.toString(), BookingDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testBooking));
    }

    @Test
    public void nullCategoryShouldNotValidate() {
        BookingDTO testBooking;
        JSONObject bookingJson = initJSONObject();
        bookingJson.replace("categoryType", null);
        try {
            testBooking = mapper.readValue(bookingJson.toString(), BookingDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testBooking));
    }

}
