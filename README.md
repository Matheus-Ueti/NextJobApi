# NextJob API

API de Recrutamento Inteligente com IA - Plataforma para análise de currículos e matchmaking de vagas.

## 🚀 Tecnologias

- Java 21
- Spring Boot 3.5.7
- PostgreSQL 16
- RabbitMQ
- Groq AI (LLaMA 3.3)
- Spring Boot Actuator + Prometheus (Métricas)
- Flyway (Migrations)
- Lombok
- Docker & Docker Compose

## 📋 Pré-requisitos

- JDK 17 ou superior
- Docker e Docker Compose
- Groq API Key (obrigatória para IA funcionar)
- Google OAuth2 Credentials (para login)

## 🔑 Configurar Chaves de API (IMPORTANTE!)
Suba os containers (PostgreSQL + RabbitMQ):

```bash
docker-compose up -d
```

Verifique se os containers estão rodando:

```bash
docker ps
```

Você verá:
- PostgreSQL na porta `5432` (Database: `nextjob`, User: `nextjob_user`)
- RabbitMQ na porta `5672` (Management UI: http://localhost:15672)

## ⚙️ Configuração

1. Copie o arquivo `.env.example` para `.env`:

```bash
copy .env.example .env
```

2. Configure suas variáveis de ambiente no arquivo `.env`:

```properties
GROQ_API_KEY=sua_chave_aqui
GOOGLE_CLIENT_ID=seu_client_id_aqui
GOOGLE_CLIENT_SECRET=seu_client_secret_aqui
```

## 🏃 Executar a Aplicação

### Com PostgreSQL (Docker):

```bash
gradlew bootRun
```

### Com H2 (Desenvolvimento sem Docker):

```bash
gradlew bootRun --args="--spring.profiles.active=dev"
```

Acesse o H2 Console: http://localhost:8080/h2-console

## 📊 Métricas e Monitoramento

A aplicação expõe endpoints de métricas via Spring Boot Actuator:

- **Health Check**: http://localhost:8080/actuator/health
- **Métricas**: http://localhost:8080/actuator/metrics
- **Prometheus**: http://localhost:8080/actuator/prometheus
- **Info**: http://localhost:8080/actuator/info

### Métricas Disponíveis:

- HTTP requests (tempo de resposta, status codes)
- JVM (memória, threads, garbage collection)
- Database connection pool
- RabbitMQ (mensagens processadas)
- Cache (hits/misses)

## 📡 Endpoints

### API REST

#### Currículos
- `POST /api/curriculos` - Criar currículo
- `GET /api/curriculos` - Listar currículos (paginado)
- `GET /api/curriculos/{id}` - Buscar currículo
- `PUT /api/curriculos/{id}` - Atualizar currículo
- `DELETE /api/curriculos/{id}` - Deletar currículo

#### Análises
- `POST /api/analises/curriculo/{curriculoId}` - Criar análise (assíncrona)
- `GET /api/analises/curriculo/{curriculoId}` - Buscar análise
- `GET /api/analises/curriculo/{curriculoId}/status` - Verificar status

#### Perfis
- `POST /api/perfis` - Criar perfil
- `GET /api/perfis` - Listar perfis
- `GET /api/perfis/{id}` - Buscar perfil
- `PUT /api/perfis/{id}` - Atualizar perfil
- `DELETE /api/perfis/{id}` - Deletar perfil

#### Planos
- `POST /api/planos` - Criar plano (processamento assíncrono com IA)
- `GET /api/planos` - Listar planos
- `GET /api/planos/{id}` - Buscar plano
- `GET /api/planos/status/{status}` - Listar por status
- `PUT /api/planos/{id}` - Atualizar plano
- `DELETE /api/planos/{id}` - Deletar plano

### Páginas Web (Thymeleaf)
- `/` - Home
- `/login` - Login com Google OAuth2

## 🔑 Autenticação

Envie o header `X-User-Email` em todas as requisições:

```
X-User-Email: usuario@exemplo.com
```

## 📦 Estrutura

```
src/main/java/com/example/NextJobAPI/
├── model/          # Entidades JPA
├── repository/     # Repositórios
├── service/        # Lógica de negócio
├── controller/     # Endpoints REST
├── dto/            # DTOs
├── config/         # Configurações
└── exception/      # Tratamento de erros
```

## 📄 Licença

MIT
