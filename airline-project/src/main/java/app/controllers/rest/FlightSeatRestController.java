package app.controllers.rest;

import app.dto.FlightSeatDTO;
import app.entities.FlightSeat;
import app.enums.CategoryType;
import app.services.FlightSeatServiceImpl;
import app.util.mappers.FlightSeatMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "FlightSeat REST")
@Tag(name = "FlightSeat REST", description = "Операции с посадочными местами на рейс")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flight-seats")
public class FlightSeatRestController {

    private final FlightSeatServiceImpl flightSeatService;
    private final FlightSeatMapper flightSeatMapper;


    @ApiOperation(value = "Get FlightSeat by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "flight seat found"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FlightSeatDTO> getFlightSeatById(
            @ApiParam(
                    name = "id",
                    value = "FlightSeat.id",
                    required = true
            )
            @PathVariable Long id
    ) {
        log.info("getFlightSeatById: get FlightSeat by id={}", id);
        return (flightSeatService.findById(id) == null) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(new FlightSeatDTO(flightSeatService.findById(id)));
    }

    @ApiOperation(value = "Get list of FlightSeat by code of Flight")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "flight seats found"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/all-flight-seats/{flightId}")
    public ResponseEntity<List<FlightSeatDTO>> getFlightSeatsByFlightId(
            @PageableDefault(sort = {"seatId"}) Pageable pageable,
            @ApiParam(
                    name = "flightId",
                    value = "Flight.id",
                    required = true
            )
            @PathVariable Long flightId,
            @ApiParam(
                    name = "isSold",
                    value = "FlightSeat.isSold"
            )
            @RequestParam(required = false) Boolean isSold) {

        if (isSold != null && !isSold) {
            log.info("getFlightSeatById: get not sold FlightSeat by id={}", flightId);
            Page<FlightSeat> result = flightSeatService.findNotSoldById(flightId, pageable);
            return (result.isEmpty()) ?
                    ResponseEntity.notFound().build() :
                    ResponseEntity.ok(result
                            .stream()
                            .map(FlightSeatDTO::new)
                            .collect(Collectors.toList()));
        }

        log.info("getFlightSeatsByFlightId: get flight seats by flightId. flightId={}", flightId);
        Page<FlightSeat> result = flightSeatService.findByFlightId(flightId, pageable);
        return (result.isEmpty()) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(result
                        .stream()
                        .map(FlightSeatDTO::new)
                        .collect(Collectors.toList() )
                );
    }

    @ApiOperation(value = "Get flight seats by flight and category")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "flightSeat found"),
            @ApiResponse(code = 404, message = "flightSeat not found")
    })
    @GetMapping("/cheapest")
    public ResponseEntity<List<FlightSeatDTO>> getFlightSeatsByFlightAndSeatCategory(
            @RequestParam(name = "flightID") Long flightID,
            @RequestParam(name = "category") CategoryType category
    ) {
        log.info("getFlightSeatsByFlightAndSeatCategory: find flightSeats by flight ID = {} and seat category = {}", flightID, category);
        List<FlightSeat> flightSeat = flightSeatService.findFlightSeatsByFlightIdAndSeatCategory(flightID, category);

        if (flightSeat.isEmpty()) {
            log.error("getFlightSeatsByFlightAndSeatCategory: flightSeats with flightID = {} or seat category = {} not found", flightID, category);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(flightSeat.stream().map(FlightSeatDTO::new).collect(Collectors.toList()), HttpStatus.OK);

    }

    @ApiOperation(value = "Get SINGLE flight seat by flight and seat category",
            notes = "Use getFlightSeatsByFlightAndSeatCategory method instead")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "flight seat found"),
            @ApiResponse(code = 404, message = "flight seat not found")
    })
    @GetMapping("/cheapest_one")
    public ResponseEntity<List<FlightSeatDTO>> getSingleFlightSeatByFlightAndSeatCategory(
            @RequestParam(name = "flightID") Long id,
            @RequestParam(name = "category") CategoryType seatCategoryType
    ) {
        List<FlightSeat> singleFlightSeat = flightSeatService.findSingleFlightSeatByFlightIdAndSeatCategory(id, seatCategoryType);

        if (singleFlightSeat.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(singleFlightSeat.stream().map(FlightSeatDTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @ApiOperation(value = "Add FlightSeat by ID of Flight")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Existed flight seats"),
            @ApiResponse(code = 201, message = "Flight seats added"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping("/all-flight-seats/{flightId}")
    public ResponseEntity<Set<FlightSeatDTO>> addFlightSeatsByFlightID(
            @ApiParam(
                    name = "flightId",
                    value = "Flight.id",
                    required = true
            )
            @PathVariable
            Long flightId) {
        log.info("addFlightSeatsByFlightNumber: add flight seats by flightId. flightId={}", flightId);
        Set<FlightSeat> flightSeats = flightSeatService.findByFlightId(flightId);
        if (!flightSeats.isEmpty()) {
            return new ResponseEntity<>(flightSeats.stream().map(FlightSeatDTO::new)
                    .collect(Collectors.toSet()), HttpStatus.OK);
        }
        return new ResponseEntity<>(flightSeatService.addFlightSeatsByFlightId(flightId)
                .stream()
                .map(FlightSeatDTO::new)
                .collect(Collectors.toSet()),
                HttpStatus.CREATED);
    }

    @ApiOperation(value = "Edit FlightSeat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "flight seat edited"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<FlightSeatDTO> editFlightSeatById(
            @ApiParam(
                    name = "id",
                    value = "FlightSeat.id",
                    required = true
            )
            @PathVariable Long id,
            @ApiParam(
                    name = "flightSeat",
                    value = "FlightSeat DTO",
                    required = true
            )
            @RequestBody
            @Valid FlightSeatDTO flightSeatDTO) {
        log.info("editFlightSeatById: edit flight seat by id. id={}", id);

        if (flightSeatService.findById(id) == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new FlightSeatDTO(flightSeatService.editFlightSeat(id, flightSeatMapper.convertToFlightSeatEntity(flightSeatDTO))));
    }

    @ApiOperation(value = "Delete FlightSeat by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlightSeatById(
            @ApiParam(
                    name = "id",
                    value = "FlightSeat.id"
            ) @PathVariable Long id
    ) {
        try {
            flightSeatService.deleteById(id);
            log.info("deleteFlightSeatById: FlightSeat with id={} deleted", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("deleteFlightSeatById: error while deleting - FlightSeat with id={} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }
}
