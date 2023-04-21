package app.controllers.rest;

import app.dto.account.PassengerDTO;
import app.entities.account.Passenger;
import app.services.interfaces.PassengerService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = "Passenger REST")
@Tag(name = "Passenger REST", description = "API для операций с пассажирами")
@Slf4j
@RestController
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
public class PassengerRestController {

    private final PassengerService passengerService;

    @ApiOperation(value = "Find passenger by Email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "passenger found"),
            @ApiResponse(code = 404, message = "Passenger not found")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<PassengerDTO> getByEmail(
            @ApiParam(
                    name = "email",
                    example = "passenger@mail.ru",
                    required = true
            )
            @PathVariable String email) {
        log.info("getByEmail: find passenger by email = {}", email);
        Passenger passenger = passengerService.findByEmail(email);

        if (passenger == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new PassengerDTO(passenger), HttpStatus.OK);
    }

    @ApiOperation(value = "Get list of all Passenger")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "passengers found"),
            @ApiResponse(code = 404, message = "Passengers not found")
    })
    @GetMapping()
    public ResponseEntity<List<PassengerDTO>> getAll(@PageableDefault(sort = {"id"}) Pageable p) {
        log.info("getAll: find all passengers");
        Page<Passenger> passengers = passengerService.findAll(p);

        return passengers.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(passengers.stream().map(PassengerDTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    @ApiOperation(value = "Get Passenger by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "passenger found"),
            @ApiResponse(code = 404, message = "Passenger not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PassengerDTO> getById(
            @ApiParam(
                    name = "id",
                    value = "User.id",
                    required = true
            )
            @PathVariable Long id) {
        log.info("getById: find passenger by ID = {}", id);
        Optional<Passenger> passenger = passengerService.findById(id);

        if (passenger.isEmpty()) {
            log.error("getById: passenger with this id={} doesnt exist", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(new PassengerDTO(passenger.get()), HttpStatus.OK);

    }


    @ApiOperation(value = "Get list of Passenger by first name", notes = "Be careful with letter 'ё' ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "passengers found"),
            @ApiResponse(code = 404, message = "Passengers not found")
    })
    @GetMapping("/firstName/{passengerFirstName}")
    public ResponseEntity<List<PassengerDTO>> getByFirstName(
            @ApiParam(
                    name = "passengerFirstName",
                    example = " \"Иван\", \"Пётр\"",
                    required = true
            )
            @PathVariable String passengerFirstName) {
        log.info("getByFullName: find passenger by firstname = {}", passengerFirstName);
        List<Passenger> passengerList = passengerService.findByFistName(passengerFirstName);

        return passengerList.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(passengerList.stream().map(PassengerDTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    @ApiOperation(value = "Get list of Passenger by last name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "passengers found"),
            @ApiResponse(code = 404, message = "Passengers not found")
    })
    @GetMapping("/lastName/{passengerLastName}")
    public ResponseEntity<List<PassengerDTO>> getByLastName(
            @ApiParam(
                    name = "passengerLastName",
                    value = "Passenger last name",
                    example = "\"Иванов\" \"Петров\" \"Евдокимов\"",
                    required = true
            )
            @PathVariable String passengerLastName) {
        log.info("getByLastName: find passengers by lastname = {}", passengerLastName);
        List<Passenger> passengerList = passengerService.findByLastName(passengerLastName);

        return passengerList.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(passengerList.stream().map(PassengerDTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    @ApiOperation(value = "Get list of Passengers by any name (firstname, lastname, middlename")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "passengers found"),
            @ApiResponse(code = 404, message = "Passengers not found")
    })
    @GetMapping("/anyName/{passengerAnyName}")
    public ResponseEntity<List<PassengerDTO>> getByAnyName(
            @ApiParam(
                    name = "passengerAnyName",
                    value = "Passenger any name, custom format\n",
                    example = "\"Пётр\" \"Петрович Пётр\" \"Пётр Петров Петрович\"",
                    required = true
            )
            @PathVariable String passengerAnyName) {
        log.info("getByAnyName: find passenger by anyName = {}", passengerAnyName);
        List<Passenger> passengerList = passengerService.findByAnyName(passengerAnyName);

        return passengerList.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(passengerList.stream().map(PassengerDTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    @ApiOperation(value = "Get Passenger by passportSerialNumber")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "passenger found"),
            @ApiResponse(code = 404, message = "Passenger not found")
    })
    @GetMapping("/passport/{serialNumber}")
    public ResponseEntity<PassengerDTO> getByPassportSerialNumber(
            @ApiParam(
                    name = "passportSerialNumber",
                    value = "Passenger serial and number of passport\n",
                    example = "3333 333333",
                    required = true
            )
            @PathVariable String serialNumber) {
        log.info("getByPassportSerialNumber: find passenger by PassportSerialNumber. serialNumber={}", serialNumber);
        Optional<Passenger> passenger = passengerService.findByPassportSerialNumber(serialNumber);

        if (passenger.isEmpty()) {
            log.error("getByPassportSerialNumber: passenger with this serial number doesnt exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new PassengerDTO(passenger.get()), HttpStatus.OK);
    }


    @ApiOperation(value = "Create new Passenger", notes = "Create method requires in model field \"@type\": \"passenger\"")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "passenger created"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping()
    public ResponseEntity<PassengerDTO> addPassenger(
            @ApiParam(
                    name = "passenger",
                    value = "Passenger model"
            )
            @RequestBody @Valid PassengerDTO passengerDTO) {

        if (passengerDTO.getId() != null) {
            log.error("addPassenger: passenger already exist in database");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.info("addPassenger: new passenger added");
        return new ResponseEntity<>(new PassengerDTO(passengerService.save(passengerDTO.convertToEntity())),
                HttpStatus.CREATED);
    }

    @ApiOperation(value = "Edit Passenger", notes = "Update method requires in model field \"@type\": \"passenger\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "passenger edited"),
            @ApiResponse(code = 404, message = "passenger for edit not found")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<PassengerDTO> editPassenger(
            @ApiParam(
                    name = "id",
                    value = "User.id",
                    required = true
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "passenger",
                    value = "Passenger model"
            )
            @RequestBody @Valid PassengerDTO passengerDTO) {
        passengerDTO.setId(id);

        log.info("editPassenger: edit passenger={}", passengerDTO);
        return new ResponseEntity<>(new PassengerDTO(passengerService.update(passengerDTO.convertToEntity())),
                HttpStatus.OK);
    }


    @ApiOperation(value = "Delete Passenger by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "passenger deleted"),
            @ApiResponse(code = 404, message = "passenger for delete not found")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deletePassenger(
            @ApiParam(
                    name = "id",
                    value = "User.id",
                    required = true
            )
            @PathVariable Long id) {

        log.info("deletePassenger: passenger with id={} deleted", id);
        passengerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}


