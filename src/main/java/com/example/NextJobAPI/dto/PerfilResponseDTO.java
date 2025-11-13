package com.example.NextJobAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilResponseDTO {
    
    private Long id;
    private String nomeCompleto;
    private String telefone;
    private String linkedin;
    private String github;
    private String portfolio;
    private String usuarioEmail;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
