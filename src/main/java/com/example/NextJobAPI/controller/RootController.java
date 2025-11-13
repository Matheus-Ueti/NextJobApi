package com.example.NextJobAPI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String home() {
        return "index";
    }
}
