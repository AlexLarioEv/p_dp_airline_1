package app.controllers.rest;

import app.dto.PaymentDto;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Payment REST")
@Tag(name = "Payment REST", description = "API для операций с оплатой (Payment)")
@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentRestController {

    @ApiOperation(value = "Create Payment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Payment added")
    })
    @PostMapping
    public ResponseEntity<PaymentDto> addPayment(@RequestBody @Valid PaymentDto paymentDto) {
        log.info("addPayment: post request");
        return ResponseEntity.ok(paymentDto);
    }
}
