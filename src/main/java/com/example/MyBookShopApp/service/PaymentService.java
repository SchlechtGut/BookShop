package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.book.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class PaymentService {

    @Value("${robokassa.merchant.login}")
    private String merchantLogin;

    @Value("${robokassa.pass.first.test}")
    private String firstTestPass;


    public String getPaymentUrlFromBooks(List<Book> booksFromCookieSlugs) throws NoSuchAlgorithmException {
        Double paymentSumTotal = booksFromCookieSlugs.stream().mapToDouble(Book::getDiscountPrice).sum();
        MessageDigest md = MessageDigest.getInstance("MD5");
        String invId = "5";
        md.update((merchantLogin + ":" + paymentSumTotal + ":" + invId + ":" + firstTestPass).getBytes());

        return "https://auth.robokassa.ru/Merchant/Index.aspx" +
                "?MerchantLogin=" + merchantLogin +
                "&InvId=" + invId +
                "&Culture=ru" +
                "&Encoding=utf-8" +
                "&OutSum=" + paymentSumTotal +
                "&SignatureValue=" + DatatypeConverter.printHexBinary(md.digest()).toUpperCase() +
                "&IsTest=1";
    }

    public String getPaymentUrl(Integer sum, String user) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        String invId = "5";
        md.update((merchantLogin + ":" + sum + ":" + invId + ":" + firstTestPass).getBytes());

        return "https://auth.robokassa.ru/Merchant/Index.aspx" +
                "?MerchantLogin=" + merchantLogin +
                "&InvId=" + invId +
                "&Culture=ru" +
                "&Encoding=utf-8" +
                "&OutSum=" + sum +
                "&SignatureValue=" + DatatypeConverter.printHexBinary(md.digest()).toUpperCase() +
                "&IsTest=1";
    }
}
