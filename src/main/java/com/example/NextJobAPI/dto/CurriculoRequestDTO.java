package com.example.NextJobAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class CurriculoRequestDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @Size(max = 100, message = "Cargo atual deve ter no máximo 100 caracteres")
    private String cargoAtual;
    
    @Size(max = 100, message = "Cargo desejado deve ter no máximo 100 caracteres")
    private String cargoDesejado;
    
    private String habilidades;
    
    private String experiencia;
    
    private String educacao;
}
