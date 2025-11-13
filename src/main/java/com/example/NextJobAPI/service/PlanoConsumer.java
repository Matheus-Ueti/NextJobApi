package com.example.NextJobAPI.service;

import com.example.NextJobAPI.exception.ResourceNotFoundException;
import com.example.NextJobAPI.model.Plano;
import com.example.NextJobAPI.repository.PlanoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanoConsumer {
    
    private final PlanoRepository planoRepository;
    private final GroqAIService groqAIService;
    private final PlanoService planoService;
    
    @RabbitListener(queues = "plano.processamento")
    public void processarPlano(Long planoId) {
        log.info("Iniciando processamento do plano: {}", planoId);
        
        try {
            Plano plano = planoRepository.findById(planoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + planoId));
            
            // Atualiza status para processando
            plano.setStatus("PROCESSANDO");
            planoRepository.save(plano);
            log.info("Status do plano {} atualizado para PROCESSANDO", planoId);
            
            // Monta o prompt para a IA
            String prompt = construirPrompt(plano);
            
            // Chama o serviço de IA
            log.info("Chamando GroqAI para gerar conteúdo do plano {}", planoId);
            String conteudoGerado = groqAIService.gerarConteudo(prompt);
            
            // Atualiza o plano com o conteúdo gerado
            planoService.atualizarConteudoGerado(planoId, conteudoGerado);
            
            log.info("Processamento do plano {} concluído com sucesso", planoId);
            
        } catch (Exception e) {
            log.error("Erro ao processar plano {}: {}", planoId, e.getMessage(), e);
            
            // Atualiza status para erro
            try {
                Plano plano = planoRepository.findById(planoId).orElse(null);
                if (plano != null) {
                    plano.setStatus("ERRO");
                    plano.setConteudoGerado("Erro ao processar: " + e.getMessage());
                    planoRepository.save(plano);
                }
            } catch (Exception ex) {
                log.error("Erro ao atualizar status de erro do plano {}", planoId, ex);
            }
        }
    }
    
    private String construirPrompt(Plano plano) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Você é um assistente de planejamento de carreira e desenvolvimento profissional.\n\n");
        prompt.append("Crie um plano detalhado baseado nas seguintes informações:\n\n");
        prompt.append("Título: ").append(plano.getTitulo()).append("\n");
        prompt.append("Descrição: ").append(plano.getDescricao()).append("\n");
        
        if (plano.getCategoria() != null) {
            prompt.append("Categoria: ").append(plano.getCategoria()).append("\n");
        }
        
        if (plano.getPrioridade() != null) {
            prompt.append("Prioridade: ").append(plano.getPrioridade()).append("\n");
        }
        
        prompt.append("\nGere um plano de ação detalhado que inclua:\n");
        prompt.append("1. Objetivos específicos e mensuráveis\n");
        prompt.append("2. Etapas de implementação\n");
        prompt.append("3. Recursos necessários\n");
        prompt.append("4. Cronograma sugerido\n");
        prompt.append("5. Indicadores de sucesso\n");
        prompt.append("6. Dicas e recomendações práticas\n\n");
        prompt.append("Seja específico, prático e motivador na sua resposta.");
        
        return prompt.toString();
    }
}
