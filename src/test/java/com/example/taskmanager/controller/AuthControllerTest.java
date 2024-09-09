package com.example.taskmanager.controller;

import com.example.taskmanager.AbstractTest;
import com.example.taskmanager.dto.auth.UpsertUserRq;
import com.example.taskmanager.entity.RoleType;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("Integration tests for User's signin and register")
@Tag("integration")
class AuthControllerTest extends AbstractTest {


    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws Exception {

    }

    @Test
    @DisplayName("Checking for receipt of security tokens with correct data of user")
    void whenSigninWithCorrectAdmin_whenReturnTokens() throws Exception {

        User admin = createUser("admin", "admin@mail.com", RoleType.ROLE_ADMIN, "admin");
        refreshTokenService.createRefreshToken(admin.getId());
        setAuthenticationInContext(admin, "admin");


        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"admin@mail.com\", \"password\": \"admin\" }")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(admin.getId()))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.email").value("admin@mail.com"))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refreshToken").exists());

    }

    @Test
    @DisplayName("Checking for receipt of security tokens with incorrect data of user")
    void whenSigninIncorrect_whenErrorMassage() throws Exception {

        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user@mail.com\", \"password\": \"user\" }")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Unauthorized"))
                .andExpect(jsonPath("$.detail").value("Bad credentials"))
                .andExpect(jsonPath("$.description").value("The email or password is incorrect"));

    }

    @Test
    @DisplayName("Checking register user with correct data")
    void whenRegisteredUser_thanReturnMessage() throws Exception {

        UpsertUserRq upsertUserRq = UpsertUserRq.builder()
                .username("user")
                .email("user@mail.com")
                .roles(Set.of(RoleType.ROLE_USER))
                .password("user")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(upsertUserRq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User created"));
    }

    @Test
    @DisplayName("Checking register user with incorrect data")
    void whenRegisteredBadUser_thanReturnErrorMessage() throws Exception {

        UpsertUserRq upsertUserRq = UpsertUserRq.builder()
                .username("user")
                .email("usermail.com")
                .roles(Set.of(RoleType.ROLE_USER))
                .password("user")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(upsertUserRq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The email address must be in the format user@example.com"));
    }
}