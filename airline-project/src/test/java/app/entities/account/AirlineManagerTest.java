package app.entities.account;

import app.dto.account.AirlineManagerDTO;
import app.entities.EntityTest;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

public class AirlineManagerTest extends EntityTest {
    private Validator validator;
    private ObjectMapper mapper;
    private AirlineManagerDTO airlineManager;
    private JSONObject airlineManagerJsonObject;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        mapper = new ObjectMapper();
        airlineManager = null;
        airlineManagerJsonObject = initValidableJSONObject();
    }

    private JSONObject initValidableJSONObject() {
        JSONObject validableAirlineManagerJson = new JSONObject();

        validableAirlineManagerJson.put("@type", "manager");
        validableAirlineManagerJson.put("id", 1002L);
        validableAirlineManagerJson.put("email", "manager@mail.ru");
        validableAirlineManagerJson.put("password", "1@Password");
        validableAirlineManagerJson.put("securityQuestion", "securityQuestion");
        validableAirlineManagerJson.put("answerQuestion", "answerQuestion");
        validableAirlineManagerJson.put("roles", null);

        return validableAirlineManagerJson;
    }

    @Test
    public void validAirlineManagerShouldValidate() {
        try {
            airlineManager = mapper.readValue(airlineManagerJsonObject.toString(), AirlineManagerDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, airlineManager));
    }

    @Test
    public void blankEmailShouldNotValidate() {
        airlineManagerJsonObject.replace("email", "");

        try {
            airlineManager = mapper.readValue(airlineManagerJsonObject.toString(), AirlineManagerDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, airlineManager));
    }

    @Test
    public void blankPasswordShouldNotValidate() {
        airlineManagerJsonObject.replace("password", "");

        try {
            airlineManager = mapper.readValue(airlineManagerJsonObject.toString(), AirlineManagerDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, airlineManager));
    }

    @Test
    public void passwordUnder8CharShouldNotValidate() {
        airlineManagerJsonObject.replace("password", "1@Passw");

        try {
            airlineManager = mapper.readValue(airlineManagerJsonObject.toString(), AirlineManagerDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, airlineManager));
    }

    @Test
    public void passwordWithoutUpperCaseCharShouldNotValidate() {
        airlineManagerJsonObject.replace("password", "1@password");

        try {
            airlineManager = mapper.readValue(airlineManagerJsonObject.toString(), AirlineManagerDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, airlineManager));
    }

    @Test
    public void passwordWithoutLowerCharShouldNotValidate() {
        airlineManagerJsonObject.replace("password", "1@PASSWORD");

        try {
            airlineManager = mapper.readValue(airlineManagerJsonObject.toString(), AirlineManagerDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, airlineManager));
    }

    @Test
    public void passwordWithoutNumberShouldNotValidate() {
        airlineManagerJsonObject.replace("password", "@Password");

        try {
            airlineManager = mapper.readValue(airlineManagerJsonObject.toString(), AirlineManagerDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, airlineManager));
    }

    @Test
    public void passwordWithoutSpecialCharShouldNotValidate() {
        airlineManagerJsonObject.replace("password", "1Password");

        try {
            airlineManager = mapper.readValue(airlineManagerJsonObject.toString(), AirlineManagerDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, airlineManager));
    }

    @Test
    public void blankQuestionShouldNotValidate() {
        airlineManagerJsonObject.replace("securityQuestion", "");

        try {
            airlineManager = mapper.readValue(airlineManagerJsonObject.toString(), AirlineManagerDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, airlineManager));
    }

    @Test
    public void blankAnswerShouldNotValidate() {
        airlineManagerJsonObject.replace("answerQuestion", "");

        try {
            airlineManager = mapper.readValue(airlineManagerJsonObject.toString(), AirlineManagerDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, airlineManager));
    }
}