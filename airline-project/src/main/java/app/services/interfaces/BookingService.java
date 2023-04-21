package app.services.interfaces;

import app.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    Booking save(Booking book);

    Page<Booking> findAll(Pageable pageable);

    Booking findById(Long id);

    List<Booking> getAllBooksForEmailNotification(LocalDateTime departureIn, LocalDateTime gap);

    void deleteById(Long id);

    Booking findByBookingNumber(String number);
}
