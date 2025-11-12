package com.example.NextJobAPI.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "analises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Analise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculo_id", nullable = false, unique = true)
    private Curriculo curriculo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAnalise status;
    
    @Column(columnDefinition = "TEXT")
    private String pontosFortesJson;
    
    @Column(columnDefinition = "TEXT")
    private String pontosMelhoriaJson;
    
    @Column(columnDefinition = "TEXT")
    private String matchVagasJson;
    
    @Column(columnDefinition = "TEXT")
    private String capacitacoesJson;
    
    @Column(columnDefinition = "TEXT")
    private String mensagemErro;
    
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    public enum StatusAnalise {
        PENDENTE,
        PROCESSANDO,
        CONCLUIDA,
        ERRO
    }
    
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
        if (status == null) {
            status = StatusAnalise.PENDENTE;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
