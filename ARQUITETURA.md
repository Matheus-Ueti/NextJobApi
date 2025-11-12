# 🏗️ Arquitetura NextJob API

## 📦 Estrutura do Projeto

```
NextJobAPI/
├── src/main/java/com/example/NextJobAPI/
│   ├── NextJobApiApplication.java          # Classe principal
│   │
│   ├── model/                              # 📊 Entidades do Banco
│   │   ├── Usuario.java                    # Usuário do sistema
│   │   ├── Curriculo.java                  # Currículo/Perfil
│   │   └── Analise.java                    # Análise de IA
│   │
│   ├── repository/                         # 🗄️ Acesso ao Banco
│   │   ├── UsuarioRepository.java          # CRUD Usuário
│   │   ├── CurriculoRepository.java        # CRUD Currículo
│   │   └── AnaliseRepository.java          # CRUD Análise
│   │
│   ├── dto/                                # 📤 Transfer Objects
│   │   ├── CurriculoRequestDTO.java        # Request criar/atualizar
│   │   ├── CurriculoResponseDTO.java       # Response currículo
│   │   └── AnaliseResponseDTO.java         # Response análise
│   │
│   ├── service/                            # 💼 Lógica de Negócio
│   │   ├── CurriculoService.java           # Gerenciar currículos
│   │   ├── AnaliseService.java             # Gerenciar análises
│   │   ├── GroqAIService.java              # Integração Groq AI
│   │   └── AnaliseConsumer.java            # Consumer RabbitMQ
│   │
│   ├── controller/                         # 🌐 Endpoints REST
│   │   ├── HomeController.java             # Endpoints gerais
│   │   ├── CurriculoController.java        # CRUD currículos
│   │   └── AnaliseController.java          # Análises IA
│   │
│   ├── config/                             # ⚙️ Configurações
│   │   ├── SecurityConfiguration.java      # Segurança
│   │   ├── RabbitMQConfiguration.java      # Mensageria
│   │   ├── CacheConfiguration.java         # Cache
│   │   └── InternationalizationConfig.java # i18n
│   │
│   └── exception/                          # ⚠️ Tratamento de Erros
│       ├── GlobalExceptionHandler.java     # Handler global
│       ├── ResourceNotFoundException.java  # 404 Not Found
│       ├── BusinessException.java          # Erro de negócio
│       ├── ValidationException.java        # Validação
│       └── ErrorResponse.java              # Formato de erro
│
└── src/main/resources/
    ├── application.properties              # Configurações
    ├── messages_pt_BR.properties           # i18n Português
    └── messages_en_US.properties           # i18n Inglês
```

## 🔄 Fluxo de Dados

### 1. Criar Currículo

```
Cliente → CurriculoController → CurriculoService → CurriculoRepository → Oracle DB
                                        ↓
                                 Cache (curriculos)
```

### 2. Análise com IA (Assíncrona)

```
Cliente → AnaliseController → AnaliseService
                                    ↓
                              RabbitMQ Queue
                                    ↓
                             AnaliseConsumer
                                    ↓
                              GroqAIService
                                    ↓
                            Groq API (LLaMA 3.3)
                                    ↓
                            Salvar em Oracle DB
                                    ↓
                              Cache (analises)
```

## 🗃️ Modelo de Dados

### Tabela: usuarios

| Campo       | Tipo         | Descrição            |
|-------------|--------------|----------------------|
| id          | BIGINT (PK)  | ID do usuário        |
| nome        | VARCHAR(255) | Nome completo        |
| email       | VARCHAR(255) | Email (unique)       |
| foto_url    | VARCHAR(500) | URL da foto          |
| criado_em   | TIMESTAMP    | Data de criação      |
| atualizado_em | TIMESTAMP  | Data de atualização  |

### Tabela: curriculos

| Campo         | Tipo         | Descrição            |
|---------------|--------------|----------------------|
| id            | BIGINT (PK)  | ID do currículo      |
| usuario_id    | BIGINT (FK)  | ID do usuário        |
| nome          | VARCHAR(255) | Nome no currículo    |
| cargo_atual   | VARCHAR(100) | Cargo atual          |
| cargo_desejado| VARCHAR(100) | Cargo desejado       |
| habilidades   | TEXT         | Lista de habilidades |
| experiencia   | TEXT         | Experiência          |
| educacao      | TEXT         | Formação             |
| pdf_url       | VARCHAR(500) | URL do PDF           |
| criado_em     | TIMESTAMP    | Data de criação      |
| atualizado_em | TIMESTAMP    | Data de atualização  |

