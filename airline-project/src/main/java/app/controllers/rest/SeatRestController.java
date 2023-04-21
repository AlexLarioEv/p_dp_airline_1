package app.controllers.rest;

import app.dto.SeatDTO;
import app.entities.Seat;

import app.exceptions.ViolationOfForeignKeyConstraintException;
import app.services.interfaces.AircraftService;
import app.services.interfaces.SeatService;
import app.util.mappers.SeatMapper;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Seat REST")
@Tag(name = "Seat REST", description = "API для операций с местом в самолете")
@RestController
@RequestMapping("/api/seats")
@Slf4j
@RequiredArgsConstructor
public class SeatRestController {
    private final SeatService seatService;
    private final AircraftService aircraftService;
    private final SeatMapper seatMapper;

    @PostMapping
    @ApiOperation(value = "Create new Seat")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "seat created"),
            @ApiResponse(code = 400, message = "seat not created")
    })
    public ResponseEntity<SeatDTO> saveSeat(
            @ApiParam(
                    name = "seat",
                    value = "Seat model"
            )
            @RequestBody @Valid SeatDTO seatDTO) {
        Seat seat = seatMapper.convertToSeatEntity(seatDTO);
        seatService.save(seat);
        log.info("saveSeat: new seat saved with id= {}", seat.getId());
        return new ResponseEntity<>(new SeatDTO(seatService.findById(seat.getId())),HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Seat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "seat found"),
            @ApiResponse(code = 404, message = "seat not found")
    })
    public ResponseEntity<SeatDTO> getSeatById(
            @ApiParam(
                    name = "id",
                    value = "Seat.id"
            )
            @PathVariable("id") long id) {

        Seat seat = seatService.findById(id);

        if (seat != null) {
            log.info("getSeatById: find seat with id = {}", id);
            return new ResponseEntity<>(new SeatDTO(seat), HttpStatus.OK);
        } else {
            log.info("getSeatById: not find seat with id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Edit Seat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "seat edited"),
            @ApiResponse(code = 400, message = "seat failed to edit"),
            @ApiResponse(code = 404, message = "seat not found")
    })
    public ResponseEntity<SeatDTO> editSeat(
            @ApiParam(
                    name = "id",
                    value = "Seat.id"
            )
            @PathVariable("id") long id,
            @ApiParam(
                    name = "seat",
                    value = "Seat model"
            )
            @RequestBody @Valid SeatDTO seatDTO) {
        if (seatService.findById(id) == null) {
            log.error("editSeat: error of editing seat by id = {} - not found seat.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        seatService.editById(id, seatMapper.convertToSeatEntity(seatDTO));
        log.info("editSeat: the seat with id = {} has been edited.", id);
        return new ResponseEntity<>(new SeatDTO(seatService.findById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete Seat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "seat deleted"),
            @ApiResponse(code = 404, message = "seat not found"),
            @ApiResponse(code = 405, message = "seat is locked")
    })
    public ResponseEntity<String> deleteSeat(
            @ApiParam(
                    name = "id",
                    value = "Seat.id"
            )
            @PathVariable("id") Long id){
        try {
            seatService.delete(id);
            log.info("deleteSeat: the seat with id={} has been deleted.", id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } catch (ViolationOfForeignKeyConstraintException e) {
            log.error("deleteSeat: error of deleting - seat with id={} is locked by FlightSeat.", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        } catch (EmptyResultDataAccessException e) {
            log.error("deleteSeat: error of deleting - seat with id={} not found.", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/aircraft/{aircraftId}")
    @ApiOperation(value = "Get Seats by \"aircraftId\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Seats found"),
            @ApiResponse(code = 404, message = "Seats not found")
    })
    public ResponseEntity<List<SeatDTO>> getAllSeatsByAircraftId(
            @PageableDefault(sort = {"seatId"}) Pageable pageable,
            @ApiParam(
                    name = "aircraftId",
                    value = "Aircraft.id"
            )
            @PathVariable("aircraftId") long aircraftId) {

        Page<Seat> seats = seatService.findByAircraftId(aircraftId, pageable);

        if (!seats.isEmpty()) {
            List<SeatDTO> seatDTOs = seats.stream().map(SeatDTO::new).collect(Collectors.toList());
            log.info("getSeatsByAircraftId: found {} seats with aircraftId = {}", seatDTOs.size(), aircraftId);
            return new ResponseEntity<>(seatDTOs, HttpStatus.OK);
        } else {
            log.info("getSeatByAircraftId: not found seats with aircraftId = {}", aircraftId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/aircraft/{aircraftId}")
    @ApiOperation(value = "Create new array of Seat")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "seats created"),
            @ApiResponse(code = 400, message = "seats not created"),
            @ApiResponse(code = 404, message = "aircraft with this id not found")
    })
    public ResponseEntity<List<SeatDTO>> saveManySeatsByAircraftId(@PathVariable("aircraftId") long aircraftId) {
        if (aircraftService.findById(aircraftId) == null){
            log.error("saveManySeatsByAircraftId: aircraft with id = {} not found", aircraftId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<SeatDTO> savedSeats = seatService.saveManySeats(aircraftId);
        log.info("saveManySeatsByAircraftId: saved {} new seats with aircraft.id = {}", savedSeats.size(), aircraftId);
        return new ResponseEntity<>(savedSeats, HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "Get all Seats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Seats found"),
            @ApiResponse(code = 404, message = "Seats not found")
    })
    public ResponseEntity<List<SeatDTO>> getAllSeats(@PageableDefault() Pageable pageable) {
        Page<Seat> seats = seatService.findAll(pageable);
        if (!seats.isEmpty()) {
            List<SeatDTO> seatDTOs = seats.stream().map(SeatDTO::new).collect(Collectors.toList());
            log.info("getAllSeats: found {} seats", seatDTOs.size());
            return new ResponseEntity<>(seatDTOs, HttpStatus.OK);
        } else {
            log.info("getAllSeats: seats not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
