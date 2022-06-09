package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.api.request.ProfileRequest;
import com.example.MyBookShopApp.controllers.DefaultController;
import com.example.MyBookShopApp.data.SmsCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AuthUserController extends DefaultController {

    private final UserRegister userRegister;
    private final SmsService smsService;
    private final JavaMailSender javaMailSender;

    @Autowired
    public AuthUserController(UserRegister userRegister, SmsService smsService, JavaMailSender javaMailSender) {
        this.userRegister = userRegister;
        this.smsService = smsService;
        this.javaMailSender = javaMailSender;
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
        model.addAttribute("curUsr", userRegister.getCurrentUser(authentication));
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model, Authentication authentication) {
        model.addAttribute("curUsr", userRegister.getCurrentUser(authentication));
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
    public String editProfile(@RequestBody ProfileRequest profileRequest, Authentication authentication) {
        System.out.println(profileRequest);

        userRegister.editProfile(profileRequest, authentication);

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
