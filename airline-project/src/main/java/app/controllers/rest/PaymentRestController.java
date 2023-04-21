package app.controllers.rest;

import app.entities.Payment;
import app.services.interfaces.PaymentService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@Api(tags = "Payment REST")
@Tag(name = "Payment REST", description = "API для операций с оплатой бронирований")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentRestController {

    private final PaymentService paymentService;

    @PostMapping
    @ApiOperation(value = "Create new Payment")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "payment created"),
            @ApiResponse(code = 400, message = "payment not created")
    })
    public ResponseEntity<?> savePayment(
            @ApiParam(
                    name = "payment",
                    value = "Payment model"
            )
            @RequestBody @Valid Payment payment) {

        try {
            Payment savedPayment = paymentService.savePayment(payment);
            log.info("savePayment: new payment saved with id= {}", savedPayment.getId());
            return new ResponseEntity<Payment>(savedPayment, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            log.error("savePayment: some bookings not exist");
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Get Payment by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "payment found"),
            @ApiResponse(code = 404, message = "payment not found")
    })
    public ResponseEntity<Payment> getSeatById(
            @ApiParam(
                    name = "id",
                    value = "Payment.id"
            )
            @PathVariable("id") long id) {

        Payment payment = paymentService.findPaymentById(id);

        if (payment != null) {
            log.info("getPaymentById: find payment with id = {}", id);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } else {
            log.info("getPaymentById: not find payment with id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping
    @ApiOperation(value = "Get list of all Payments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "payments found"),
            @ApiResponse(code = 404, message = "payments not found")
    })
    public ResponseEntity<List<Payment>> getListOfAllPayments() {
        List<Payment> payments = paymentService.findAllPayments();
        if (payments == null) {
            log.info("getListOfAllPayments: not found any payments");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("getListOfAllPayments: found {} payments", payments.size());
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

}
