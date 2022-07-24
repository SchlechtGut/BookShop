package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.DTO.SearchWordDto;
import com.example.MyBookShopApp.data.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.enums.Book2UserType;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.security.UserRegister;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class DefaultController {

    final UserRegister userRegister;

    public DefaultController(UserRegister userRegister) {
        this.userRegister = userRegister;
    }


    @ModelAttribute("authenticated")
    public Boolean isAuthenticated(Authentication authentication) {
        return authentication != null;
    }

    @ModelAttribute("curUsr")
    public User currentUser(Authentication authentication) {
        if (authentication != null) {
            return userRegister.getCurrentUser(authentication);
        }

        return null;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute
    public void countDisplay(HttpServletRequest request, Authentication authentication, Model model) {
        HttpSession session = request.getSession();
        User emptyUser = (User) session.getAttribute("empty_user");

        long keptCount = 0;
        long cartCount = 0;

        if (authentication != null) {
            List<Book2UserEntity> list = userRegister.getCurrentUser(authentication).getBook2UserEntities();

            keptCount = list.stream().filter(x -> x.getType().equals(Book2UserType.KEPT)).count();
            cartCount = list.stream().filter(x -> x.getType().equals(Book2UserType.CART)).count();

        } else if (emptyUser != null) {
            Optional<User> optional = userRegister.getUserById(emptyUser.getId());

            if (optional.isPresent()) {
                keptCount = optional.get().getBook2UserEntities().stream().filter(x -> x.getType().equals(Book2UserType.KEPT)).count();
                cartCount = optional.get().getBook2UserEntities().stream().filter(x -> x.getType().equals(Book2UserType.CART)).count();
            }
        }

        model.addAttribute("postponedCount", keptCount);
        model.addAttribute("cartContentsCount", cartCount);
    }
}
