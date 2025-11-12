package com.example.NextJobAPI.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.NextJobAPI.model.Analise.StatusAnalise;

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
public class AnaliseResponseDTO {
    
    private Long id;
    private Long curriculoId;
    private StatusAnalise status;
    private List<String> pontoFortes;
    private List<String> pontosMelhoria;
    private List<MatchVaga> matchVagas;
    private List<Capacitacao> capacitacoes;
    private String mensagemErro;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MatchVaga {
        private String tituloVaga;
        private Integer percentualMatch;
        private String descricao;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Capacitacao {
        private String titulo;
        private String plataforma;
        private String url;
        private String duracao;
    }
}
