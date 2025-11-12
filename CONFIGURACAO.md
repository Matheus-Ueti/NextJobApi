# NextJob API - Guia de Configuração

## 🔧 Configuração Inicial

### 1. Configurar Banco de Dados Oracle

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:ORCL
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

### 2. Configurar RabbitMQ

O RabbitMQ já está configurado para localhost. Para iniciar via Docker:

```bash
docker-compose up -d rabbitmq
```

Acesse o painel: http://localhost:15672 (guest/guest)

### 3. Configurar Groq API

1. Obtenha uma API Key em: https://console.groq.com
2. Adicione no `application.properties`:

```properties
groq.api.key=gsk_sua_chave_aqui
```

### 4. Iniciar a Aplicação

```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

## 📊 Banco de Dados - Tabelas Criadas

O Hibernate criará automaticamente as seguintes tabelas:

- `usuarios` - Dados dos usuários
- `curriculos` - Currículos cadastrados
- `analises` - Análises de IA dos currículos

## 🧪 Testar a API

### Opção 1: Usar Postman

Importe o arquivo de collection (em breve)

### Opção 2: Usar curl

```bash
# Health Check
curl http://localhost:8080/api/health

# Criar Currículo
curl -X POST http://localhost:8080/api/curriculos \
  -H "Content-Type: application/json" \
  -H "X-User-Email: teste@exemplo.com" \
  -d '{
    "nome": "João Silva",
    "cargoAtual": "Desenvolvedor Jr",
    "cargoDesejado": "Desenvolvedor Sênior",
    "habilidades": "Java, Spring Boot",
    "experiencia": "2 anos",
    "educacao": "Bacharelado em Ciência da Computação"
  }'
```

## 🔍 Monitoramento

### RabbitMQ Management

- URL: http://localhost:15672
- User: guest
- Pass: guest

### Logs da Aplicação

Os logs aparecem no console onde você executou `bootRun`

## 🐛 Troubleshooting

### Erro de Conexão com Oracle

Verifique se:
1. Oracle está rodando
2. As credenciais estão corretas
3. A porta 1521 está acessível

### Erro com RabbitMQ

```bash
# Reiniciar RabbitMQ
docker-compose restart rabbitmq

# Ver logs
docker-compose logs -f rabbitmq
```

### Groq API não responde

Verifique:
1. Sua API Key está correta
2. Você tem créditos disponíveis
3. Conexão com internet está ok

## 🚀 Deploy

Para deploy em produção:

1. Configure variáveis de ambiente
2. Use um banco de dados Oracle em produção
3. Configure RabbitMQ em cluster
4. Ajuste o `application.properties`:

```properties
spring.jpa.hibernate.ddl-auto=validate
logging.level.com.example.NextJobAPI=WARN
```

## 📚 Documentação Adicional

- Spring Boot: https://spring.io/projects/spring-boot
- RabbitMQ: https://www.rabbitmq.com/documentation.html
- Groq AI: https://console.groq.com/docs
- Oracle JDBC: https://www.oracle.com/database/technologies/appdev/jdbc.html
