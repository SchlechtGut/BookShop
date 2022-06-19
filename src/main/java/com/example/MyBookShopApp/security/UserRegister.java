package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.api.request.ProfileRequest;
import com.example.MyBookShopApp.data.ProfileResetToken;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.ProfileResetTokenRepository;
import com.example.MyBookShopApp.repository.UserRepository;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserRegister {

    private final UserRepository bookstoreUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final ProfileResetTokenRepository profileResetTokenRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public UserRegister(UserRepository bookstoreUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil, ProfileResetTokenRepository profileResetTokenRepository, JavaMailSender javaMailSender) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.profileResetTokenRepository = profileResetTokenRepository;
        this.javaMailSender = javaMailSender;
    }

    public User registerNewUser(RegistrationForm registrationForm) {

        if (bookstoreUserRepository.findByEmail(registrationForm.getEmail()) == null) {
            User user = new User();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));

            user.setHash(String.valueOf(user.hashCode()));

            bookstoreUserRepository.save(user);
            return user;
        }

        return null;
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                        payload.getCode()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        BookstoreUserDetails userDetails =
                (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(payload.getContact());

        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public User getCurrentUser(Authentication authentication) {
        User user;

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            user = bookstoreUserRepository.findByEmail(oAuth2User.getAttribute("email"));

        } else {
            BookstoreUserDetails userDetails = (BookstoreUserDetails) authentication.getPrincipal();
            user = userDetails.getBookstoreUser();
        }

        return user;
    }

    public void registerOauthUser(OAuth2User principal) {
        if (!bookstoreUserRepository.existsByEmail(principal.getAttribute("email"))) {
            User user = new User();
            user.setEmail(principal.getAttribute("email"));
            user.setName(principal.getAttribute("name"));
            user.setPhone(principal.getAttribute("phone"));
            user.setHash(String.valueOf(user.hashCode()));

            bookstoreUserRepository.save(user);
        }
    }

    public void editProfile(String token) {
        ProfileResetToken profileResetToken = profileResetTokenRepository.findByToken(token);

        if (LocalDateTime.now().isBefore(profileResetToken.getExpiryDate())) {
            User user = profileResetToken.getUser();

            user.setName(profileResetToken.getName());
            user.setEmail(profileResetToken.getMail());
            user.setPhone(profileResetToken.getPhone());
            if (profileResetToken.getPassword() != null) {
                user.setPassword(profileResetToken.getPassword());
            }
            bookstoreUserRepository.save(user);
        }
    }


    public void sendConfirmationLinkToChangeProfile(ProfileRequest profileRequest, Authentication authentication, HttpServletRequest request) {
        User user = getCurrentUser(authentication);

        String token = UUID.randomUUID().toString();
        createProfileResetToken(user, token, profileRequest);
        javaMailSender.send(constructResetTokenEmail(getAppUrl(request), token, user));

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void createProfileResetToken(User user, String token, ProfileRequest profileRequest) {
        ProfileResetToken myToken = new ProfileResetToken(token, user);
        myToken.setMail(profileRequest.getMail());
        myToken.setPhone(profileRequest.getPhone());
        myToken.setPassword(profileRequest.getPasswordReply());
        myToken.setName(profileRequest.getName());

        profileResetTokenRepository.save(myToken);
    }

    private SimpleMailMessage constructResetTokenEmail(String contextPath, String token, User user) {
        String url = contextPath + "/confirmProfile?token=" + token;
        String message = "Confirm new profile info";
        return constructEmail(message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String body,
                                             User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Profile change confirmation");
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom("schlechtgut@mail.ru");
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }


}
