package app.controllers;

import app.dto.DestinationDTO;
import app.entities.Destination;
import app.enums.Airport;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-destination-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DestinationControllerIT extends IntegrationTestBase {

    @Test
    void shouldCreateDestination() throws Exception {
        Destination destination = new Destination(4L, Airport.OMS, "Moscow", "Moscow", "+3", "Russia");
        DestinationDTO destinationDTO = new DestinationDTO(destination);
        System.out.println(objectMapper.writeValueAsString(destination));
        mockMvc.perform(post("http://localhost:8080/api/destinations")
                        .content(objectMapper.writeValueAsString(destinationDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldShowDestinationByName() throws Exception {
        String city = "Абакан";
        String country = "";
        mockMvc.perform(get("http://localhost:8080/api/destinations")
                        .param("cityName", city)
                        .param("countryName", country))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    void shouldUpdateDestination() throws Exception {
        Long id = 3L;
        mockMvc.perform(patch("http://localhost:8080/api/destinations/{id}", id)
                        .content(objectMapper.writeValueAsString(new DestinationDTO
                                (new Destination(3L, Airport.RAT, "Радужный", "Радужный", "+3", "Россия"))))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteDestinationById() throws Exception {
        long id = 2;
        mockMvc.perform(delete("http://localhost:8080/api/destinations/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
