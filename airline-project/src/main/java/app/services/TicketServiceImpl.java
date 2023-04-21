package app.services;

import app.entities.Ticket;
import app.repositories.*;
import app.services.interfaces.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;

    @Override
    public Page<Ticket> findAll(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    @Override
    public Ticket findTicketByTicketNumber(String ticketNumber) {
        return ticketRepository.findByTicketNumberContainingIgnoreCase(ticketNumber);
    }


    @Override
    @Transactional
    public void deleteTicketById(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Ticket saveTicket(Ticket ticket) {
        ticket.setPassenger(passengerRepository.findByEmail(ticket.getPassenger().getEmail()));
        ticket.setFlight(flightRepository.findByCodeWithLinkedEntities(ticket.getFlight().getCode()));
        ticket.setFlightSeat(flightSeatRepository
                .findFlightSeatByFlightAndSeat(
                ticket.getFlight().getCode(),
                ticket.getFlightSeat().getSeat().getSeatNumber()
                ).orElse(null));
        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        updatedTicket.setId(id);
        if (updatedTicket.getFlight() == null) {
            updatedTicket.setFlight(ticketRepository.findTicketById(id).getFlight());
        }
        if (updatedTicket.getTicketNumber() == null) {
            updatedTicket.setTicketNumber(ticketRepository.findTicketById(updatedTicket.getId()).getTicketNumber());
        }
        if (updatedTicket.getPassenger() == null) {
            updatedTicket.setPassenger(ticketRepository.findTicketById(id).getPassenger());
        }
        if (updatedTicket.getFlightSeat() == null) {
            updatedTicket.setFlightSeat(ticketRepository.findTicketById(id).getFlightSeat());
        }
        return updatedTicket;
    }

}
