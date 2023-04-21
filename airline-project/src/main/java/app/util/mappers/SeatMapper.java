package app.util.mappers;

import app.dto.SeatDTO;
import app.entities.Seat;
import app.services.interfaces.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatMapper {

    private final AircraftService aircraftService;

    public Seat convertToSeatEntity(SeatDTO seatDTO) {
        Seat seat = new Seat();
        seat.setId(seatDTO.getId());
        seat.setSeatNumber(seatDTO.getSeatNumber());
        seat.setIsNearEmergencyExit(seatDTO.getIsNearEmergencyExit());
        seat.setIsLockedBack(seatDTO.getIsLockedBack());
        seat.setCategory(seatDTO.getCategory());
        seat.setAircraft(aircraftService.findById(seatDTO.getAircraftId()));
        return seat;
    }
}
