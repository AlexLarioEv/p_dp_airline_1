package app.dto.account;

import app.entities.account.Account;
import app.entities.account.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.mail.MethodNotSupportedException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AdminDTO.class, name = "admin"),
        @JsonSubTypes.Type(value = AirlineManagerDTO.class, name = "manager"),
        @JsonSubTypes.Type(value = PassengerDTO.class, name = "passenger")}
)
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString(exclude = {"roles"})
public class AccountDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Email
    @NotBlank(message = "The field cannot be empty")
    private String email;

    @NotBlank(message = "The field cannot be empty")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,}", message = "min 8 characters, 1 uppercase latter" +
            "1 lowercase latter, at least 1 number, 1 special character")
    private String password;

    @NotBlank(message = "The field cannot be empty")
    private String securityQuestion;

    @NotBlank(message = "The field cannot be empty")
    private String answerQuestion;

    private Set<Role> roles;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.securityQuestion = account.getSecurityQuestion();
        this.answerQuestion = account.getAnswerQuestion();
        this.roles = account.getRoles();
    }

    public Account convertToEntity() throws MethodNotSupportedException {
        throw new MethodNotSupportedException("Account Entity is abstract");
    }

}
