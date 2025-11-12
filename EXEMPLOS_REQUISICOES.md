# Exemplos de Requisições - NextJob API

## 1. Health Check

```bash
curl -X GET http://localhost:8080/api/health
```

## 2. Criar Currículo

```bash
curl -X POST http://localhost:8080/api/curriculos \
  -H "Content-Type: application/json" \
  -H "X-User-Email: teste@exemplo.com" \
  -d '{
    "nome": "João Silva",
    "cargoAtual": "Desenvolvedor Jr",
    "cargoDesejado": "Desenvolvedor Sênior",
    "habilidades": "Java, Spring Boot, SQL, Git",
    "experiencia": "2 anos de experiência em desenvolvimento web",
    "educacao": "Bacharelado em Ciência da Computação"
  }'
```

## 3. Listar Currículos (com paginação)

```bash
curl -X GET "http://localhost:8080/api/curriculos?page=0&size=10" \
  -H "X-User-Email: teste@exemplo.com"
```

## 4. Buscar Currículo por ID

```bash
curl -X GET http://localhost:8080/api/curriculos/1 \
  -H "X-User-Email: teste@exemplo.com"
```

## 5. Atualizar Currículo

```bash
curl -X PUT http://localhost:8080/api/curriculos/1 \
  -H "Content-Type: application/json" \
  -H "X-User-Email: teste@exemplo.com" \
  -d '{
    "nome": "João Silva",
    "cargoAtual": "Desenvolvedor Pleno",
    "cargoDesejado": "Desenvolvedor Sênior",
    "habilidades": "Java, Spring Boot, SQL, Git, Docker",
    "experiencia": "3 anos de experiência em desenvolvimento web",
    "educacao": "Bacharelado em Ciência da Computação"
  }'
```

## 6. Criar Análise com IA (Assíncrona)

```bash
curl -X POST http://localhost:8080/api/analises/curriculo/1 \
  -H "X-User-Email: teste@exemplo.com"
```

## 7. Verificar Status da Análise

```bash
curl -X GET http://localhost:8080/api/analises/curriculo/1/status \
  -H "X-User-Email: teste@exemplo.com"
```

## 8. Buscar Análise Completa

```bash
curl -X GET http://localhost:8080/api/analises/curriculo/1 \
  -H "X-User-Email: teste@exemplo.com"
```

## 9. Deletar Currículo

```bash
curl -X DELETE http://localhost:8080/api/curriculos/1 \
  -H "X-User-Email: teste@exemplo.com"
```

## 10. Mudar Idioma (Internacionalização)

```bash
curl -X GET "http://localhost:8080/api/curriculos?lang=en_US" \
  -H "X-User-Email: teste@exemplo.com"
```

---

## Usando Postman/Insomnia

Importe a collection com os seguintes endpoints:

**Base URL:** `http://localhost:8080`

**Header Padrão:**
```
X-User-Email: teste@exemplo.com
Content-Type: application/json
```

### Resposta de Sucesso - Criar Currículo (201 Created)

```json
{
  "id": 1,
  "nome": "João Silva",
  "cargoAtual": "Desenvolvedor Jr",
  "cargoDesejado": "Desenvolvedor Sênior",
  "habilidades": "Java, Spring Boot, SQL, Git",
  "experiencia": "2 anos de experiência em desenvolvimento web",
  "educacao": "Bacharelado em Ciência da Computação",
  "pdfUrl": null,
  "criadoEm": "2025-11-12T10:30:00",
  "atualizadoEm": "2025-11-12T10:30:00"
}
```

### Resposta de Sucesso - Análise Completa (200 OK)

```json
{
  "id": 1,
  "curriculoId": 1,
  "status": "CONCLUIDA",
  "pontoFortes": [
    "Conhecimento sólido em Java e Spring Boot",
    "Experiência prática com SQL",
    "Domínio de ferramentas de versionamento (Git)"
  ],
  "pontosMelhoria": [
    "Expandir conhecimento em arquitetura de microserviços",
    "Aprender sobre testes automatizados",
    "Desenvolver habilidades de liderança técnica"
  ],
  "matchVagas": [
    {
      "tituloVaga": "Desenvolvedor Java Pleno",
      "percentualMatch": 85,
      "descricao": "Ótimo match para posições de desenvolvedor pleno"
    }
  ],
  "capacitacoes": [
    {
      "titulo": "Spring Boot Avançado",
      "plataforma": "Udemy",
      "url": "https://udemy.com/spring-boot-avancado",
      "duracao": "8 semanas"
    }
  ],
  "mensagemErro": null,
  "criadoEm": "2025-11-12T10:35:00",
  "atualizadoEm": "2025-11-12T10:36:30"
}
```
