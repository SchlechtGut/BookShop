package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.request.FeedbackRequest;
import com.example.MyBookShopApp.data.genre.Genre;
import com.example.MyBookShopApp.security.UserRegister;
import com.example.MyBookShopApp.service.GenreService;
import com.example.MyBookShopApp.service.ToolsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class ToolsController extends DefaultController {

    private final GenreService genreService;
    private final ToolsService toolsService;

    public ToolsController(UserRegister userRegister, GenreService genreService, ToolsService toolsService) {
        super(userRegister);
        this.genreService = genreService;
        this.toolsService = toolsService;
    }

    @GetMapping("/documents")
    public String documents(){
        return "/documents/index";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/faq")
    public String faq(){
        return "faq";
    }

    @GetMapping("/contacts")
    public String contacts(Authentication authentication, Model model) {
        if (userRegister.getCurrentUser(authentication) != null) {
            model.addAttribute("isAuthenticated", true);
        }

        return "contacts";
    }

    @PostMapping("/contacts")
    public String saveFeedBack(Authentication authentication, @ModelAttribute FeedbackRequest feedbackRequest) {
        toolsService.saveFeedBack(userRegister.getCurrentUser(authentication), feedbackRequest);
        return "redirect:/contacts";
    }

    @GetMapping("/api/genre")
    @ResponseBody
    public List<Genre> allGenres() {
        return genreService.getAllGenres();
    }
}
