package app.dto;

import app.enums.State;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PaymentDto {
    private Long id;

    @NotEmpty
    private List<Long> bookingsId = new ArrayList<>();

    private State paymentState;
}
