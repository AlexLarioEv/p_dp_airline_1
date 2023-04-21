package app.services;

import app.entities.Destination;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.repositories.AircraftRepository;
import app.repositories.DestinationRepository;
import app.enums.Airport;
import app.repositories.FlightRepository;
import app.repositories.FlightSeatRepository;
import app.services.interfaces.FlightService;
import app.util.aop.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final AircraftRepository aircraftRepository;
    private final DestinationRepository destinationRepository;

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Page<Flight> getAllFlights(Pageable pageable) {
        return flightRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Set<FlightSeat> getFreeSeats(Long id) {
        var flight = getById(id);
        if (flight == null) {
            return null;
        }
        Set<FlightSeat> flightSeatSet = flightSeatRepository.findFlightSeatByFlight(flight);

        if (flightSeatSet != null) {
            flightSeatSet = flightSeatSet.stream()
                    .filter(flightSeat -> !flightSeat.getIsRegistered())
                    .filter(flightSeat -> !flightSeat.getIsSold())
                    .collect(Collectors.toSet());
        }

        //TODO раскомментировать после добавление сущностей Seat и Aircraft
        //TODO по другому сделал, эта логика тоже нужна, но по моему мнению не здесь

        //var seats = flight.getAircraft().getSeatList()
        //        .stream().filter(seat -> !seat.getIsSold()).collect(Collectors.toList());
        //Map<String, Integer> freeSeats = new HashMap<>();
        //freeSeats.put("всего свободных", seats.size());
        //freeSeats.put("эконом", (int) seats.stream()
        //        .filter(seat -> seat.getCategory().getCategoryType() == CategoryType.ECONOMY).count());
        //freeSeats.put("бизнес", (int) seats.stream()
        //        .filter(seat -> seat.getCategory().getCategoryType() == CategoryType.BUSINESS).count());
        //return freeSeats;
        //return Map.of("NO DATA", 1);

        return flightSeatSet;
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Flight getFlightByCode(String code) {
        return flightRepository.getByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<Flight> getFlightByDestinationsAndDates(String from, String to,
                                                        String start, String finish,
                                                        Pageable pageable) {
        return getAllFlights(pageable).stream()
                .filter(flight -> from == null || flight.getFrom().getCityName().equals(from))
                .filter(flight -> to == null || flight.getTo().getCityName().equals(to))
                .filter(flight -> start == null || flight.getDepartureDateTime().isEqual(LocalDateTime.parse(start)))
                .filter(flight -> finish == null || flight.getArrivalDateTime().isEqual(LocalDateTime.parse(finish)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<Flight> getFlightsByDestinationsAndDepartureDate(Destination from, Destination to,
                                                                 LocalDate departureDate) {
        return flightRepository.getByFromAndToAndDepartureDate(from, to, departureDate);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<Flight> getListDirectFlightsByFromAndToAndDepartureDate(Airport airportCodeFrom, Airport airportCodeTo, Date departureDate) {
        return flightRepository.getListDirectFlightsByFromAndToAndDepartureDate(airportCodeFrom, airportCodeTo, departureDate);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<Flight> getListNonDirectFlightsByFromAndToAndDepartureDate(int airportIdFrom, int airportIdTo, Date departureDate) {
        return flightRepository.getListNonDirectFlightsByFromAndToAndDepartureDate(airportIdFrom, airportIdTo, departureDate);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Flight getFlightByIdAndDates(Long id, String start, String finish) {
        var flight = flightRepository.findById(id);
        if (flight.isPresent()) {
            if (flight.get().getDepartureDateTime().isEqual(LocalDateTime.parse(start))
                    && flight.get().getArrivalDateTime().isEqual(LocalDateTime.parse(finish))) {
                return flight.get();
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Flight getById(Long id) {
        return flightRepository.findById(id).orElse(null);
    }

    @Override
    @Loggable
    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    @Loggable
    public Flight update(Long id, Flight updated) {
        updated.setId(id);
        if (updated.getAircraft() == null) {
            updated.setAircraft(getById(id).getAircraft());
        } else {
            updated.setAircraft(aircraftRepository.findByAircraftNumber(updated.getAircraft().getAircraftNumber()));
        }
        if (updated.getFrom() == null) {
            updated.setFrom(getById(id).getFrom());
        } else {
            updated.setFrom(destinationRepository.findDestinationByAirportCode(updated.getFrom().getAirportCode()).orElse(null));
        }
        if (updated.getTo() == null) {
            updated.setTo(getById(id).getTo());
        } else {
            updated.setTo(destinationRepository.findDestinationByAirportCode(updated.getTo().getAirportCode()).orElse(null));
        }
        return flightRepository.saveAndFlush(updated);
    }

    @Override
    @Loggable
    public void deleteById(Long id) {
        flightRepository.deleteById(id);
    }
}