package com.example.NextJobAPI.service;

import com.example.NextJobAPI.config.RabbitMQConfiguration;
import com.example.NextJobAPI.dto.AnaliseResponseDTO;
import com.example.NextJobAPI.exception.BusinessException;
import com.example.NextJobAPI.exception.ResourceNotFoundException;
import com.example.NextJobAPI.model.Analise;
import com.example.NextJobAPI.model.Curriculo;
import com.example.NextJobAPI.repository.AnaliseRepository;
import com.example.NextJobAPI.repository.CurriculoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnaliseService {
    
    private final AnaliseRepository analiseRepository;
    private final CurriculoRepository curriculoRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    
    @Transactional
    public AnaliseResponseDTO criarAnaliseAssincrona(Long curriculoId, String userEmail) {
        log.info("Criando análise assíncrona para currículo ID: {}", curriculoId);
        
        Curriculo curriculo = curriculoRepository.findById(curriculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Currículo", "id", curriculoId));
        
        if (!curriculo.getUsuario().getEmail().equals(userEmail)) {
            throw new BusinessException("Você não tem permissão para analisar este currículo");
        }
        
        // Verifica se já existe análise
        Analise analise = analiseRepository.findByCurriculoId(curriculoId)
                .orElseGet(() -> criarNovaAnalise(curriculo));
        
        // Envia para fila RabbitMQ
        rabbitTemplate.convertAndSend(
                RabbitMQConfiguration.ANALISE_EXCHANGE,
                RabbitMQConfiguration.ANALISE_ROUTING_KEY,
                curriculoId
        );
        
        analise.setStatus(Analise.StatusAnalise.PROCESSANDO);
        analise = analiseRepository.save(analise);
        
        return mapToResponseDTO(analise);
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "analises", key = "#curriculoId")
    public AnaliseResponseDTO buscarPorCurriculoId(Long curriculoId, String userEmail) {
        log.info("Buscando análise para currículo ID: {}", curriculoId);
        
        Curriculo curriculo = curriculoRepository.findById(curriculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Currículo", "id", curriculoId));
        
        if (!curriculo.getUsuario().getEmail().equals(userEmail)) {
            throw new BusinessException("Você não tem permissão para acessar esta análise");
        }
        
        Analise analise = analiseRepository.findByCurriculoId(curriculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Análise não encontrada para o currículo ID: " + curriculoId));
        
        return mapToResponseDTO(analise);
    }
    
    @Transactional(readOnly = true)
    public Analise.StatusAnalise verificarStatus(Long curriculoId, String userEmail) {
        Curriculo curriculo = curriculoRepository.findById(curriculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Currículo", "id", curriculoId));
        
        if (!curriculo.getUsuario().getEmail().equals(userEmail)) {
            throw new BusinessException("Você não tem permissão para verificar o status desta análise");
        }
        
        return analiseRepository.findByCurriculoId(curriculoId)
                .map(Analise::getStatus)
                .orElse(Analise.StatusAnalise.PENDENTE);
    }
    
    private Analise criarNovaAnalise(Curriculo curriculo) {
        Analise analise = Analise.builder()
                .curriculo(curriculo)
                .status(Analise.StatusAnalise.PENDENTE)
                .build();
        
        return analiseRepository.save(analise);
    }
    
    private AnaliseResponseDTO mapToResponseDTO(Analise analise) {
        try {
            List<String> pontoFortes = analise.getPontosFortesJson() != null 
                    ? objectMapper.readValue(analise.getPontosFortesJson(), new TypeReference<List<String>>() {})
                    : null;
            
            List<String> pontosMelhoria = analise.getPontosMelhoriaJson() != null
                    ? objectMapper.readValue(analise.getPontosMelhoriaJson(), new TypeReference<List<String>>() {})
                    : null;
            
            List<AnaliseResponseDTO.MatchVaga> matchVagas = analise.getMatchVagasJson() != null
                    ? objectMapper.readValue(analise.getMatchVagasJson(), new TypeReference<List<AnaliseResponseDTO.MatchVaga>>() {})
                    : null;
            
            List<AnaliseResponseDTO.Capacitacao> capacitacoes = analise.getCapacitacoesJson() != null
                    ? objectMapper.readValue(analise.getCapacitacoesJson(), new TypeReference<List<AnaliseResponseDTO.Capacitacao>>() {})
                    : null;
            
            return AnaliseResponseDTO.builder()
                    .id(analise.getId())
                    .curriculoId(analise.getCurriculo().getId())
                    .status(analise.getStatus())
                    .pontoFortes(pontoFortes)
                    .pontosMelhoria(pontosMelhoria)
                    .matchVagas(matchVagas)
                    .capacitacoes(capacitacoes)
                    .mensagemErro(analise.getMensagemErro())
                    .criadoEm(analise.getCriadoEm())
                    .atualizadoEm(analise.getAtualizadoEm())
                    .build();
        } catch (Exception e) {
            log.error("Erro ao mapear análise para DTO: ", e);
            throw new BusinessException("Erro ao processar dados da análise");
        }
    }
}
