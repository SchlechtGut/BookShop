package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.NoSuchAlgorithmException;

@Controller
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payment")
    public RedirectView handlePay(@RequestParam Integer sum, @RequestParam String user) throws NoSuchAlgorithmException {
        String paymentUrl = paymentService.getPaymentUrl(sum, user);
        return new RedirectView(paymentUrl);
    }
}
