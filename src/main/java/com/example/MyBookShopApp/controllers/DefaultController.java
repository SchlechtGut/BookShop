package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.DTO.SearchWordDto;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class DefaultController {
    @Autowired
    private BookService bookService;

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("cartContentsCount")
    public Integer cartContentsCount(@CookieValue(value = "cartContentsCount", required = false) String cartContents) {
        if (cartContents == null || cartContents.equals("")) {
            return 0;
        }


        return Integer.parseInt(cartContents);
    }

    @ModelAttribute("postponedCount")
    public Integer postponedCount(@CookieValue(value = "postponedCount", required = false) String postponedCount) {
        if (postponedCount == null || postponedCount.equals("")) {
            return 0;
        }

        return Integer.parseInt(postponedCount);
    }


}
