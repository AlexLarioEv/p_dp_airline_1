package app.services;

import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.enums.CategoryType;
import app.repositories.FlightRepository;
import app.repositories.FlightSeatRepository;
import app.repositories.SeatRepository;
import app.services.interfaces.FlightSeatService;
import app.services.interfaces.FlightService;
import app.util.aop.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightSeatServiceImpl implements FlightSeatService {
    private final FlightSeatRepository flightSeatRepository;
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;
    private final FlightService flightService;

    @Override
    @Loggable
    public Set<FlightSeat> findAll() {
        Set<FlightSeat> flightSeatSet = new HashSet<>();
        flightSeatRepository.findAll().forEach(flightSeatSet::add);
        return flightSeatSet;
    }

    @Override
    public Page<FlightSeat> findAll(Pageable pageable) {
        return flightSeatRepository.findAll(pageable);
    }

    @Override
    public FlightSeat findById(Long id) {
        return flightSeatRepository.findById(id).orElse(null);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Override
    @Loggable
    public Set<FlightSeat> findByFlightId(Long flightId) {
        return flightSeatRepository.findFlightSeatsByFlightId(flightId);
    }

    @Override
    public Page<FlightSeat> findByFlightId(Long flightId, Pageable pageable) {
        return flightSeatRepository.findFlightSeatsByFlightId(flightId, pageable);
    }

    @Override
    public Set<FlightSeat> findByFlightNumber(String flightNumber) {
        Set<FlightSeat> flightSeatSet = new HashSet<>();
        flightSeatRepository.findFlightSeatByFlight(flightRepository.getByCode(flightNumber))
                .forEach(flightSeatSet::add);
        return flightSeatSet;
    }

    @Transactional
    @Override
    @Loggable
    public Set<FlightSeat> addFlightSeatsByFlightId(Long flightId) {
        Set<FlightSeat> newFlightSeats = new HashSet<>();
        Flight flight = flightService.getById(flightId);
        Set<Seat> seats = seatRepository.findByAircraftId(flight.getAircraft().getId());
        for (Seat s : seats) {
            FlightSeat flightSeat = new FlightSeat();
            flightSeat.setSeat(s);
            flightSeat.setFlight(flight);
            flightSeat.setIsBooking(false);
            flightSeat.setIsSold(false);
            flightSeat.setIsRegistered(false);
            flightSeat.setFare(generateFareForFlightseat(s));
            newFlightSeats.add(flightSeat);
        }
        for (FlightSeat f : newFlightSeats) {
            saveFlightSeat(f);
        }
        return newFlightSeats;
    }

    @Override
    @Transactional
    @Loggable
    public Set<FlightSeat> addFlightSeatsByFlightNumber(String flightNumber) {
        Set<FlightSeat> seatsForAdd = new HashSet<>();
        Set<FlightSeat> allFlightSeats = findAll();
        Flight flight = flightRepository.getByCode(flightNumber);
        if (flight != null) {
            Set<Seat> seatsAircraft = flight.getAircraft().getSeatSet();

            for (Seat s : seatsAircraft) {
                FlightSeat flightSeat = new FlightSeat();
                flightSeat.setSeat(s);
                flightSeat.setFlight(flight);
                if (allFlightSeats.contains(flightSeat)) {
                    continue;
                }
                flightSeat.setFare(0);
                flightSeat.setIsSold(false);
                flightSeat.setIsRegistered(false);
                seatsForAdd.add(flightSeat);
            }

            for (FlightSeat f : seatsForAdd) {
                f = flightSeatRepository.save(f);
            }
        }
        return seatsForAdd;
    }

    @Override
    @Transactional
    @Loggable
    public FlightSeat saveFlightSeat(FlightSeat flightSeat) {
        return flightSeatRepository.save(flightSeat);
    }

    @Loggable
    public FlightSeat editFlightSeat(Long id, FlightSeat flightSeat) {
        var targetFlightSeat = flightSeatRepository.findById(id).orElse(null);
        flightSeat.setId(id);

        if (flightSeat.getFare() == null) {
            flightSeat.setFare(targetFlightSeat.getFare());
        }
        if (flightSeat.getIsSold() == null) {
            flightSeat.setIsSold(targetFlightSeat.getIsSold());
        }
        if (flightSeat.getIsBooking() == null) {
            flightSeat.setIsBooking(targetFlightSeat.getIsBooking());
        }
        if (flightSeat.getFlight() == null) {
            flightSeat.setFlight(targetFlightSeat.getFlight());
        }
        if (flightSeat.getSeat() == null) {
            flightSeat.setSeat(targetFlightSeat.getSeat());
        }
        return flightSeatRepository.save(flightSeat);
    }

    @Override
    @Loggable
    public int getNumberOfFreeSeatOnFlight(Flight flight) {
        return flight.getAircraft().getSeatSet().size() - flightSeatRepository.findFlightSeatByFlight(flight).size();
    }

    @Override
    @Loggable
    public Set<Seat> getSetOfFeeSeatOnFlightByFlightId(Long id) {
        var targetFlight = flightRepository.getById(id);
        var setOfSeat = targetFlight.getAircraft().getSeatSet();
        var setOfReservedSeat = flightSeatRepository.findFlightSeatByFlight(targetFlight)
                .stream().map(FlightSeat::getSeat)
                .collect(Collectors.toSet());
        for (Seat s : setOfReservedSeat) {
            setOfSeat.remove(s);
        }
        return setOfSeat;
    }

    @Override
    @Loggable
    public Set<FlightSeat> findFlightSeatsBySeat(Seat seat) {
        return flightSeatRepository.findFlightSeatsBySeat(seat);
    }

    @Override
    @Loggable
    public void deleteById(Long id) {
        flightSeatRepository.deleteById(id);
    }

    @Override
    @Loggable
    public Set<FlightSeat> findNotSoldById(Long id) {
        return flightSeatRepository.findAllFlightsSeatByFlightIdAndIsSoldFalse(id);
    }

    @Override
    public List<FlightSeat> findFlightSeatsByFlightIdAndSeatCategory(Long id, CategoryType type) {
        return flightSeatRepository.findFlightSeatsByFlightIdAndSeatCategory(id, type);
    }

    @Override
    public List<FlightSeat> findSingleFlightSeatByFlightIdAndSeatCategory(Long id, CategoryType type) {
        return flightSeatRepository.findFlightSeatsByFlightIdAndSeatCategory(id, type)
                .stream()
                .limit(1)
                .collect(Collectors.toList());
    }


    public Page<FlightSeat> findNotSoldById(Long id, Pageable pageable) {
        return flightSeatRepository.findAllFlightsSeatByFlightIdAndIsSoldFalse(id, pageable);
    }

    public int generateFareForFlightseat(Seat seat) {
        int baseFare = 5000;
        float emergencyExitRatio;
        float categoryRatio;
        float lockedBackRatio;
        if (seat.getIsNearEmergencyExit()) {
            emergencyExitRatio = 1.3f;
        } else emergencyExitRatio = 1f;
        if (seat.getIsLockedBack()) {
            lockedBackRatio = 0.8f;
        } else lockedBackRatio = 1f;
        switch (seat.getCategory().getCategoryType()) {
            case PREMIUM_ECONOMY : categoryRatio = 1.2f;
                break;
            case BUSINESS : categoryRatio = 2f;
                break;
            case FIRST : categoryRatio = 2.5f;
                break;
            default : categoryRatio = 1f;
        }
        return Math.round(baseFare * emergencyExitRatio * categoryRatio * lockedBackRatio);
    }
}
