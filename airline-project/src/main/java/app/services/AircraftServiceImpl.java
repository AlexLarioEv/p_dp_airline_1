package app.services;

import app.entities.Aircraft;
import app.entities.Flight;
import app.repositories.AircraftRepository;
import app.repositories.FlightRepository;
import app.services.interfaces.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AircraftServiceImpl implements AircraftService {

    private final AircraftRepository aircraftRepository;
    private final FlightRepository flightRepository;

    @Transactional
    public Aircraft save(Aircraft aircraft) {
        if (!aircraft.getSeatSet().isEmpty()) {
            aircraft.getSeatSet().forEach(seat -> seat.setAircraft(aircraft));
        }
        return aircraftRepository.save(aircraft);
    }

    public Page<Aircraft> findAll(Pageable pageable) {
        return aircraftRepository.findAll(pageable);
    }

    public Aircraft findById(Long id) {
        return aircraftRepository.findById(id).orElse(null);
    }

    public Aircraft findByAircraftNumber(String aircraftNumber) {
        return aircraftRepository.findByAircraftNumber(aircraftNumber);
    }

    @Transactional
    public void delete(Long id) {
        List<Flight> flightSet = flightRepository.findByAircraft_Id(id);
        if (flightSet != null) {
            flightSet.forEach(flight -> flight.setAircraft(null));
        }
        aircraftRepository.deleteById(id);
    }

}