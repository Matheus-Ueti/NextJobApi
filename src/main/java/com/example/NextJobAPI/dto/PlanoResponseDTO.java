package com.example.NextJobAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoResponseDTO {
    
    private Long id;
    private String titulo;
    private String descricao;
    private String categoria;
    private String prioridade;
    private String status;
    private String conteudoGerado;
    private String usuarioEmail;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
