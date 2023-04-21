package app.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-flight-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FlightRestControllerIT extends IntegrationTestBase {

    @Test
    void showAllFlights_test() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/flights/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/sqlQuery/delete-from-tables.sql"})
    void showAllFlights_testError() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/flights/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));
    }

}
