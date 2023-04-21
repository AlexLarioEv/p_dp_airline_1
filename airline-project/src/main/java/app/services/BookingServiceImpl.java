package app.services;

import app.entities.Booking;
import app.repositories.BookingRepository;
import app.services.interfaces.BookingService;
import app.services.interfaces.CategoryService;
import app.services.interfaces.FlightService;
import app.services.interfaces.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CategoryService categoryService;
    private final PassengerService passengerService;
    private final FlightService flightService;


    @Transactional
    @Override
    public Booking save(Booking booking) {
        booking.setPassenger(passengerService.findByEmail(booking.getPassenger().getEmail()));
        booking.setFlight(flightService.getFlightByCode(booking.getFlight().getCode()));
        booking.setCategory(categoryService.findByCategoryType(booking.getCategory().getCategoryType()));

        return bookingRepository.save(booking);
    }

    @Override
    public Page<Booking> findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @Override
    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<Booking> getAllBooksForEmailNotification(LocalDateTime departureIn, LocalDateTime gap) {
        return bookingRepository.getAllBooksForEmailNotification(departureIn, gap);
    }

    @Override
    public Booking findByBookingNumber(String number) {
        return bookingRepository.findByBookingNumber(number).orElse(null);
    }
}
