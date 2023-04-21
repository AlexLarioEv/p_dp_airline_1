package app.entities.account;
import app.dto.account.PassengerDTO;
import app.entities.EntityTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class PassengerTest extends EntityTest {
    private Validator validator;
    private PassengerDTO passenger;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        passenger = new PassengerDTO();
    }

    private JSONObject initJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("@type", "passenger");

        jsonObject.put("id", 1004L);
        jsonObject.put("email", "passenger@mail.ru");
        jsonObject.put("password", "1Passenger@Passenger");
        jsonObject.put("securityQuestion", "someQuestion");
        jsonObject.put("answerQuestion", "someAnswer");
        jsonObject.put("roles", null);

        jsonObject.put("firstName", "Alexandr");
        jsonObject.put("lastName", "Bagin");
        jsonObject.put("birthDate", "1998-01-08");
        jsonObject.put("phoneNumber", "89995252503");
        jsonObject.put("passport", null);
        return jsonObject;
    }

    @Test
    public void testRightPassenger() {
        try {
            passenger = mapper.readValue(initJSONObject().toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void blankFirstNameShouldNotValidate() {
        JSONObject jsonObject = initJSONObject();
        jsonObject.replace("firstName", "");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void smallFirstNameShouldNotValidate() {
        JSONObject jsonObject = initJSONObject();
        jsonObject.replace("firstName", "a");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void blankLastNameShouldNotValidate() {
        JSONObject jsonObject = initJSONObject();
        jsonObject.replace("lastName", "");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void smallLastNameShouldNotValidate() {
        JSONObject jsonObject = initJSONObject();
        jsonObject.replace("lastName", "a");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void nullBirthDateShouldNotValidate() {
        JSONObject jsonObject = initJSONObject();
        jsonObject.replace("birthDate", null);
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void pastBirthDateShouldNotValidate() {
        JSONObject jsonObject = initJSONObject();
        jsonObject.replace("birthDate", "2054-02-07");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void blankPhoneNumberShouldNotValidate() {
        JSONObject jsonObject = initJSONObject();
        jsonObject.replace("phoneNumber", "");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void smallPhoneNumberShouldNotValidate() {
        JSONObject jsonObject = initJSONObject();
        jsonObject.replace("phoneNumber", "4456");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void longFirstNameShouldNotValidate() {
        JSONObject jsonObject = initJSONObject();
        jsonObject.replace("firstName", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void longLastNameShouldNotValidate() {
        JSONObject jsonObject = initJSONObject();
        jsonObject.replace("lastName", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void longPhoneNumberShouldNotValidate() {
        JSONObject jsonObject = initJSONObject();
        jsonObject.replace("phoneNumber", "888888888888888888888888888888888888888888888888888888888888888888" +
                "88888888888888888888888888888888888888888888888888888888888888888");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }
}