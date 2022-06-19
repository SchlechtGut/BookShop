package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.BalanceTransactionRepository;
import com.example.MyBookShopApp.repository.Book2UserRepository;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.repository.UserRepository;
import com.example.MyBookShopApp.security.UserRegister;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostponedAndCartService {

    private final BookRepository bookRepository;
    private final UserRegister userRegister;
    private final BalanceTransactionRepository balanceTransactionRepository;
    private final Book2UserRepository book2UserRepository;
    private final UserRepository userRepository;


    public PostponedAndCartService(BookRepository bookRepository, UserRegister userRegister, BalanceTransactionRepository balanceTransactionRepository, Book2UserRepository book2UserRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRegister = userRegister;
        this.balanceTransactionRepository = balanceTransactionRepository;
        this.book2UserRepository = book2UserRepository;
        this.userRepository = userRepository;
    }


    public void addToCart(String slug, String cartContents, HttpServletResponse response, Model model) {
        if (cartContents == null || cartContents.equals("")) {
            Cookie cookie = new Cookie("cartContents", slug);
            updateCartContentsCount(response, model, cookie);

        } else if (!cartContents.contains(slug)) {

            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
            updateCartContentsCount(response, model, cookie);
        }
    }

    public void addToPostponedBooks(String slug, String postponedBooks, HttpServletResponse response, Model model) {
        if (postponedBooks == null || postponedBooks.equals("")) {
            Cookie cookie = new Cookie("postponedBooks", slug);
            updatePostponedCount(response, model, cookie);

        } else if (!postponedBooks.contains(slug)) {

            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(postponedBooks).add(slug);
            Cookie cookie = new Cookie("postponedBooks", stringJoiner.toString());
            updatePostponedCount(response, model, cookie);
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

    public void removeBookFromPostponed(String slug, String postponedBooks, HttpServletResponse response, Model model) {
        if (postponedBooks != null && !postponedBooks.equals("")) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(postponedBooks.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("postponedBooks", String.join("/", cookieBooks));
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);

            if (cookie.getValue().length() == 0) {
                Cookie numberOfAddedToCart = new Cookie("postponedCount", "0");
                numberOfAddedToCart.setPath("/");
                response.addCookie(numberOfAddedToCart);
            } else {
                Cookie numberOfAddedToCart = new Cookie("postponedCount", String.valueOf(cookie.getValue().split("/").length));
                numberOfAddedToCart.setPath("/");
                response.addCookie(numberOfAddedToCart);
            }

        } else {
            model.addAttribute("isPostponedEmpty", true);
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

    public String buyBooks(String cartContents, HttpServletResponse response, Model model, Authentication authentication) {
        cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
        cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
        String[] cookieSlugs = cartContents.split("/");
        List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlugs);

        double paymentSumTotal = booksFromCookieSlugs.stream().mapToDouble(Book::getDiscountPrice).sum();
        User user;
        try {
            user = userRegister.getCurrentUser(authentication);
        } catch (Exception e){
            return "redirect:/signin";
        }
        int balance = user.getBalance();

        if (balance < paymentSumTotal){
            model.addAttribute("notEnough", true);
            return "redirect:/cart";
        }



        List <BalanceTransactionEntity> transactions = new ArrayList<>();
        List <Book2UserEntity> book2UserEntities = new ArrayList<>();
        for (Book book : booksFromCookieSlugs) {
            double price = book.getDiscountPrice().doubleValue();
            BalanceTransactionEntity transaction = new BalanceTransactionEntity();

            transaction.setBookId(book.getId());
            transaction.setValue((int) -price);
            transaction.setUserId(user.getId());
            transaction.setDescription("Покупка " + book.getTitle());
            transaction.setTime(LocalDateTime.now());

            Book2UserEntity book2UserEntity = new Book2UserEntity();
            book2UserEntity.setBookId(book.getId());
            book2UserEntity.setUserId(user.getId());
            book2UserEntity.setTime(LocalDateTime.now());
            book2UserEntity.setTypeId(3);

            transactions.add(transaction);
            book2UserEntities.add(book2UserEntity);
        }

        user.setBalance((int) (balance - paymentSumTotal));

        balanceTransactionRepository.saveAll(transactions);
        book2UserRepository.saveAll(book2UserEntities);
        userRepository.save(user);


        response.addCookie(new Cookie("cartContents", ""));
        model.addAttribute("success", true);
        model.addAttribute("isCartEmpty", true);
        model.addAttribute("cartContentsCount", 0);


        return "redirect:/cart";
    }


}
