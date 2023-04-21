package app.controllers.rest;


import app.dto.AircraftDTO;
import app.dto.TicketDTO;
import app.entities.Ticket;
import app.services.interfaces.TicketService;
import app.util.mappers.TicketMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Ticket REST")
@Tag(name = "Ticket REST", description = "API для операций с билетами")
@Slf4j
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketRestController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @GetMapping()
    @ApiOperation(value = "Get list of all Tickets")
    public ResponseEntity<List<TicketDTO>> getAllTicket(Pageable pageable) {
        log.info("getAllTicket: get all tickets");
        var tickets = ticketService.findAll(pageable);
        return tickets.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(tickets.getContent().stream().map(TicketDTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @ApiOperation(value = "Create new Ticket")
    @ApiResponse(code = 201, message = "Ticket created")
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(
            @ApiParam(
                    name = "ticket",
                    value = "Ticket model"
            )
            @RequestBody @Valid TicketDTO ticketDTO) {
        log.info("methodName: createTicket - create new ticket");
        ticketService.saveTicket(ticketMapper.convertToTicketEntity(ticketDTO));
        return new ResponseEntity<>(ticketDTO, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Gets Ticket by ticketNumber")
    @ApiResponse(code = 200, message = "Found the ticket")
    @GetMapping("/ticket")
    public ResponseEntity<TicketDTO> showTicket(
            @ApiParam(
                    name = "ticketNumber",
                    value = "ticketNumber",
                    example = "SD-2222"
            )
            @RequestParam(value = "ticketNumber") String ticketNumber) {
        log.info("methodName: showTicketByTicketNumber - search ticket by ticketNumber");
        Ticket ticket = ticketService.findTicketByTicketNumber(ticketNumber);
        return ticket != null
                ? new ResponseEntity<>(new TicketDTO(ticket), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Edit Ticket by \"id\"")
    @ApiResponse(code = 200, message = "Ticket has been updated")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTicket(
            @ApiParam(
                    name = "id",
                    value = "Ticket.id"
            ) @PathVariable Long id,
            @ApiParam(
                    name = "ticket",
                    value = "Ticket model"
            )
            @RequestBody @Valid TicketDTO ticketDTO) {
        log.info("methodName: updateTicket - update of current ticket");
        ticketDTO.setId(id);
        return new ResponseEntity<>(ticketService.updateTicket(id, ticketMapper.convertToTicketEntity(ticketDTO)),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Ticket by \"id\"")
    @ApiResponse(code = 200, message = "Ticket has been removed")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTicket(
            @ApiParam(
                    name = "id",
                    value = "Ticket.id"
            )
            @PathVariable Long id) {
        try {
            ticketService.deleteTicketById(id);
            log.info("deleteTicket: ticket of current destination. id={}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("deleteTicket: error of deleting - ticket with id={} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }
}
