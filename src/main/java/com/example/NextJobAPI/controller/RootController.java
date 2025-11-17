package com.example.NextJobAPI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    
    @GetMapping("/")
    public String root() {
        // Redireciona para home, que exigirá autenticação
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String home() {
        // Exige autenticação - se não estiver logado, redireciona para /login
        return "index";
    }
}
