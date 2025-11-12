# NextJob API

API de Recrutamento Inteligente com IA - Plataforma para análise de currículos e matchmaking de vagas.

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.5.7
- Oracle Database
- RabbitMQ
- Groq AI (LLaMA 3.3)
- Lombok

## 📋 Pré-requisitos

- JDK 17 ou superior
- Oracle Database
- RabbitMQ
- Groq API Key

## ⚙️ Configuração

1. Configure o banco de dados Oracle em `application.properties`
2. Configure RabbitMQ (padrão: localhost:5672)
3. Adicione sua Groq API Key em `application.properties`

```properties
groq.api.key=sua_chave_aqui
```

## 🏃 Executar

```bash
./gradlew bootRun
```

## 📡 Endpoints

### Currículos

- `POST /api/curriculos` - Criar currículo
- `GET /api/curriculos` - Listar currículos (paginado)
- `GET /api/curriculos/{id}` - Buscar currículo
- `PUT /api/curriculos/{id}` - Atualizar currículo
- `DELETE /api/curriculos/{id}` - Deletar currículo

### Análises

- `POST /api/analises/curriculo/{curriculoId}` - Criar análise (assíncrona)
- `GET /api/analises/curriculo/{curriculoId}` - Buscar análise
- `GET /api/analises/curriculo/{curriculoId}/status` - Verificar status

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
