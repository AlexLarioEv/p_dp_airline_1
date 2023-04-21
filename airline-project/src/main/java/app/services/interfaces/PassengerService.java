package app.services.interfaces;

import app.entities.account.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PassengerService {
    Passenger save(Passenger passenger);

    Passenger update(Passenger passenger);

    Page<Passenger> findAll(Pageable pageable);

    Optional<Passenger> findById(Long id);

    Optional<Passenger> findByPassportSerialNumber(String passportSerialNumber);

    Passenger findByEmail(String email);

    List<Passenger> findByLastName(String lastName);

    List<Passenger> findByFistName(String firstName);

    List<Passenger> findByMiddleName(String middleName);

    List<Passenger> findByAnyName(String name);

    void deleteById(Long id);
}
