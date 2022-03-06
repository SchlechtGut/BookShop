package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.PostponedAndCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class PostponedAndCartController extends DefaultController {

    private final BookService bookService;
    private final PostponedAndCartService postponedAndCartService;

    @Autowired
    public PostponedAndCartController(BookService bookService, PostponedAndCartService postponedAndCartService) {
        this.bookService = bookService;
        this.postponedAndCartService = postponedAndCartService;
    }

    @GetMapping("/cart")
    public String cart(@CookieValue(required = false) String cartContents, Model model) {
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isCartEmpty", true);
        } else {
            model.addAttribute("isCartEmpty", false);
            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
            cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
            String[] cookieSlugs = cartContents.split("/");
            List<Book> booksFromCookieSlugs = bookService.findBooksBySlugIn(cookieSlugs);
            model.addAttribute("bookCart", booksFromCookieSlugs);
            model.addAttribute("totalPrice", booksFromCookieSlugs.stream().mapToInt(Book::getDiscountPrice).sum());
            model.addAttribute("totalOldPrice", booksFromCookieSlugs.stream().mapToInt(Book::getPriceOld).sum());
        }

        return "cart";
    }

    @GetMapping("/postponed")
    public String postponed(@CookieValue(required = false) String postponedBooks, Model model) {
        if (postponedBooks == null || postponedBooks.equals("")) {
            model.addAttribute("isPostponedEmpty", true);
        } else {
            model.addAttribute("isPostponedEmpty", false);
            postponedBooks = postponedBooks.startsWith("/") ? postponedBooks.substring(1) : postponedBooks;
            postponedBooks = postponedBooks.endsWith("/") ? postponedBooks.substring(0, postponedBooks.length() - 1) : postponedBooks;
            String[] cookieSlugs = postponedBooks.split("/");
            List<Book> booksFromCookieSlugs = bookService.findBooksBySlugIn(cookieSlugs);
            model.addAttribute("postponedBooks", booksFromCookieSlugs);
            model.addAttribute("totalPrice", booksFromCookieSlugs.stream().mapToInt(Book::getDiscountPrice).sum());
            model.addAttribute("totalOldPrice", booksFromCookieSlugs.stream().mapToInt(Book::getPriceOld).sum());
        }

        System.out.println("before");

        return "postponed";
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable String slug, @CookieValue(required = false) String cartContents,
                                                  HttpServletResponse response, Model model) {
        postponedAndCartService.removeBookFromCart(slug, cartContents, response, model);

        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/postponed/remove/{slug}")
    public String handleRemoveBookFromPostponedRequest(@PathVariable String slug, @CookieValue(required = false) String postponedBooks,
                                                  HttpServletResponse response, Model model) {
        postponedAndCartService.removeBookFromPostponed(slug, postponedBooks, response, model);

        return "redirect:/books/postponed";
    }



    @PostMapping("/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@RequestParam String status , @PathVariable String slug,
                                         @CookieValue(required = false) String cartContents,
                                         @CookieValue(required = false) String postponedBooks,
                                         HttpServletResponse response, Model model) {

        if (cartContents == null) cartContents = "";
        if (postponedBooks == null) postponedBooks = "";

        if (!cartContents.contains(slug) && !postponedBooks.contains(slug)) {
            if (status.equals("KEPT")) {
                postponedAndCartService.addToPostponedBooks(slug, postponedBooks, response, model);
            } else if (status.equals("CART")) {
                postponedAndCartService.addToCart(slug, cartContents, response, model);
            }
        } else if (postponedBooks.contains(slug)) {
            if (status.equals("CART")) {
                postponedAndCartService.addToCart(slug, cartContents, response, model);
            }
            postponedAndCartService.removeBookFromPostponed(slug, postponedBooks, response, model);
        } else if (cartContents.contains(slug)) {
            if (status.equals("KEPT")) {
                postponedAndCartService.addToPostponedBooks(slug, postponedBooks, response, model);
                postponedAndCartService.removeBookFromCart(slug, cartContents, response, model);
            } else {
                return "redirect:/books/cart";
            }
        }

        return ("redirect:/books/" + slug);
    }


}