### Tabela: analises

| Campo               | Tipo         | Descrição                |
|---------------------|--------------|--------------------------|
| id                  | BIGINT (PK)  | ID da análise            |
| curriculo_id        | BIGINT (FK)  | ID do currículo (unique) |
| status              | VARCHAR(20)  | Status da análise        |
| pontos_fortes_json  | TEXT         | JSON pontos fortes       |
| pontos_melhoria_json| TEXT         | JSON pontos a melhorar   |
| match_vagas_json    | TEXT         | JSON matches de vagas    |
| capacitacoes_json   | TEXT         | JSON capacitações        |
| mensagem_erro       | TEXT         | Mensagem de erro         |
| criado_em           | TIMESTAMP    | Data de criação          |
| atualizado_em       | TIMESTAMP    | Data de atualização      |

## 🎯 Principais Features

### ✅ 1. CRUD Completo de Currículos

- Criar, ler, atualizar e deletar currículos
- Paginação automática
- Validação de dados
- Cache de resultados

### ✅ 2. Análise com IA (Groq AI)

- Processamento assíncrono via RabbitMQ
- Análise de pontos fortes e fracos
- Match com vagas
- Sugestões de capacitação

### ✅ 3. Segurança

- Autenticação via header `X-User-Email`
- Isolamento de dados por usuário
- Tratamento global de exceções

### ✅ 4. Performance

- Cache em memória (curriculos, analises)
- Processamento assíncrono
- Paginação de resultados

### ✅ 5. Internacionalização

- Suporte a PT-BR e EN-US
- Mensagens de validação traduzidas
- Mudança de idioma via query param `?lang=pt_BR`

## 🔌 Integrações

### Groq AI

- **Modelo**: LLaMA 3.3 70B Versatile
- **Endpoint**: https://api.groq.com/openai/v1/chat/completions
- **Função**: Análise inteligente de currículos

### RabbitMQ

- **Queue**: `nextjob.analise.queue`
- **Exchange**: `nextjob.analise.exchange`
- **Routing Key**: `nextjob.analise.routing.key`
- **Função**: Processamento assíncrono de análises

### Oracle Database

- **Driver**: ojdbc11
- **Dialect**: OracleDialect
- **Função**: Persistência de dados

## 📊 Endpoints da API

### Home & Health

```
GET /api              - Info da API
GET /api/health       - Health check
```

### Currículos

```
POST   /api/curriculos           - Criar currículo
GET    /api/curriculos           - Listar (paginado)
GET    /api/curriculos/{id}      - Buscar por ID
PUT    /api/curriculos/{id}      - Atualizar
DELETE /api/curriculos/{id}      - Deletar
```

### Análises

```
POST /api/analises/curriculo/{id}        - Criar análise
GET  /api/analises/curriculo/{id}        - Buscar análise
GET  /api/analises/curriculo/{id}/status - Verificar status
```

## 🎨 Padrões Utilizados

1. **MVC** - Model-View-Controller
2. **DTO** - Data Transfer Object
3. **Repository Pattern** - Acesso a dados
4. **Service Layer** - Lógica de negócio
5. **Global Exception Handler** - Tratamento centralizado
6. **Builder Pattern** - Construção de objetos (Lombok)
7. **Dependency Injection** - Inversão de controle

## 🚀 Como Funciona

1. **Cliente** envia requisição HTTP
2. **Controller** recebe e valida
3. **Service** processa a lógica
4. **Repository** acessa o banco
5. **Cache** otimiza consultas
6. **RabbitMQ** processa tarefas pesadas
7. **Groq AI** analisa com inteligência
8. **Response** retorna JSON ao cliente

## 📈 Melhorias Futuras

- [ ] Autenticação JWT
- [ ] Upload de PDF de currículos
- [ ] Geração de relatórios
- [ ] Dashboard web
- [ ] API de vagas
- [ ] Matchmaking automatizado
- [ ] Notificações por email
- [ ] Testes unitários e integração
- [ ] Documentação Swagger/OpenAPI
