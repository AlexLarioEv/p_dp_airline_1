package app.controllers.rest;

import app.dto.DestinationDTO;
import app.entities.Destination;
import app.services.interfaces.DestinationService;
import app.util.mappers.DestinationMapper;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;


import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Destination REST")
@Tag(name = "Destination REST", description = "API для операций с пунктами назначения(прилет/вылет)")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/destinations")
public class DestinationRestController {
    private final DestinationService destinationService;
    private final DestinationMapper destinationMapper;

    @ApiOperation(value = "Create new Destination")
    @ApiResponse(code = 201, message = "Destination created")
    @PostMapping
    public ResponseEntity<DestinationDTO> createDestination(
            @ApiParam(
                    name = "destination",
                    value = "Destination DTO"
            )
            @RequestBody DestinationDTO destinationDTO) {
        log.info("createDestination: create new destination");
        destinationService.saveDestination(destinationMapper.convertToDestinationEntity(destinationDTO));
        return new ResponseEntity<>(destinationDTO, HttpStatus.CREATED);
    }

    @GetMapping()
    @ApiOperation(value = "Get list of all Destination")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "destinations found"),
            @ApiResponse(code = 404, message = "destinations not found")
    })
    public ResponseEntity<List<DestinationDTO>> getAllDestination(@PageableDefault(sort = {"id"}) Pageable p) {

        Page<Destination> destinations = destinationService.findAll(p);

        if (destinations != null) {
            log.info("getAllDestination: find all destinations");
            return new ResponseEntity<>(destinations
                    .stream()
                    .map(DestinationDTO::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            log.info("getAllDestination: list destinations is null");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Gets list of Destination by cityName or countryName")
    @ApiResponse(code = 200, message = "Found the destinations")
    @GetMapping("/filter")
    public ResponseEntity<List<DestinationDTO>> showDestinationByCityName(
            @ApiParam(
                    name = "cityName",
                    value = "cityName",
                    example = "Волгоград"
            )
            @RequestParam(value = "cityName", required = false) String cityName,
            @ApiParam(
                    name = "countryName",
                    value = "countryName"
            )
            @RequestParam(value = "countryName", required = false) String countryName) {
        log.info("showDestinationByCityName: search destination by cityName or countryName.countryName={} / cityName={}", countryName, cityName);
        List<Destination> destination = destinationService.findDestinationByName(cityName, countryName);
        return destination != null
                ? new ResponseEntity<>(destination
                .stream()
                .map(DestinationDTO::new)
                .collect(Collectors.toList()), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Edit Destination by id")
    @ApiResponse(code = 200, message = "Destination has been updated")
    @PatchMapping("/{id}")
    public ResponseEntity<Destination> updateDestination(
            @ApiParam(
                    name = "id",
                    value = "Destination.id"
            ) @PathVariable Long id,
            @ApiParam(
                    name = "destination",
                    value = "Destination DTO"
            )
            @RequestBody DestinationDTO destinationDTO) {
        log.info("updateDestination: update of current destination WITH id={}", id);
        destinationService.updateDestination(id, destinationMapper.convertToDestinationEntity(destinationDTO));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete destination by \"id\"")
    @ApiResponse(code = 200, message = "Destination has been removed")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDestination(@PathVariable @ApiParam("id") Long id) {
        log.info("deleteDestination: delete of current destination. id={}", id);
        destinationService.deleteDestinationById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
