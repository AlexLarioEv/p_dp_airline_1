package app.util.mappers;

import app.dto.TicketDTO;
import app.entities.Ticket;
import app.services.interfaces.FlightSeatService;
import app.services.interfaces.FlightService;
import app.services.interfaces.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final PassengerService passengerService;
    private final FlightService flightService;
    private final FlightSeatService flightSeatService;

    public Ticket convertToTicketEntity(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(ticketDTO.getTicketNumber());
        ticket.setPassenger(passengerService.findById(ticketDTO.getPassengerId()).orElse(null));
        ticket.setFlight(flightService.getById(ticketDTO.getFlightId()));
        ticket.setFlightSeat(flightSeatService.findById(ticketDTO.getFlightSeatId()));
        return ticket;
    }
}
