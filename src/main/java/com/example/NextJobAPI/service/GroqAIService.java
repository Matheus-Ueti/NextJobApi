package com.example.NextJobAPI.service;

import com.example.NextJobAPI.exception.BusinessException;
import com.example.NextJobAPI.model.Curriculo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GroqAIService {
    
    @Value("${groq.api.key:}")
    private String groqApiKey;
    
    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String groqApiUrl;
    
    @Value("${groq.model:llama-3.3-70b-versatile}")
    private String groqModel;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public GroqAIService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    public String analisarCurriculo(Curriculo curriculo) {
        log.info("Analisando currículo ID: {} com Groq AI", curriculo.getId());
        
        if (groqApiKey == null || groqApiKey.isEmpty()) {
            throw new BusinessException("Groq API Key não configurada");
        }
        
        String prompt = construirPrompt(curriculo);
        
        try {
            String response = chamarGroqAPI(prompt);
            return extrairConteudoJSON(response);
        } catch (Exception e) {
            log.error("Erro ao chamar Groq API: ", e);
            throw new BusinessException("Erro ao processar análise com IA", e);
        }
    }
    
    private String construirPrompt(Curriculo curriculo) {
        return String.format("""
            Você é um especialista em recrutamento e análise de currículos.
            
            Analise o seguinte currículo e forneça uma avaliação completa:
            
            Nome: %s
            Cargo Atual: %s
            Cargo Desejado: %s
            Habilidades: %s
            Experiência: %s
            Educação: %s
            
            Retorne a análise em formato JSON com a estrutura exata:
            {
                "pontoFortes": ["ponto 1", "ponto 2", "ponto 3"],
                "pontosMelhoria": ["ponto 1", "ponto 2", "ponto 3"],
                "matchVagas": [
                    {
                        "tituloVaga": "título da vaga",
                        "percentualMatch": 85,
                        "descricao": "descrição do match"
                    }
                ],
                "capacitacoes": [
                    {
                        "titulo": "Curso de...",
                        "plataforma": "Coursera",
                        "url": "https://coursera.org/...",
                        "duracao": "4 semanas"
                    }
                ]
            }
            
            Seja objetivo e específico nas recomendações.
            """,
            curriculo.getNome(),
            curriculo.getCargoAtual(),
            curriculo.getCargoDesejado(),
            curriculo.getHabilidades(),
            curriculo.getExperiencia(),
            curriculo.getEducacao()
        );
    }
    
    private String chamarGroqAPI(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", groqModel);
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "Você é um especialista em análise de currículos e recrutamento."),
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
                groqApiUrl,
                HttpMethod.POST,
                request,
                String.class
        );
        
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new BusinessException("Erro na resposta da Groq API: " + response.getStatusCode());
        }
        
        return response.getBody();
    }
    
    private String extrairConteudoJSON(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            
            // Remove markdown code blocks se existirem
            content = content.replaceAll("```json\\n?", "").replaceAll("```\\n?", "").trim();
            
            return content;
        } catch (Exception e) {
            log.error("Erro ao extrair JSON da resposta: ", e);
            throw new BusinessException("Erro ao processar resposta da IA");
        }
    }
    
    public String gerarConteudo(String prompt) {
        log.info("Gerando conteúdo com Groq AI");
        
        if (groqApiKey == null || groqApiKey.isEmpty()) {
            throw new BusinessException("Groq API Key não configurada");
        }
        
        try {
            String response = chamarGroqAPI(prompt);
            return extrairTexto(response);
        } catch (Exception e) {
            log.error("Erro ao gerar conteúdo com Groq API: ", e);
            throw new BusinessException("Erro ao gerar conteúdo com IA", e);
        }
    }
    
    private String extrairTexto(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            return content.trim();
        } catch (Exception e) {
            log.error("Erro ao extrair texto da resposta: ", e);
            throw new BusinessException("Erro ao processar resposta da IA");
        }
    }
    
    public String gerarPlanoCarreira(String titulo, String curriculo) {
        log.info("Gerando plano de carreira com Groq AI para: {}", titulo);
        
        if (groqApiKey == null || groqApiKey.isEmpty()) {
            throw new BusinessException("Groq API Key não configurada");
        }
        
        String prompt = criarPromptPlanoCarreira(titulo, curriculo);
        
        try {
            String response = chamarGroqAPI(prompt);
            return extrairTexto(response);
        } catch (Exception e) {
            log.error("Erro ao gerar plano de carreira com Groq API: ", e);
            throw new BusinessException("Erro ao gerar plano de carreira com IA", e);
        }
    }
    
    private String criarPromptPlanoCarreira(String titulo, String curriculo) {
        return String.format("""
            Você é um especialista em desenvolvimento de carreira e orientação profissional.
            
            Com base no seguinte currículo, crie um plano de carreira detalhado e personalizado para: "%s"
            
            CURRÍCULO:
            %s
            
            Por favor, forneça um plano de carreira estruturado e bem formatado em Markdown com:
            
            1. **📊 Análise do Perfil Atual**
               - Pontos fortes identificados
               - Áreas de melhoria
               - Competências técnicas presentes
            
            2. **🎯 Objetivos de Carreira**
               - **Curto Prazo (6 meses)**: metas imediatas e alcançáveis
               - **Médio Prazo (1-2 anos)**: objetivos de consolidação
               - **Longo Prazo (3-5 anos)**: visão de futuro profissional
            
            3. **📚 Plano de Desenvolvimento**
               - Cursos e certificações recomendados (com nomes específicos)
               - Tecnologias para aprender (liste as mais relevantes)
               - Soft skills a desenvolver
            
            4. **🚀 Estratégias de Crescimento**
               - Oportunidades de networking
               - Projetos práticos sugeridos
               - Áreas de especialização recomendadas
            
            5. **✅ Próximos Passos Imediatos**
               - Liste 5 ações concretas e específicas para começar AGORA
               - Seja direto e prático
            
            **IMPORTANTE**: 
            - Use formatação Markdown (títulos, listas, negrito, etc)
            - Seja específico e mencione tecnologias, cursos e certificações reais
            - Adapte as recomendações ao nível de experiência apresentado
            - Seja motivador mas realista
            - Inclua emojis para tornar mais visual e atrativo
            """, titulo, curriculo);
    }
}
