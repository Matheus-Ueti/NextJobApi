package com.example.NextJobAPI.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NextJobAPI.dto.AnaliseResponseDTO;
import com.example.NextJobAPI.model.Analise.StatusAnalise;
import com.example.NextJobAPI.service.AnaliseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analises")
@RequiredArgsConstructor
public class AnaliseController {
    
    private final AnaliseService analiseService;
    
    @PostMapping("/curriculo/{curriculoId}")
    public ResponseEntity<AnaliseResponseDTO> criar(
            @PathVariable Long curriculoId,
            @RequestHeader("X-User-Email") String userEmail) {
        
        AnaliseResponseDTO response = analiseService.criarAnaliseAssincrona(curriculoId, userEmail);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
    
    @GetMapping("/curriculo/{curriculoId}")
    public ResponseEntity<AnaliseResponseDTO> buscarPorCurriculoId(
            @PathVariable Long curriculoId,
            @RequestHeader("X-User-Email") String userEmail) {
        
        AnaliseResponseDTO response = analiseService.buscarPorCurriculoId(curriculoId, userEmail);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/curriculo/{curriculoId}/status")
    public ResponseEntity<Map<String, String>> verificarStatus(
            @PathVariable Long curriculoId,
            @RequestHeader("X-User-Email") String userEmail) {
        
        StatusAnalise status = analiseService.verificarStatus(curriculoId, userEmail);
        return ResponseEntity.ok(Map.of("status", status.name()));
    }
}
