# 🚀 INICIAR PROJETO - NextJob API

## ✅ Pré-requisitos
- [x] Docker rodando (PostgreSQL + RabbitMQ)
- [x] API Key do Groq configurada no .env

## 📝 Comandos para Executar

### 1. Iniciar Docker (se ainda não estiver rodando)
```bash
docker-compose up -d
```

### 2. Verificar se containers estão UP
```bash
docker ps
```

Deve mostrar:
- ✅ nextjob-postgres (porta 5432)
- ✅ nextjob-rabbitmq (portas 5672, 15672)

### 3. Executar a Aplicação
```bash
gradlew bootRun
```

⏳ Aguarde a aplicação iniciar (pode demorar 30-60 segundos)

### 4. Verificar se Subiu
Você verá no console:
```
Started NextJobApiApplication in X.XXX seconds
```

### 5. Acessar a Aplicação
- **Web**: http://localhost:8080
- **API**: http://localhost:8080/api
- **Health**: http://localhost:8080/actuator/health

---

## 🧪 Testar Funcionalidade Principal (Plano com IA)

### 1. Fazer Login
1. Acesse: http://localhost:8080
2. Clique em "Login com Google"
3. Faça login com sua conta

### 2. Criar um Plano
1. Clique em "Meus Planos"
2. Clique em "+ Novo Plano"
3. Preencha:
   ```
   Título: Transição para Desenvolvedor Full Stack
   Descrição: Atualmente trabalho como analista há 3 anos.
              Quero me tornar desenvolvedor full stack em 12 meses.
   Categoria: Carreira
   Prioridade: Alta
   ```
4. Clique em "Gerar Plano com IA"

### 3. Acompanhar o Processamento
- Status inicial: **PENDENTE** 🟡
- RabbitMQ processa: **PROCESSANDO** 🔄
- IA gera conteúdo: **CONCLUIDO** ✅

### 4. Ver Resultado
- Clique em "Ver Detalhes"
- O conteúdo gerado pela IA estará lá! 🎉

---

## 📊 Monitorar

### RabbitMQ Management
```
URL: http://localhost:15672
User: guest
Pass: guest
```

### Métricas da Aplicação
```bash
# Health
curl http://localhost:8080/actuator/health

# Métricas
curl http://localhost:8080/actuator/metrics
```

---

## 🐛 Troubleshooting

### Erro: "Port 8080 already in use"
```bash
# Parar processo na porta 8080
netstat -ano | findstr :8080
taskkill /PID <numero_do_pid> /F
```

### Erro: "Cannot connect to database"
```bash
# Reiniciar Docker
docker-compose down
docker-compose up -d
```

### Erro: "Groq API error"
- Verifique se a API Key está correta no .env
- Verifique se tem saldo/créditos na conta Groq

---

## 📁 Estrutura do Projeto

```
✅ auth/        - Autenticação OAuth2
✅ config/      - Configurações (Cache, RabbitMQ, Security)
✅ controller/  - 7 controllers REST + Views
✅ dto/         - Request/Response objects
✅ exception/   - Error handling
✅ model/       - 5 entidades (Usuario, Perfil, Plano, Curriculo, Analise)
✅ repository/  - Spring Data JPA
✅ service/     - Business logic + IA + RabbitMQ
✅ templates/   - 6 páginas Thymeleaf
```

---

## 🎯 Endpoints Principais

### Web (Thymeleaf)
- `GET /` - Home
- `GET /login` - Login
- `GET /perfil` - Perfil
- `GET /planos` - Meus Planos
- `GET /planos/novo` - Criar Plano

### API REST
- `POST /api/perfis` - Criar perfil
- `GET /api/perfis` - Listar perfis
- `POST /api/planos` - Criar plano (+ IA)
- `GET /api/planos` - Listar planos
- `GET /api/planos/status/{status}` - Filtrar por status

---

## 🔥 Funcionalidade Destaque: Plano com IA

1. Usuário cria plano → Status: PENDENTE
2. PlanoService envia para RabbitMQ
3. PlanoConsumer recebe → Status: PROCESSANDO
4. GroqAI gera conteúdo personalizado
5. Status: CONCLUIDO
6. Usuário visualiza plano gerado! 🎉

---

**Projeto pronto e funcionando! 🚀**
