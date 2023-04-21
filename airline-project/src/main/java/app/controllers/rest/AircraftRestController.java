package app.controllers.rest;

import app.dto.AircraftDTO;
import app.entities.Aircraft;
import app.services.interfaces.AircraftService;
import app.util.mappers.AircraftMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Aircraft REST")
@Tag(name = "Aircraft REST", description = "API для операций с самолётом(-ами)")
@RestController
@RequestMapping("/api/aircrafts")
@Slf4j
@RequiredArgsConstructor
public class AircraftRestController {

    private final AircraftService aircraftService;
    private final AircraftMapper aircraftMapper;

    @GetMapping()
    @ApiOperation(value = "Get list of all Aircraft")
    public ResponseEntity<List<AircraftDTO>> getAllAircraft(Pageable pageable) {
        log.info("getAllAircraft: get all aircrafts");
        var aircrafts = aircraftService.findAll(pageable);
        return aircrafts.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(aircrafts.getContent().stream().map(AircraftDTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Aircraft by it's \"id\"")
    public ResponseEntity<AircraftDTO> getAircraftById(
            @ApiParam(
                    name = "id",
                    value = "Aircraft.id"
            )
            @PathVariable("id") Long id) {
        Aircraft aircraft = aircraftService.findById(id);
        if (aircraft == null) {
            log.error("getAircraftById: aircraft with id={} doesn't exist.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("getAircraftById: aircraft with id={} returned.", id);
        return new ResponseEntity<>(new AircraftDTO(aircraft), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "Create new Aircraft")
    public ResponseEntity<?> saveAircraft(
            @ApiParam(
                    name = "Aircraft",
                    value = "Aircraft model"
            )
            @RequestBody @Valid AircraftDTO aircraftDTO) {
        log.info("saveAircraft: new aircraft saved.");
        return new ResponseEntity<>(aircraftService.save(aircraftMapper
                .convertToAircraftEntity(aircraftDTO)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Edit Aircraft by \"id\"")
    public ResponseEntity<?> editAircraft(
            @ApiParam(
                    name = "id",
                    value = "Aircraft.id"
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "Aircraft",
                    value = "Aircraft model"
            )
            @RequestBody @Valid AircraftDTO aircraftDTO) {
        if (aircraftService.findById(id) == null) {
            log.error("editAircraft: aircraft with id={} doesn't exist.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        aircraftDTO.setId(id);
        log.info("editAircraft: the aircraft with id={} has been edited.", id);
        return ResponseEntity.ok(aircraftService.save(aircraftMapper
                .convertToAircraftEntity(aircraftDTO)));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete Aircraft by \"id\"")
    public ResponseEntity<HttpStatus> deleteAircraft(
            @ApiParam(
                    name = "id",
                    value = "Aircraft.id"
            )
            @PathVariable("id") Long id) {
        try {
            aircraftService.delete(id);
            log.info("deleteAircraft: the aircraft with id={} has been deleted.", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("deleteAircraft: error of deleting - aircraft with id={} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }
}
