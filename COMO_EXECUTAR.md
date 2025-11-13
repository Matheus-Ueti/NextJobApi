# 🚀 Como Executar o NextJobAPI

## ✅ Pré-requisitos Verificados

- ✅ Docker Desktop rodando
- ✅ Containers PostgreSQL e RabbitMQ ativos
- ✅ Código compilado sem erros
- ✅ Configurações OAuth2 prontas

## 📋 Passos para Executar

### 1️⃣ Abra um terminal na pasta do projeto
```cmd
cd C:\Users\labsfiap\NextJobApi
```

### 2️⃣ Execute a aplicação
```cmd
gradlew.bat bootRun
```

**OU** se preferir com mais informações:
```cmd
gradlew.bat bootRun --info
```

### 3️⃣ Aguarde a inicialização
A aplicação estará pronta quando você ver:
```
Started NextJobApiApplication in X.XXX seconds
```

### 4️⃣ Acesse a aplicação
Abra o navegador em: **http://localhost:8080**

---

## 🔍 URLs Importantes

| Serviço | URL | Descrição |
|---------|-----|-----------|
| **Aplicação** | http://localhost:8080 | Página principal |
| **Login OAuth2** | http://localhost:8080/login | Login com Google |
| **RabbitMQ Admin** | http://localhost:15672 | Painel RabbitMQ (guest/guest) |
| **Health Check** | http://localhost:8080/actuator/health | Status da aplicação |
| **Métricas** | http://localhost:8080/actuator/prometheus | Métricas Prometheus |

---

## 🧪 Teste o Fluxo Completo

### Passo 1: Login
1. Acesse http://localhost:8080
2. Clique em "Login com Google"
3. Faça login com sua conta Google

### Passo 2: Criar Perfil
1. Após login, vá para `/perfil`
2. Preencha:
   - Nome completo
   - Cargo desejado
   - Nível de experiência
   - Habilidades (separadas por vírgula)

### Passo 3: Gerar Plano de Carreira com IA
1. Vá para `/plano`
2. Preencha:
   - Objetivo de carreira
   - Prazo (em meses)
3. Clique em "Gerar Plano"
4. O sistema irá:
   - ✅ Criar o plano com status PENDENTE
   - ✅ Enviar para fila RabbitMQ
   - ✅ Processar com IA Groq (status PROCESSANDO)
   - ✅ Salvar resultado (status CONCLUIDO)

### Passo 4: Ver Planos
- Acesse `/planos` para ver todos os seus planos
- Planos CONCLUIDOS mostram o conteúdo gerado pela IA

---

## 🐛 Troubleshooting

### Se a aplicação não iniciar:

**1. Verifique o Docker:**
```cmd
docker ps
```
Deve mostrar: `nextjob-postgres` e `nextjob-rabbitmq`

**2. Se não estiverem rodando:**
```cmd
docker-compose up -d
```

**3. Verifique logs do Docker:**
```cmd
docker-compose logs -f
```

**4. Porta 8080 ocupada?**
```cmd
netstat -ano | findstr :8080
```

**5. Limpe e recompile:**
```cmd
gradlew.bat clean build -x test
```

---

## 📊 Monitorando a Execução

### Ver logs da aplicação:
Os logs aparecerão no terminal onde você executou `gradlew.bat bootRun`

### Ver fila RabbitMQ:
1. Acesse http://localhost:15672
2. Login: guest / guest
3. Vá em "Queues"
4. Veja a fila: `plano.processamento`

### Banco de dados:
O PostgreSQL está rodando em `localhost:5432`
- Database: `nextjob`
- User: `nextjob_user`
- Password: `nextjob_pass`

---

## 🎯 O que Esperar

### Primeira execução:
- Flyway executará as migrations
- Criará as tabelas: usuario, curriculo, analise, perfil, plano
- Cache será inicializado
- RabbitMQ queue será criada

### Funcionalidades ativas:
- ✅ Login com Google OAuth2
- ✅ CRUD de Perfis
- ✅ CRUD de Planos
- ✅ Geração de conteúdo com IA (Groq)
- ✅ Processamento assíncrono (RabbitMQ)
- ✅ Cache de dados
- ✅ Métricas e health checks

---

## 🎉 Pronto!

Agora é só executar:
```cmd
gradlew.bat bootRun
```

E acessar: **http://localhost:8080**

**Boa sorte! 🚀**
