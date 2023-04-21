package app.controllers;

import app.dto.FlightSeatDTO;
import app.dto.SearchResultDTO;
import app.entities.search.Search;
import app.entities.search.SearchResult;
import app.repositories.SearchResultProjection;
import app.services.interfaces.SearchService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Search")
@Tag(name = "Search", description = "API поиска маршрутов с заданными параметрами")
@RestController
@RequestMapping("/api/search")
@Slf4j
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @PostMapping
    @ApiOperation(value = "Create new search",
            notes = "Минимально необходимые поля для корректной работы контроллера:\n" +
                    " \"from\": {\"airportCode\": \"value\"},\n" +
                    " \"to\": {\"airportCode\": \"value\"},\n" +
                    " \"departureDate\": \"yyyy-mm-dd\",\n" +
                    " \"numberOfPassengers\": value (value - mast be bigger then 0)")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "returned \"id\" of SearchResult"),
            @ApiResponse(code = 400, message = "search return error. check validField "),
            @ApiResponse(code = 404, message = "Destinations not found")
    })
    public ResponseEntity<SearchResultDTO> saveSearch(
            @ApiParam(
                    name = "search",
                    value = "Search model"
            )
            @RequestBody @Valid Search search) {
        if (search.getFrom() == null) {
            log.info("saveSearch: Destination.from is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (search.getReturnDate() != null && !search.getReturnDate().isAfter(search.getDepartureDate())) {
                log.info("saveSearch: DepartureDate must be earlier then ReturnDate");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            SearchResult searchResult = searchService.saveSearch(search);
            if (searchResult.getDepartFlight().isEmpty()) {
                log.info("saveSearch: Destinations not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            log.info("saveSearch: new search result saved with id= {}", searchResult.getId());
            SearchResultDTO result = new SearchResultDTO(searchResult);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get search result by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "search result found"),
            @ApiResponse(code = 404, message = "search result not found")
    })
    public ResponseEntity<List<SearchResultDTO>> getSearchResultById(

            @PageableDefault() Pageable pageable,
            @ApiParam(
                    name = "id",
                    value = "SearchResult.id"
            )
            @PathVariable("id") long id) {

        Page<SearchResultProjection> searchResult = searchService.findSearchResultByID(id, pageable);
        if (!searchResult.isEmpty()) {
            log.info("getSearchResultById: find search result with id = {}", id);
            return new ResponseEntity<>(searchResult
                    .stream()
                    .map(SearchResultDTO::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            log.info("getSearchResultById: not find search result with id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}