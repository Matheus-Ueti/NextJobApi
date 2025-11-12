package com.example.NextJobAPI.controller;

import com.example.NextJobAPI.dto.CurriculoRequestDTO;
import com.example.NextJobAPI.dto.CurriculoResponseDTO;
import com.example.NextJobAPI.service.CurriculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/curriculos")
@RequiredArgsConstructor
public class CurriculoController {
    
    private final CurriculoService curriculoService;
    
    @PostMapping
    public ResponseEntity<CurriculoResponseDTO> criar(
            @Valid @RequestBody CurriculoRequestDTO request,
            @RequestHeader("X-User-Email") String userEmail) {
        
        CurriculoResponseDTO response = curriculoService.criar(request, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CurriculoResponseDTO> buscarPorId(
            @PathVariable Long id,
            @RequestHeader("X-User-Email") String userEmail) {
        
        CurriculoResponseDTO response = curriculoService.buscarPorId(id, userEmail);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<Page<CurriculoResponseDTO>> listar(
            @RequestHeader("X-User-Email") String userEmail,
            @PageableDefault(size = 10, sort = "criadoEm", direction = Sort.Direction.DESC) Pageable pageable) {
        
        Page<CurriculoResponseDTO> response = curriculoService.listarPorUsuario(userEmail, pageable);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CurriculoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CurriculoRequestDTO request,
            @RequestHeader("X-User-Email") String userEmail) {
        
        CurriculoResponseDTO response = curriculoService.atualizar(id, request, userEmail);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @RequestHeader("X-User-Email") String userEmail) {
        
        curriculoService.deletar(id, userEmail);
        return ResponseEntity.noContent().build();
    }
}
