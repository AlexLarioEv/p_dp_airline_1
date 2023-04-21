package app.controllers;

import app.dto.account.PassengerDTO;
import app.entities.account.Passenger;
import app.services.interfaces.PassengerService;
import app.services.interfaces.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PassengerRestControllerIT extends IntegrationTestBase {
    @Autowired
    private PassengerService passengerService;

    @Autowired
    private RoleService roleService;

    @Test
    @DisplayName("Get all passengers with pagination")
    void shouldGetAllPassengers() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        mockMvc.perform(
                        get("http://localhost:8080/api/passengers"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(passengerService.findAll(pageable)
                                .stream().map(PassengerDTO::new).collect(Collectors.toList())))
                );
    }

    @Test
    @DisplayName("Get passenger by ID")
    void shouldGetPassengerById() throws Exception {
        long id = 4L;
        mockMvc.perform(
                        get("http://localhost:8080/api/passengers/{id}", id))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(new PassengerDTO(passengerService.findById(id).get())))
                );

    }

    @Test
    @DisplayName("Get passenger by any name")
    void shouldGetPassengerByAnyName() throws Exception {
        String name = "Ivanov Jovanovich"; // Фамилия и Отчество человека
        mockMvc.perform(
                        get("http://localhost:8080/api/passengers/anyName/{passengerAnyName}", name))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(passengerService.findByAnyName(name)
                                .stream().map(PassengerDTO::new).collect(Collectors.toList())))
                );
    }

    @Test
    @DisplayName("Get passenger by first name")
    void shouldGetPassengerByFirstName() throws Exception {
        String firstName = "Ivan";
        mockMvc.perform(
                        get("http://localhost:8080/api/passengers/firstName/{passengerFirstName}", firstName))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(passengerService.findByFistName(firstName)
                                .stream().map(PassengerDTO::new).collect(Collectors.toList())))
                );
    }

    @Test
    @DisplayName("Get passenger by last name")
    void shouldGetPassengerByLastName() throws Exception {
        String lastName = "Ivanov";
        mockMvc.perform(
                        get("http://localhost:8080/api/passengers/lastName/{passengerLastName}", lastName))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(passengerService.findByLastName(lastName)
                                .stream().map(PassengerDTO::new).collect(Collectors.toList())))
                );
    }

    @Test
    @DisplayName("Get passenger by email")
    void shouldGetPassengerByEmail() throws Exception {
        String email = "passenger@mail.ru";
        mockMvc.perform(
                        get("http://localhost:8080/api/passengers/email/{email}", email))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(
                                new PassengerDTO(passengerService.findByEmail(email))))
                );
    }

    @Test
    @DisplayName("Get passenger by serial and number of passport")
    void shouldGetPassengerByPassportSerialNumber() throws Exception {
        String serialNumber = "2222 222222";
        mockMvc.perform(
                        get("http://localhost:8080/api/passengers/passport/{serialNumber}", serialNumber))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(
                                new PassengerDTO(passengerService.findByPassportSerialNumber(serialNumber).get())))
                );
    }

    @Test
    @DisplayName("Post new passenger")
    void shouldAddNewPassenger() throws Exception {
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setFirstName("Petr");
        passengerDTO.setLastName("Petrov");
        passengerDTO.setBirthDate(LocalDate.of(2023, 3, 23));
        passengerDTO.setPhoneNumber("79222222222");
        passengerDTO.setEmail("petrov@mail.ru");
        passengerDTO.setPassword("passwordIsGood?1@");
        passengerDTO.setSecurityQuestion("securityQuestion");
        passengerDTO.setAnswerQuestion("securityQuestion");
        passengerDTO.setRoles(Set.of(roleService.getRoleByName("ROLE_PASSENGER")));

        mockMvc.perform(
                        post("http://localhost:8080/api/passengers")
                                .content(objectMapper.writeValueAsString(passengerDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Post exist passenger")
    void shouldAddExistPassenger() throws Exception {
        Passenger passenger = passengerService.findById(4L).get();

        mockMvc.perform(
                        post("http://localhost:8080/api/passengers")
                                .content(objectMapper.writeValueAsString(passenger))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete passenger by ID and check passenger with deleted ID")
    void shouldDeletePassenger() throws Exception {
        long id = 4L;
        mockMvc.perform(delete("http://localhost:8080/api/passengers/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("http://localhost:8080/api/passengers/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update passenger")
    void shouldUpdatePassenger() throws Exception {
        long id = 4L;
        PassengerDTO passengerDTO = new PassengerDTO(passengerService.findById(4L).get());
        passengerDTO.setFirstName("Klark");

        mockMvc.perform(put("http://localhost:8080/api/passengers/{id}", id)
                        .content(objectMapper.writeValueAsString(passengerDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
