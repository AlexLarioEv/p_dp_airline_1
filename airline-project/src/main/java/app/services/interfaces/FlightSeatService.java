package app.services.interfaces;

import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.enums.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FlightSeatService {

    Set<FlightSeat> findAll();

    Page<FlightSeat> findAll(Pageable pageable);

    FlightSeat findById(Long id);

    Set<FlightSeat> findByFlightId(Long flightId);
    Page<FlightSeat> findByFlightId(Long flightId, Pageable pageable);
    Set<FlightSeat> findByFlightNumber(String flightNumber);

    Set<FlightSeat> addFlightSeatsByFlightId(Long flightId);

    Set<FlightSeat> addFlightSeatsByFlightNumber(String flightNumber);

    FlightSeat saveFlightSeat(FlightSeat flightSeat);
    int getNumberOfFreeSeatOnFlight(Flight flight);
    Set<Seat> getSetOfFeeSeatOnFlightByFlightId(Long id);
    Set<FlightSeat> findFlightSeatsBySeat(Seat seat);

    void deleteById(Long id);

    Set<FlightSeat> findNotSoldById(Long id);

    List<FlightSeat> findFlightSeatsByFlightIdAndSeatCategory(Long id, CategoryType type);

    List<FlightSeat> findSingleFlightSeatByFlightIdAndSeatCategory(Long id, CategoryType type);

    Page<FlightSeat> findNotSoldById(Long id, Pageable pageable);
}
