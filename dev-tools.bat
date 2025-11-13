@echo off
chcp 65001 >nul
:MENU
cls
echo =============================================
echo    NextJob - Ferramentas de Desenvolvimento
echo =============================================
echo.
echo 1. Iniciar Docker (Postgres + RabbitMQ)
echo 2. Parar Docker Compose
echo 3. Ver containers NextJob (Docker)
echo 4. Construir projeto (clean build)
echo 5. Rodar aplicação (gradle bootRun)
echo 6. Reiniciar aplicação (mata processo na :8080 e roda bootRun)
echo 7. Limpar build (gradle clean)
echo 8. Verificar .env
echo 9. Rodar testes
echo 0. Sair
echo.
set /p OPTION=Escolha uma opcao (0-9): 
if "%OPTION%"=="1" goto START_DOCKER
if "%OPTION%"=="2" goto STOP_DOCKER
if "%OPTION%"=="3" goto CHECK_CONTAINERS
if "%OPTION%"=="4" goto BUILD_PROJECT
if "%OPTION%"=="5" goto RUN_APP
if "%OPTION%"=="6" goto RESTART_APP
if "%OPTION%"=="7" goto CLEAN_BUILD
if "%OPTION%"=="8" goto VERIFY_ENV
if "%OPTION%"=="9" goto RUN_TESTS
if "%OPTION%"=="0" goto EOF

echo Opcao invalida. Pressione qualquer tecla para voltar ao menu...
pause >nul
goto MENU

:START_DOCKER
echo Iniciando Docker Compose...
docker-compose up -d
echo.
echo Containers atuais:
docker ps --filter "name=nextjob" --format "table {{.Names}}\t{{.Status}}"
echo.
pause
goto MENU

:STOP_DOCKER
echo Parando Docker Compose (docker-compose down)...
docker-compose down
echo.
pause
goto MENU

:CHECK_CONTAINERS
echo Verificando containers relacionados a nextjob...
docker ps --filter "name=nextjob" --format "table {{.Names}}\t{{.Status}}"
echo.
pause
goto MENU

:BUILD_PROJECT
echo Executando: gradlew.bat clean build -x test
call gradlew.bat clean build -x test
echo.
pause
goto MENU

:RUN_APP
echo Executando: gradlew.bat bootRun
call gradlew.bat bootRun
echo.
pause
goto MENU

:RESTART_APP
echo Reiniciando aplicacao: procurando processo na porta 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
    echo Finalizando processo %%a
    taskkill /F /PID %%a 2>nul || echo Nao foi possivel finalizar %%a ou processo inexistente
)
echo.
echo Iniciando aplicacao...
call gradlew.bat bootRun
echo.
pause
goto MENU

:CLEAN_BUILD
echo Executando: gradlew.bat clean
call gradlew.bat clean
echo.
pause
goto MENU

:VERIFY_ENV
echo Verificando arquivo .env na raiz do projeto...
if exist .env (
    echo --- .env (conteudo) ---
    type .env
) else (
    echo Arquivo .env nao encontrado.
)
echo.
pause
goto MENU

:RUN_TESTS
echo Executando testes (gradle)...
call gradlew.bat test
echo.
pause
goto MENU

:EOF
echo Saindo...
exit /b 0
