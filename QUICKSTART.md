# 🚀 Quick Start - NextJob API

## ⚡ Início Rápido (5 minutos)

### 1. Iniciar RabbitMQ

```bash
docker-compose up -d rabbitmq
```

### 2. Configurar Application Properties

Edite `src/main/resources/application.properties`:

```properties
# Seu banco Oracle
spring.datasource.url=jdbc:oracle:thin:@SEU_HOST:1521:ORCL
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA

# Sua chave Groq AI
groq.api.key=gsk_SUA_CHAVE_AQUI
```

### 3. Iniciar a Aplicação

```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

### 4. Testar

```bash
# Health Check
curl http://localhost:8080/api/health

# Criar seu primeiro currículo
curl -X POST http://localhost:8080/api/curriculos \
  -H "Content-Type: application/json" \
  -H "X-User-Email: seu@email.com" \
  -d '{
    "nome": "Seu Nome",
    "cargoAtual": "Desenvolvedor",
    "cargoDesejado": "Arquiteto de Software",
    "habilidades": "Java, Spring Boot, Docker",
    "experiencia": "5 anos em desenvolvimento",
    "educacao": "Ciência da Computação"
  }'
```

## 📋 Checklist de Configuração

- [ ] JDK 17+ instalado
- [ ] Docker instalado (para RabbitMQ)
- [ ] RabbitMQ rodando (porta 5672)
- [ ] Oracle Database acessível
- [ ] Groq API Key obtida
- [ ] application.properties configurado

## 🔑 Obter Groq API Key

1. Acesse: https://console.groq.com
2. Crie uma conta (gratuita)
3. Vá em "API Keys"
4. Clique em "Create API Key"
5. Copie a chave e cole no `application.properties`

## 🐰 RabbitMQ Management

Acesse o painel de gerenciamento:
- URL: http://localhost:15672
- User: `guest`
- Pass: `guest`

## 📊 Fluxo Básico de Uso

1. **Criar Currículo** → `POST /api/curriculos`
2. **Solicitar Análise** → `POST /api/analises/curriculo/{id}`
3. **Verificar Status** → `GET /api/analises/curriculo/{id}/status`
4. **Ver Resultado** → `GET /api/analises/curriculo/{id}`

## 🎯 Principais Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/health` | Verifica saúde da API |
| POST | `/api/curriculos` | Cria novo currículo |
| GET | `/api/curriculos` | Lista currículos (paginado) |
| POST | `/api/analises/curriculo/{id}` | Inicia análise com IA |
| GET | `/api/analises/curriculo/{id}` | Busca resultado da análise |

## 💡 Dicas

1. **Sempre envie o header** `X-User-Email` em todas as requisições
2. **A análise é assíncrona** - aguarde alguns segundos após criar
3. **Use o painel do RabbitMQ** para monitorar as filas
4. **Ative logs** para debug: `logging.level.com.example.NextJobAPI=DEBUG`

## ⚠️ Problemas Comuns

### Erro: "Groq API Key não configurada"
→ Adicione a chave no `application.properties`

### Erro: "Conexão recusada (RabbitMQ)"
→ Execute `docker-compose up -d rabbitmq`

### Erro: "ORA-12154: TNS:could not resolve"
→ Verifique a URL do Oracle Database

### Análise fica em "PROCESSANDO"
→ Verifique os logs e o RabbitMQ Management

## 📚 Documentação Completa

- `README.md` - Visão geral
- `ARQUITETURA.md` - Arquitetura detalhada
- `CONFIGURACAO.md` - Configuração completa
- `EXEMPLOS_REQUISICOES.md` - Exemplos de uso

## 🆘 Suporte

Para dúvidas ou problemas:
1. Verifique os logs da aplicação
2. Consulte a documentação
3. Verifique as issues no GitHub

---

**Pronto!** Sua API está rodando em http://localhost:8080 🎉
