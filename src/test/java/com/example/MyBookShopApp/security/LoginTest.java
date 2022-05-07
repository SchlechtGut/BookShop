package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.UserRepository;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class LoginTest{

    private final JWTUtil jwtUtil;
    private final UserRegister userRegister;
    private final UserRepository userRepository;

    private static ContactConfirmationPayload contactConfirmationPayload;

    @Autowired
    LoginTest(JWTUtil jwtUtil, UserRegister userRegister, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRegister = userRegister;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        contactConfirmationPayload = new ContactConfirmationPayload();
        contactConfirmationPayload.setContact("test@mail.ru");
        contactConfirmationPayload.setCode("123456");

        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setPassword("123456");
        registrationForm.setEmail("test@mail.ru");

        userRegister.registerNewUser(registrationForm);
    }

    @AfterEach
    void tearDown() {
        contactConfirmationPayload = null;
        userRepository.deleteUserByEmail("test@mail.ru");
    }

    @Test
    void jwtLogin() {
        User user = new User();
        user.setEmail("test@mail.ru");
        BookstoreUserDetails userDetails = new BookstoreUserDetails(user);
        String jwtToken = jwtUtil.generateToken(userDetails);

        ContactConfirmationResponse response = userRegister.jwtLogin(contactConfirmationPayload);
        assertNotNull(response);
        assertTrue(jwtToken.matches(response.getResult()));
    }
}