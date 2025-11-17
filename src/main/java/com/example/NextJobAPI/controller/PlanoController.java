package com.example.NextJobAPI.controller;

import com.example.NextJobAPI.dto.PlanoRequestDTO;
import com.example.NextJobAPI.dto.PlanoResponseDTO;
import com.example.NextJobAPI.service.PlanoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planos")
@RequiredArgsConstructor
public class PlanoController {
    
    private final PlanoService planoService;
      @PostMapping
    public ResponseEntity<PlanoResponseDTO> criar(
            @Valid @RequestBody PlanoRequestDTO request,
            @AuthenticationPrincipal OAuth2User principal) {
        
        String email = getEmailFromPrincipal(principal);
        PlanoResponseDTO response = planoService.criarPlano(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<PlanoResponseDTO>> listar(
            @AuthenticationPrincipal OAuth2User principal) {
        
        String email = getEmailFromPrincipal(principal);
        List<PlanoResponseDTO> planos = planoService.listarPlanos(email);
        return ResponseEntity.ok(planos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponseDTO> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal OAuth2User principal) {
        
        String email = getEmailFromPrincipal(principal);
        PlanoResponseDTO plano = planoService.buscarPorId(id, email);
        return ResponseEntity.ok(plano);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PlanoResponseDTO>> listarPorStatus(
            @PathVariable String status,
            @AuthenticationPrincipal OAuth2User principal) {
        
        String email = getEmailFromPrincipal(principal);
        List<PlanoResponseDTO> planos = planoService.listarPorStatus(email, status);
        return ResponseEntity.ok(planos);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PlanoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlanoRequestDTO request,
            @AuthenticationPrincipal OAuth2User principal) {
        
        String email = getEmailFromPrincipal(principal);
        PlanoResponseDTO response = planoService.atualizar(id, request, email);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @AuthenticationPrincipal OAuth2User principal) {
        
        String email = getEmailFromPrincipal(principal);
        planoService.deletar(id, email);
        return ResponseEntity.noContent().build();
    }
    
    private String getEmailFromPrincipal(OAuth2User principal) {
        String email = principal.getAttribute("email");
        if (email == null || email.isBlank()) {
            String login = principal.getAttribute("login");
            email = login != null ? login + "@github.com" : "user@github.com";
        }
        return email;
    }
}
