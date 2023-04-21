package app.entities.account;

import app.dto.account.AdminDTO;
import app.entities.EntityTest;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

public class  AdminTest extends EntityTest {
    private Validator validator;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        mapper = new ObjectMapper();
    }

    private JSONObject initValidableJSONObject() {
        JSONObject validableAdminJson = new JSONObject();

        validableAdminJson.put("@type", "admin");
        validableAdminJson.put("id", 1002L);
        validableAdminJson.put("email", "admin@mail.ru");
        validableAdminJson.put("password", "1Admin@Admin");
        validableAdminJson.put("securityQuestion", "someQuestion");
        validableAdminJson.put("answerQuestion", "someAnswer");
        validableAdminJson.put("roles", null);

        return validableAdminJson;
    }

    @Test
    public void validAdminShouldValidate() {
        AdminDTO testAdmin;
        JSONObject adminJsonObject = initValidableJSONObject();

        try {
            testAdmin = mapper.readValue(adminJsonObject.toString(), AdminDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testAdmin));
    }

    @Test
    public void blankEmailShouldNotValidate() {
        AdminDTO testAdmin;
        JSONObject adminJsonObject = initValidableJSONObject();
        adminJsonObject.replace("email", "");

        try {
            testAdmin = mapper.readValue(adminJsonObject.toString(), AdminDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testAdmin));
    }

    @Test
    public void blankPasswordShouldNotValidate() {
        AdminDTO testAdmin;
        JSONObject adminJsonObject = initValidableJSONObject();
        adminJsonObject.replace("password", "");

        try {
            testAdmin = mapper.readValue(adminJsonObject.toString(), AdminDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testAdmin));
    }

    @Test
    public void passwordUnder8CharShouldNotValidate() {
        AdminDTO testAdmin;
        JSONObject adminJsonObject = initValidableJSONObject();
        adminJsonObject.replace("password", "1Admin@");

        try {
            testAdmin = mapper.readValue(adminJsonObject.toString(), AdminDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testAdmin));
    }

    @Test
    public void passwordWithoutUpperCaseCharShouldNotValidate() {
        AdminDTO testAdmin;
        JSONObject adminJsonObject = initValidableJSONObject();
        adminJsonObject.replace("password", "1admin@admin");

        try {
            testAdmin = mapper.readValue(adminJsonObject.toString(), AdminDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testAdmin));
    }

    @Test
    public void passwordWithoutLowerCharShouldNotValidate() {
        AdminDTO testAdmin;
        JSONObject adminJsonObject = initValidableJSONObject();
        adminJsonObject.replace("password", "ADMIN@ADMIN");

        try {
            testAdmin = mapper.readValue(adminJsonObject.toString(), AdminDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testAdmin));
    }

    @Test
    public void passwordWithoutNumberShouldNotValidate() {
        AdminDTO testAdmin;
        JSONObject adminJsonObject = initValidableJSONObject();
        adminJsonObject.replace("password", "@Password");

        try {
            testAdmin = mapper.readValue(adminJsonObject.toString(), AdminDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testAdmin));
    }

    @Test
    public void passwordWithoutSpecialCharShouldNotValidate() {
        AdminDTO testAdmin;
        JSONObject adminJsonObject = initValidableJSONObject();
        adminJsonObject.replace("password", "1Password");

        try {
            testAdmin = mapper.readValue(adminJsonObject.toString(), AdminDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testAdmin));
    }

    @Test
    public void blankQuestionShouldNotValidate() {
        AdminDTO testAdmin;
        JSONObject adminJsonObject = initValidableJSONObject();
        adminJsonObject.replace("securityQuestion", "");

        try {
            testAdmin = mapper.readValue(adminJsonObject.toString(), AdminDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testAdmin));
    }

    @Test
    public void blankAnswerShouldNotValidate() {
        AdminDTO testAdmin;
        JSONObject adminJsonObject = initValidableJSONObject();
        adminJsonObject.replace("answerQuestion", "");

        try {
            testAdmin = mapper.readValue(adminJsonObject.toString(), AdminDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testAdmin));
    }
}
