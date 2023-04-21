package app.controllers;

import app.config.security.jwt.domain.JwtRequest;
import app.config.security.jwt.domain.JwtResponse;
import app.config.security.jwt.domain.RefreshJwtRequest;
import app.config.security.jwt.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-auth-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AuthControllerIT extends IntegrationTestBase {

    private final AuthService authService;

    @Autowired
    public AuthControllerIT(AuthService authService) {
        this.authService = authService;
    }

    @Test
    void shouldLogin() throws Exception {
        JwtRequest authRequest = new JwtRequest();
        authRequest.setUsername("admin2@mail.ru");
        authRequest.setPassword("admin2");
        mockMvc.perform(post("http://localhost:8080/api/auth/login")
                        .content(objectMapper.writeValueAsString(authRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetNewAccessToken() throws Exception {
        JwtRequest authRequest = new JwtRequest();
        authRequest.setUsername("admin2@mail.ru");
        authRequest.setPassword("admin2");
        final JwtResponse token = authService.login(authRequest);
        RefreshJwtRequest request = new RefreshJwtRequest();
        request.setRefreshToken(token.getRefreshToken());

        mockMvc.perform(post("http://localhost:8080/api/auth/token")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetNewRefreshToken() throws Exception {
        JwtRequest authRequest = new JwtRequest();
        authRequest.setUsername("admin2@mail.ru");
        authRequest.setPassword("admin2");
        final JwtResponse token = authService.login(authRequest);
        RefreshJwtRequest request = new RefreshJwtRequest();
        request.setRefreshToken(token.getRefreshToken());

        mockMvc.perform(post("http://localhost:8080/api/auth/refresh")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
