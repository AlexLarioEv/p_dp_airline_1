package app.dto.account;


import app.entities.Passport;
import app.entities.account.Passenger;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonTypeName(value = "passenger")
public class PassengerDTO extends AccountDTO {

    @NotBlank(message = "Field should not be empty")
    @Size(min = 2, max = 128, message = "Size first_name cannot be less than 2 and more than 128 characters")
    private String firstName;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 2, max = 128, message = "Size last_name cannot be less than 2 and more than 128 characters")
    private String lastName;

    @NotNull(message = "Field should not be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth can not be a future time")
    private LocalDate birthDate;

    @NotBlank(message = "Field should not be empty")
    @Size(min = 6, max = 64, message = "Size phone cannot be less than 6 and more than 64 characters")
    private String phoneNumber;

    private Passport passport;

    public PassengerDTO(Passenger account) {
        super(account);
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.birthDate = account.getBirthDate();
        this.phoneNumber = account.getPhoneNumber();
        this.passport = account.getPassport();
    }

    @Override
    public Passenger convertToEntity() {
        Passenger passenger = new Passenger();
        passenger.setId(getId());
        passenger.setEmail(getEmail());
        passenger.setPassword(getPassword());
        passenger.setSecurityQuestion(getSecurityQuestion());
        passenger.setAnswerQuestion(getAnswerQuestion());
        passenger.setRoles(getRoles());

        passenger.setFirstName(firstName);
        passenger.setLastName(lastName);
        passenger.setBirthDate(birthDate);
        passenger.setPhoneNumber(phoneNumber);
        passenger.setPassport(passport);
        return passenger;
    }
}
