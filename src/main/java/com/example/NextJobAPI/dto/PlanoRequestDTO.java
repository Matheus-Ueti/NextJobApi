package com.example.NextJobAPI.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoRequestDTO {
    
    @NotBlank(message = "Título do plano é obrigatório")
    private String titulo;
    
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
    
    private String categoria;
    
    private String prioridade;
    
    private String status;
}
