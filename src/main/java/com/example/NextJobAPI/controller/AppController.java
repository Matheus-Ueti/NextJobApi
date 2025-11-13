package com.example.NextJobAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AppController {
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
                "nome", "NextJob API",
                "versao", "1.0.0",
                "descricao", "API de Recrutamento Inteligente com IA",
                "timestamp", LocalDateTime.now(),
                "endpoints", Map.of(
                        "curriculos", "/api/curriculos",
                        "analises", "/api/analises",
                        "health", "/actuator/health",
                        "metrics", "/actuator/metrics"
                )
        ));
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
