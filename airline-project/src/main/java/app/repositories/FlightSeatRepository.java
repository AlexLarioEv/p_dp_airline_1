package app.repositories;

import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.enums.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FlightSeatRepository extends CrudRepository<FlightSeat, Long> {

    Page<FlightSeat> findAll(Pageable pageable);

    Set<FlightSeat> findFlightSeatsByFlightId(Long flightId);
    Page<FlightSeat> findFlightSeatsByFlightId(Long flightId, Pageable pageable);

    Set<FlightSeat> findAllFlightsSeatByFlightIdAndIsSoldFalse(Long flightId);
    Page<FlightSeat> findAllFlightsSeatByFlightIdAndIsSoldFalse(Long flightId, Pageable pageable);

    Set<FlightSeat> findFlightSeatByFlight(Flight flight);

    @Query(value = "select fs2 \n" +
            "from FlightSeat fs2 \n" +
            "join fetch Flight f on f.id = fs2.flight.id \n" +
            "join fetch Seat s on s.id = fs2.seat.id\n" +
            "where f.code = ?1 and s.seatNumber  = ?2")
    Optional<FlightSeat> findFlightSeatByFlightAndSeat(String flightCode, String seatNumber);

    Set<FlightSeat> findFlightSeatsBySeat(Seat seat);

    @Query(value = "SELECT fs from FlightSeat fs WHERE fs.flight.id = ?1 AND fs.seat.category.categoryType = ?2 ORDER BY fs.fare")
    List<FlightSeat> findFlightSeatsByFlightIdAndSeatCategory(Long id, CategoryType type);

}