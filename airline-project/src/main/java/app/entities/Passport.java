package app.entities;

import app.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Passport {

    @Size(min = 2, max = 128, message = "Size middle_name cannot be less than 2 and more than 128 characters")
    @Column(name = "middle_name")
    private String middleName;

    @ApiModelProperty(dataType = "string",
            value = "Return values \"male\" or \"female\", accepts values see example",
            example = "\"male\", \"MALE\", \"m\", \"M\", \"female\", \"FEMALE\", \"f\", \"F\"")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Pattern(regexp = "\\d{4}\\s\\d{6}", message = "Incorrect passport number format")
    @Column(name = "serial_number_passport")
    private String serialNumberPassport;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth can not be a future time")
    @Column(name = "passport_issuing_date")
    private LocalDate passportIssuingDate;

    @Column(name = "passport_issuing_country")
    private String passportIssuingCountry;
}
