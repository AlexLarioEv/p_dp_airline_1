package app.services.interfaces;

import app.entities.Payment;
import java.util.List;

public interface PaymentService {

    Payment savePayment(Payment payment);

    List<Payment> findAllPayments();

    Payment findPaymentById(long id);

}
