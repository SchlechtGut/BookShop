package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.data.book.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DefaultController {
    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }


}
