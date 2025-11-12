package com.example.NextJobAPI.service;

import com.example.NextJobAPI.config.RabbitMQConfiguration;
import com.example.NextJobAPI.exception.ResourceNotFoundException;
import com.example.NextJobAPI.model.Analise;
import com.example.NextJobAPI.model.Curriculo;
import com.example.NextJobAPI.repository.AnaliseRepository;
import com.example.NextJobAPI.repository.CurriculoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnaliseConsumer {
    
    private final CurriculoRepository curriculoRepository;
    private final AnaliseRepository analiseRepository;
    private final GroqAIService groqAIService;
    private final ObjectMapper objectMapper;
    
    @RabbitListener(queues = RabbitMQConfiguration.ANALISE_QUEUE)
    public void processarAnalise(Long curriculoId) {
        log.info("Processando análise para currículo ID: {}", curriculoId);
        
        try {
            Curriculo curriculo = curriculoRepository.findById(curriculoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Currículo", "id", curriculoId));
            
            Analise analise = analiseRepository.findByCurriculoId(curriculoId)
                    .orElseGet(() -> criarNovaAnalise(curriculo));
            
            analise.setStatus(Analise.StatusAnalise.PROCESSANDO);
            analiseRepository.save(analise);
            
            // Chama Groq AI
            String resultadoJson = groqAIService.analisarCurriculo(curriculo);
            
            // Processa resultado
            JsonNode root = objectMapper.readTree(resultadoJson);
            
            analise.setPontosFortesJson(root.path("pontoFortes").toString());
            analise.setPontosMelhoriaJson(root.path("pontosMelhoria").toString());
            analise.setMatchVagasJson(root.path("matchVagas").toString());
            analise.setCapacitacoesJson(root.path("capacitacoes").toString());
            analise.setStatus(Analise.StatusAnalise.CONCLUIDA);
            
            analiseRepository.save(analise);
            
            log.info("Análise concluída com sucesso para currículo ID: {}", curriculoId);
            
        } catch (Exception e) {
            log.error("Erro ao processar análise para currículo ID: {}", curriculoId, e);
            
            analiseRepository.findByCurriculoId(curriculoId).ifPresent(analise -> {
                analise.setStatus(Analise.StatusAnalise.ERRO);
                analise.setMensagemErro(e.getMessage());
                analiseRepository.save(analise);
            });
        }
    }
    
    private Analise criarNovaAnalise(Curriculo curriculo) {
        Analise analise = Analise.builder()
                .curriculo(curriculo)
                .status(Analise.StatusAnalise.PENDENTE)
                .build();
        
        return analiseRepository.save(analise);
    }
}
