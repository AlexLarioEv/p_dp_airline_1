package app.services;

import app.entities.Booking;
import app.entities.Payment;
import app.enums.State;
import app.repositories.PaymentRepository;
import app.services.interfaces.BookingService;
import app.services.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;

    @Override
    @Transactional
    public Payment savePayment(Payment payment) {
        payment.getBookingsId().stream().forEach(b -> {
            Booking bookingFromDb = bookingService.findById(b);
            if (bookingFromDb == null) {
                throw new NoSuchElementException(String.format("booking with id=%d not exists", b));
            }
        });
        payment.setPaymentState(State.CREATED);
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> findAllPayments(){
        return paymentRepository.findAll();
    }

    @Override
    public Payment findPaymentById(long id){
        return paymentRepository.findById(id).orElse(null);
    }



}
