package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRegisterTests {

    private RegistrationForm registrationForm;
    private final UserRegister userRegister;
    private final PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepositoryMock;

    @Autowired
    UserRegisterTests(UserRegister userRegister, PasswordEncoder passwordEncoder) {
        this.userRegister = userRegister;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@mail.org");
        registrationForm.setName("Tester");
        registrationForm.setPassword("iddqd");
        registrationForm.setPhone("9253018770");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
    }

    @Test
    void registerNewUser() {
        User user = userRegister.registerNewUser(registrationForm);
        assertNotNull(user);
        assertTrue(passwordEncoder.matches(registrationForm.getPassword(), user.getPassword()));
        assertTrue(CoreMatchers.is(user.getPhone()).matches(registrationForm.getPhone()));
        assertTrue(CoreMatchers.is(user.getName()).matches(registrationForm.getName()));
        assertTrue(CoreMatchers.is(user.getEmail()).matches(registrationForm.getEmail()));

        Mockito.verify(userRepositoryMock, Mockito.times(1)).save(Mockito.any(User.class));

    }

    @Test
    void registerNewUserFail() {
        Mockito.doReturn(new User())
                .when(userRepositoryMock)
                .findByEmail(registrationForm.getEmail());

        User user = userRegister.registerNewUser(registrationForm);
        assertNull(user);
    }
}