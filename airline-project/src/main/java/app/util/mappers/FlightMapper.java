package app.util.mappers;

import app.dto.FlightDTO;
import app.entities.Flight;
import app.services.interfaces.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlightMapper {

    private final AircraftService aircraftService;


    public Flight convertToFlightEntity(FlightDTO flightDTO) {
        Flight flight = new Flight();
        flight.setId(flightDTO.getId());
        flight.setCode(flightDTO.getCode());
        flight.setFrom(flightDTO.getFrom());
        flight.setTo(flightDTO.getTo());
        flight.setDepartureDateTime(flightDTO.getDepartureDateTime());
        flight.setArrivalDateTime(flightDTO.getArrivalDateTime());
        flight.setAircraft(aircraftService.findById(flightDTO.getAircraftId()));
        flight.setFlightStatus(flightDTO.getFlightStatus());
        return flight;
    }

    public  FlightDTO convertToFlightDTOEntity(Flight flight){
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setId(flight.getId());
        flightDTO.setCode(flight.getCode());
        flightDTO.setFrom(flight.getFrom());
        flightDTO.setTo(flight.getTo());
        flightDTO.setDepartureDateTime(flight.getDepartureDateTime());
        flightDTO.setArrivalDateTime(flight.getArrivalDateTime());
        flightDTO.setAircraftId(flight.getAircraft().getId());
        flightDTO.setFlightStatus(flight.getFlightStatus());
        return flightDTO;
    }
}
