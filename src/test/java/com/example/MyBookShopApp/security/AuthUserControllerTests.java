package com.example.MyBookShopApp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.web.SpringBootMockServletContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class AuthUserControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    AuthUserControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handleUserRegistration() throws Exception {
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@mail.ru");
        registrationForm.setName("Tester");
        registrationForm.setPassword("123456");
        registrationForm.setPhone("1112223334");

        ObjectMapper objectMapper = new ObjectMapper();

        String body = objectMapper.writeValueAsString(registrationForm);

        mockMvc.perform(post("/reg")
                        .param("name", "Tester")
                        .param("email", "test@mail.ru")
                        .param("password", "123456")
                        .param("phone", "1112223334"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signin"));
    }

    @Test
    void handleLogin() throws Exception {
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setCode("123456");
        payload.setContact("test@mail.ru");

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(payload);

        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void handleLogout() throws Exception {
        Cookie cookie = new Cookie("token", "token");
        mockMvc.perform(MockMvcRequestBuilders.post("/logout")
                        .cookie(cookie))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signin"));

    }
}