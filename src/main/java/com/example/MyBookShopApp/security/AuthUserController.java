package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.api.request.ProfileRequest;
import com.example.MyBookShopApp.controllers.DefaultController;
import com.example.MyBookShopApp.data.SmsCode;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.BalanceTransactionRepository;
import com.example.MyBookShopApp.service.ApiService;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AuthUserController extends DefaultController {

    private final UserRegister userRegister;
    private final SmsService smsService;
    private final JavaMailSender javaMailSender;
    private final ApiService apiService;
    private final BalanceTransactionRepository balanceTransactionRepository;
    private final BookService bookService;

    @Autowired
    public AuthUserController(UserRegister userRegister, SmsService smsService, JavaMailSender javaMailSender, ApiService apiService, BalanceTransactionRepository balanceTransactionRepository, BookService bookService) {
        this.userRegister = userRegister;
        this.smsService = smsService;
        this.javaMailSender = javaMailSender;
        this.apiService = apiService;
        this.balanceTransactionRepository = balanceTransactionRepository;
        this.bookService = bookService;
    }

    @GetMapping("/signin")
    public String handleSignin() {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload contactConfirmationPayload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/requestEmailConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestEmailConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("schlechtgut@mail.ru");
        message.setTo(payload.getContact());
        SmsCode smsCode = new SmsCode(smsService.generateCode(),300); //5 minutes
        smsService.saveNewCode(smsCode);
        message.setSubject("Bookstore email verification!");
        message.setText("Verification code is: " + smsCode.getCode());
        javaMailSender.send(message);
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();

        if (!payload.getContact().contains("@")) {
            response.setResult("true");
            return response;
        }

        if(smsService.verifyCode(payload.getCode())){
            response.setResult("true");
        }

        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        userRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "redirect:/signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }

    @GetMapping("/my")
    public String handleMy(Model model, Authentication authentication) {
        User curUser = userRegister.getCurrentUser(authentication);
        List<Book2UserEntity> book2UserEntities = curUser.getBook2UserEntities();
        List<Book> boughtBooks = bookService.findBooksByIdIn(book2UserEntities.stream().map(Book2UserEntity::getBookId).collect(Collectors.toList()));



        model.addAttribute("curUsr", curUser);
        model.addAttribute("boughtBooks", boughtBooks);
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model, Authentication authentication) {
        User curUser = userRegister.getCurrentUser(authentication);
        model.addAttribute("curUsr", curUser);
        model.addAttribute("transactions", balanceTransactionRepository.findByUserId(curUser.getId()));
        return "profile";
    }

    @GetMapping("/oauth-success-login")
    public String oauthSuccess(OAuth2AuthenticationToken auth2AuthenticationToken) {
        userRegister.registerOauthUser(auth2AuthenticationToken.getPrincipal());
        return "redirect:/my";
    }

    @GetMapping("/authorities")
    @ResponseBody
    public Map<String,Object> getPrincipalInfo(OAuth2AuthenticationToken principal) {

        Collection<String> authorities = principal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String,Object> info = new HashMap<>();
        info.put("name", principal.getName());
        info.put("authorities", authorities);
        info.put("tokenAttributes", principal.getPrincipal().getAttributes());

        return info;
    }

    @PostMapping(value = "/editProfile")
    public String resetProfile(@RequestParam String name, @RequestParam String mail,
                              @RequestParam String phone, @RequestParam String password,
                              @RequestParam String passwordReply, Authentication authentication, HttpServletRequest request) {
        ProfileRequest profileRequest = new ProfileRequest(name, mail, phone, password, passwordReply);

        userRegister.sendConfirmationLinkToChangeProfile(profileRequest, authentication, request);

        return "redirect:/profile";
    }

    @GetMapping(value = "/confirmProfile")
    public String editProfile(@RequestParam String token) {
        userRegister.editProfile(token);
        return "redirect:/profile";
    }




//    @GetMapping("/logout")
//    public String handleLogout(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        SecurityContextHolder.clearContext();
//        if (session != null) {
//            session.invalidate();
//        }
//
//        for (Cookie cookie : request.getCookies()) {
//            cookie.setMaxAge(0);
//        }
//
//        return "redirect:/";
//    }
}
