package app.controllers;

import app.dto.account.AccountDTO;
import app.dto.account.AirlineManagerDTO;
import app.entities.account.AirlineManager;
import app.services.interfaces.AccountService;
import app.services.interfaces.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AccountControllerIT extends IntegrationTestBase {

    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleService roleService;

    @Test
    void shouldGetAllAccounts() throws Exception {
        mockMvc.perform(
                        get("http://localhost:8080/api/accounts"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAccountById() throws Exception {
        Long id = 4L;
        mockMvc.perform(
                        get("http://localhost:8080/api/accounts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new AccountDTO(accountService.getAccountById(id).get()))));
    }

    @Test
    void shouldGetNotExistedAccount() throws Exception {
        Long id = 100L;
        mockMvc.perform(
                        get("http://localhost:8080/api/accounts/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPostNewAccount() throws Exception {
        AirlineManagerDTO airlineManager = new AirlineManagerDTO();
        airlineManager.setEmail("manager2@mail.ru");
        airlineManager.setPassword("Test123@");
        airlineManager.setSecurityQuestion("Test");
        airlineManager.setAnswerQuestion("Test");
        airlineManager.setRoles(Set.of(roleService.getRoleByName("ROLE_MANAGER")));
        mockMvc.perform(post("http://localhost:8080/api/accounts")
                        .content(objectMapper.writeValueAsString(airlineManager))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteAccountById() throws Exception {
        Long id = 4L;
        mockMvc.perform(delete("http://localhost:8080/api/accounts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("http://localhost:8080/api/accounts/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    void shouldUpdateAccount() throws Exception {
        Long id = 3L;
        AirlineManagerDTO updatableAccount = new AirlineManagerDTO((AirlineManager) accountService.getAccountById(id).get());
        updatableAccount.setEmail("test@mail.ru");
        mockMvc.perform(patch("http://localhost:8080/api/accounts/{id}", id)
                        .content(objectMapper.writeValueAsString(updatableAccount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.ru"));
    }
}
