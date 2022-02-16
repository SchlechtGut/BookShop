package com.example.MyBookShopApp.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

@Service
public class PostponedAndCartService {

    public void addToCart(String slug, String cartContents, HttpServletResponse response, Model model) {
        if (cartContents == null || cartContents.equals("")) {

            Cookie cookie = new Cookie("cartContents", slug);
            updatePostponedCount(response, model, cookie);

        } else if (!cartContents.contains(slug)) {

            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
            updatePostponedCount(response, model, cookie);
        }
    }



    public void addToPostponedBooks(String slug, String postponedBooks, HttpServletResponse response, Model model) {
        if (postponedBooks == null || postponedBooks.equals("")) {

            Cookie cookie = new Cookie("postponedBooks", slug);
            updateCartContentsCount(response, model, cookie);

        } else if (!postponedBooks.contains(slug)) {

            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(postponedBooks).add(slug);
            Cookie cookie = new Cookie("postponedBooks", stringJoiner.toString());
            updateCartContentsCount(response, model, cookie);
        }
    }

    public void removeBookFromCart(String slug, String cartContents, HttpServletResponse response, Model model) {
        if (cartContents != null && !cartContents.equals("")) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);

            if (cookie.getValue().length() == 0) {
                Cookie numberOfAddedToCart = new Cookie("cartContentsCount", "0");
                numberOfAddedToCart.setPath("/");
                response.addCookie(numberOfAddedToCart);
            } else {
                Cookie numberOfAddedToCart = new Cookie("cartContentsCount", String.valueOf(cookie.getValue().split("/").length));
                numberOfAddedToCart.setPath("/");
                response.addCookie(numberOfAddedToCart);
            }

        } else {
            model.addAttribute("isCartEmpty", true);
        }
    }

    private void updateCartContentsCount(HttpServletResponse response, Model model, Cookie cookie) {
        cookie.setPath("/books");
        response.addCookie(cookie);

        Cookie numberOfAddedToCart = new Cookie("cartContentsCount", String.valueOf(cookie.getValue().split("/").length));
        numberOfAddedToCart.setPath("/");
        response.addCookie(numberOfAddedToCart);

        model.addAttribute("isCartEmpty", false);
    }

    private void updatePostponedCount(HttpServletResponse response, Model model, Cookie cookie) {
        cookie.setPath("/books");
        response.addCookie(cookie);

        Cookie numberOfPostponed = new Cookie("postponedCount", String.valueOf(cookie.getValue().split("/").length));
        numberOfPostponed.setPath("/");
        response.addCookie(numberOfPostponed);

        model.addAttribute("isPostponedEmpty", false);
    }


}
