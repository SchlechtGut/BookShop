package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.service.ApiService;
import com.example.MyBookShopApp.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Controller
public class PaymentController {

    private final PaymentService paymentService;
    private final ApiService apiService;

    public PaymentController(PaymentService paymentService, ApiService apiService) {
        this.paymentService = paymentService;
        this.apiService = apiService;
    }

    @GetMapping("/payment")
    public RedirectView handlePay(@RequestParam Integer sum, @RequestParam String user) throws NoSuchAlgorithmException {
        String paymentUrl = paymentService.getPaymentUrl(sum, user);
        return new RedirectView(paymentUrl);
    }

    @PostMapping("/payment/success")
    public String checkPayment(@RequestParam(required = false, name = "OutSum") Integer sum,
                               @RequestParam(required = false, name = "Shp_hash") String userHash) throws NoSuchAlgorithmException {
        if (sum != null && userHash != null) {
            apiService.putMoney(userHash, sum, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        }
        return "redirect:/my";
    }

}
