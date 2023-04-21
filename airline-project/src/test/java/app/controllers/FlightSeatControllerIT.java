package app.controllers;

import app.dto.FlightSeatDTO;
import app.entities.FlightSeat;
import app.enums.CategoryType;
import app.services.interfaces.FlightSeatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-flightSeat-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FlightSeatControllerIT extends IntegrationTestBase {

    @Autowired
    private FlightSeatService flightSeatService;

    @Test
    void shouldGetFlightSeatsByFlightId() throws Exception {
        String flightNumber = "1";
        String expected = objectMapper.writeValueAsString(flightSeatService.findByFlightId((Long.parseLong(flightNumber)), PageRequest.of(0, 835, Sort.by("seatId")))
                .stream()
                .map(FlightSeatDTO::new)
                .collect(Collectors.toList()));

        String actual = mockMvc.perform(
                        get("http://localhost:8080/api/flight-seats/all-flight-seats/{flightNumber}", flightNumber))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldGetNonSoldFlightSeatsByFlightId() throws Exception {
        String flightNumber = "1";
        String expected = objectMapper.writeValueAsString(flightSeatService.findNotSoldById((Long.parseLong(flightNumber)), PageRequest.of(0, 835, Sort.by("seatId")))
                .stream()
                .map(FlightSeatDTO::new)
                .collect(Collectors.toList()));

        String actual = mockMvc.perform(
                        get("http://localhost:8080/api/flight-seats/all-flight-seats/{flightNumber}", flightNumber).param("isSold", "false"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldReturnExistingFlightSeatsByFlightId() throws Exception {
        String flightId = "1";
        Set<FlightSeat> flightSeatSet = flightSeatService.addFlightSeatsByFlightNumber(flightId);
        mockMvc.perform(
                        post("http://localhost:8080/api/flight-seats/all-flight-seats/{flightId}", flightId)
                                .content(objectMapper.writeValueAsString(flightSeatSet))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddFlightSeatsByFlightId() throws Exception {
        String flightId = "1";
        Set<FlightSeat> flightSeatSet = flightSeatService.findByFlightId(1L);
        List<Long> idList = flightSeatSet.stream().map(FlightSeat::getId).collect(Collectors.toList());
        for (Long id : idList) {
            flightSeatService.deleteById(id);
        }
        mockMvc.perform(
                        post("http://localhost:8080/api/flight-seats/all-flight-seats/{flightId}", flightId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldEditFlightSeatById() throws Exception {
        Long id = (long) 2;
        FlightSeat flightSeat = flightSeatService.findById(id);
        flightSeat.setFare(100);
        flightSeat.setIsSold(false);
        flightSeat.setIsRegistered(false);
        mockMvc.perform(patch("http://localhost:8080/api/flight-seats/{id}", id)
                        .content(objectMapper.writeValueAsString(new FlightSeatDTO(flightSeat)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetCheapestFlightSeatByFlightAndSeatCategory() throws Exception {
        String flightId = "1";
        String category = "BUSINESS";

        mockMvc.perform(get("http://localhost:8080/api/flight-seats/cheapest")
                        .param("flightID", flightId)
                        .param("category", category))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        flightSeatService.findFlightSeatsByFlightIdAndSeatCategory(1L, CategoryType.BUSINESS)
                                .stream()
                                .map(FlightSeatDTO::new)
                                .collect(Collectors.toSet())
                )));
    }

    @Test
    void shouldGetSingleCheapestFlightSeatByFlightAndSeatCategory() throws Exception {
        String flightId = "1";
        String category = "FIRST";

        mockMvc.perform(get("http://localhost:8080/api/flight-seats/cheapest_one")
                        .param("flightID", flightId)
                        .param("category", category))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        flightSeatService.findSingleFlightSeatByFlightIdAndSeatCategory(1L, CategoryType.FIRST)
                                .stream()
                                .map(FlightSeatDTO::new)
                                .collect(Collectors.toSet())
                )));
    }
}
