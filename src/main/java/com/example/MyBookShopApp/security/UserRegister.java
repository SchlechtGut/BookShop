package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.api.request.ProfileRequest;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.UserRepository;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserRegister {

    private final UserRepository bookstoreUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserRegister(UserRepository bookstoreUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;
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

    public void editProfile(ProfileRequest profileRequest, Authentication authentication) {
        User user = getCurrentUser(authentication);

        if (profileRequest.getPassword().equals(profileRequest.getPasswordReply())) {
            user.setPassword(passwordEncoder.encode(profileRequest.getPassword()));
            bookstoreUserRepository.save(user);
        }
    }
}
