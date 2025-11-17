package com.example.NextJobAPI.controller;

import com.example.NextJobAPI.dto.PerfilRequestDTO;
import com.example.NextJobAPI.dto.PerfilResponseDTO;
import com.example.NextJobAPI.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfis")
@RequiredArgsConstructor
public class PerfilController {
    
    private final PerfilService perfilService;
    
    @PostMapping
    public ResponseEntity<PerfilResponseDTO> criar(
            @Valid @RequestBody PerfilRequestDTO request,
            @AuthenticationPrincipal OAuth2User principal) {
        
        String email = getEmailFromPrincipal(principal);
        PerfilResponseDTO response = perfilService.criarPerfil(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<PerfilResponseDTO>> listar(
            @AuthenticationPrincipal OAuth2User principal) {
        
        String email = getEmailFromPrincipal(principal);
        List<PerfilResponseDTO> perfis = perfilService.listarPerfis(email);
        return ResponseEntity.ok(perfis);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal OAuth2User principal) {
        
        String email = getEmailFromPrincipal(principal);
        PerfilResponseDTO perfil = perfilService.buscarPorId(id, email);
        return ResponseEntity.ok(perfil);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PerfilRequestDTO request,
            @AuthenticationPrincipal OAuth2User principal) {
        
        String email = getEmailFromPrincipal(principal);
        PerfilResponseDTO response = perfilService.atualizar(id, request, email);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @AuthenticationPrincipal OAuth2User principal) {
        
        String email = getEmailFromPrincipal(principal);
        perfilService.deletar(id, email);
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
