package com.example.NextJobAPI.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    
    @GetMapping("/")
    public String root() {
        // Redireciona para home, que exigirá autenticação
        return "redirect:/home";
    }
      @GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        // Adiciona dados do usuário ao model
        if (principal != null) {
            // GitHub usa 'login' para username e 'avatar_url' para foto
            String name = principal.getAttribute("name");
            if (name == null || name.isBlank()) {
                name = principal.getAttribute("login"); // fallback para login do GitHub
            }
            
            String email = principal.getAttribute("email");
            if (email == null || email.isBlank()) {
                String login = principal.getAttribute("login");
                email = login != null ? login + "@github.com" : "email@exemplo.com";
            }
            
            String picture = principal.getAttribute("avatar_url"); // GitHub usa avatar_url
            
            model.addAttribute("userName", name);
            model.addAttribute("userEmail", email);
            model.addAttribute("userPicture", picture);
        }
        return "index";
    }
}
