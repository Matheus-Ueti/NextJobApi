package com.example.NextJobAPI.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Value("${spring.security.oauth2.client.registration.google.client-id:}")
    private String clientId;
    
    @Value("${spring.security.oauth2.client.registration.google.client-secret:}")
    private String clientSecret;
    
    @GetMapping("/login")
    public String login(@AuthenticationPrincipal OAuth2User principal) {
        // Se já está autenticado, redireciona para home
        if (principal != null) {
            return "redirect:/home";
        }
        // Mostra a página customizada de login
        return "login";
    }
    
    @GetMapping("/diagnostico")
    public String diagnostico(Model model) {
        model.addAttribute("clientId", clientId);
        model.addAttribute("clientSecret", clientSecret);
        return "diagnostico";
    }
      @GetMapping("/plano")
    public String plano(@AuthenticationPrincipal OAuth2User principal, Model model) {
        // Adiciona dados do usuário ao model
        if (principal != null) {
            // GitHub usa 'login' para username e 'avatar_url' para foto
            String name = principal.getAttribute("name");
            if (name == null || name.isBlank()) {
                name = principal.getAttribute("login");
            }
            
            String email = principal.getAttribute("email");
            if (email == null || email.isBlank()) {
                String login = principal.getAttribute("login");
                email = login != null ? login + "@github.com" : "email@exemplo.com";
            }
            
            String picture = principal.getAttribute("avatar_url");
            
            model.addAttribute("userName", name);
            model.addAttribute("userEmail", email);
            model.addAttribute("userPicture", picture);
        }
        return "plano";
    }
}
