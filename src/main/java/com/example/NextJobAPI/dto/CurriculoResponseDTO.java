package com.example.NextJobAPI.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurriculoResponseDTO {
    
    private Long id;
    private String nome;
    private String cargoAtual;
    private String cargoDesejado;
    private String habilidades;
    private String experiencia;
    private String educacao;
    private String pdfUrl;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
