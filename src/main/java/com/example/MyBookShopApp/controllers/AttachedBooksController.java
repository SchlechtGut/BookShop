package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.response.SuccessResponse;
import com.example.MyBookShopApp.data.enums.Book2UserType;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.UserRepository;
import com.example.MyBookShopApp.security.UserRegister;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.PaymentService;
import com.example.MyBookShopApp.service.AttachedBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AttachedBooksController extends DefaultController {

    private final BookService bookService;
    private final AttachedBooksService attachedBooksService;
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    @Autowired
    public AttachedBooksController(UserRegister userRegister, BookService bookService, AttachedBooksService attachedBooksService, PaymentService paymentService, UserRepository userRepository) {
        super(userRegister);
        this.bookService = bookService;
        this.attachedBooksService = attachedBooksService;
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }

    @GetMapping("/books/cart")
    public String cart(Model model, HttpServletRequest request, Authentication authentication) {
        attachedBooksService.showInnerContent(model, request, authentication, Book2UserType.CART);
        return "cart";
    }

    @GetMapping("/books/postponed")
    public String postponed(Model model, HttpServletRequest request, Authentication authentication) {
        attachedBooksService.showInnerContent(model, request, authentication, Book2UserType.KEPT);
        return "postponed";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/books/changeBookStatus/my/archive/{slug}")
    public String handelArchiving(@PathVariable String slug, Authentication authentication) {
        attachedBooksService.archiveBook(slug, authentication);
        return "redirect:/books/postponed";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/books/changeBookStatus/my/dearchive/{slug}")
    public String handelDeArchiving(@PathVariable String slug, Authentication authentication) {
        attachedBooksService.deArchiveBook(slug, authentication);
        return "redirect:/books/postponed";
    }

    @PostMapping("/api/changeBookStatus")
    @ResponseBody
    public SuccessResponse handleChangeBookStatus(@RequestParam String status, @RequestParam String booksIds,
                                                  HttpServletRequest request, Authentication authentication) {
        String error = null;

        String[] ids = booksIds.split(",");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("empty_user");

        if (authentication != null) {
            user = userRegister.getCurrentUser(authentication);

        } else if (user == null) {
            System.out.println("why!!!!!");
            user = new User();
            user.setRegTime(LocalDateTime.now());
            userRepository.save(user);

            session.setAttribute("empty_user", user);
        }

        if (status.equals("KEPT") || status.equals("CART")) {
             error = attachedBooksService.addToPostponedOrCart(user, ids, status);
        } else if (status.equals("UNLINK")) {
            attachedBooksService.removeFromCartPostponed(ids[0], request, authentication);
        }

        if (error == null) {
            return new SuccessResponse(true);
        } else {
            return new SuccessResponse(error);
        }
    }

    @GetMapping("/books/buy")
    @PreAuthorize("isAuthenticated()")
    public String handleBuy(@CookieValue(required = false) String cartContents, HttpServletResponse response, Model model, Authentication authentication) {
        System.out.println(cartContents);
        attachedBooksService.buyBooks(cartContents, response, model, authentication);
        return "redirect:/books/cart";
    }


}
