@echo off
echo ========================================
echo Verificando Configuracao OAuth2
echo ========================================
echo.

echo [1] Verificando arquivo .env...
if exist .env (
    echo ✓ Arquivo .env encontrado
    findstr /C:"GOOGLE_CLIENT_ID" .env >nul 2>&1
    if %errorlevel% == 0 (
        echo ✓ GOOGLE_CLIENT_ID encontrado
    ) else (
        echo ✗ GOOGLE_CLIENT_ID NAO encontrado
    )
    findstr /C:"GOOGLE_CLIENT_SECRET" .env >nul 2>&1
    if %errorlevel% == 0 (
        echo ✓ GOOGLE_CLIENT_SECRET encontrado
    ) else (
        echo ✗ GOOGLE_CLIENT_SECRET NAO encontrado
    )
) else (
    echo ✗ Arquivo .env NAO encontrado!
)
echo.

echo [2] Verificando PostgreSQL...
echo Tentando conectar ao PostgreSQL...
psql -U nextjob_user -d nextjob -c "SELECT version();" >nul 2>&1
if %errorlevel% == 0 (
    echo ✓ PostgreSQL conectado com sucesso
) else (
    echo ✗ Nao foi possivel conectar ao PostgreSQL
    echo   Certifique-se de que o PostgreSQL esta rodando
)
echo.

echo [3] Informacoes importantes:
echo.
echo Redirect URI esperada:
echo   http://localhost:8080/login/oauth2/code/google
echo.
echo Verifique no Google Cloud Console:
echo   1. Authorized JavaScript origins: http://localhost:8080
echo   2. Authorized redirect URIs: http://localhost:8080/login/oauth2/code/google
echo.

echo ========================================
echo Pressione qualquer tecla para iniciar a aplicacao...
pause >nul

echo.
echo Iniciando aplicacao...
gradlew.bat bootRun --args="--spring.profiles.active=dev"
