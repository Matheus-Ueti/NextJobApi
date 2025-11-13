package com.example.NextJobAPI.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilRequestDTO {
    
    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;
    
    private String telefone;
    
    private String linkedin;
    
    private String github;
    
    private String portfolio;
}
