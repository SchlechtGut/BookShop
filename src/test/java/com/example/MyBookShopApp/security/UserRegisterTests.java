package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.UserRepository;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class UserRegisterTests {

    private static RegistrationForm registrationForm;
    private static ContactConfirmationPayload contactConfirmationPayload;
    private final UserRegister userRegister;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @MockBean
    private UserRepository userRepositoryMock;

    @Autowired
    UserRegisterTests(UserRegister userRegister, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userRegister = userRegister;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@mail.ru");
        registrationForm.setName("Tester");
        registrationForm.setPassword("123456");
        registrationForm.setPhone("9253018770");

        contactConfirmationPayload = new ContactConfirmationPayload();
        contactConfirmationPayload.setContact("test@mail.ru");
        contactConfirmationPayload.setCode("123456");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
        contactConfirmationPayload = null;
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