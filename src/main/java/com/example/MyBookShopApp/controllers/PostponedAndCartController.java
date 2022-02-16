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

    @ModelAttribute(name = "bookCart")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {
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

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug, @CookieValue(name =
            "cartContents", required = false) String cartContents, HttpServletResponse response, Model model) {
        postponedAndCartService.removeBookFromCart(slug, cartContents, response, model);

        return "redirect:/books/cart";
    }



    @PostMapping("/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug, @CookieValue(name = "cartContents",
            required = false) String cartContents, @CookieValue(name = "postponedBooks", required = false) String postponedBooks,
                                         HttpServletResponse response, Model model) {

        postponedAndCartService.addToCart(slug, cartContents, response, model);
        postponedAndCartService.addToPostponedBooks(slug, postponedBooks, response, model);

        return ("redirect:/books/" + slug);
    }


}
