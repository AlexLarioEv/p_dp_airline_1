package app.repositories;

import app.entities.account.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Page<Passenger> findAll(Pageable pageable);

    @Query(value = "SELECT p from Passenger p WHERE p.passport.serialNumberPassport = ?1")
    Optional<Passenger> findByPassportSerialNumber(String passportSerialNumber);

    List<Passenger> findByLastName(String lastName);

    List<Passenger> findByFirstName(String firstName);

    @Query(value = "SELECT passengers FROM Passenger passengers WHERE passengers.passport.middleName = :middleName")
    List<Passenger> findByMiddleName(String middleName);

    @Query("SELECT p FROM Passenger p WHERE p.lastName = ?1 OR p.firstName = :name OR p.passport.middleName = :name")
    List<Passenger> findByAnyName(String name);

//    List<Passenger> findAllByFirstNameOrLastNameOrPassport_MiddleName(List<Passenger> passengersNames);

    Passenger findByEmail(String email);
}
