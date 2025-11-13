@echo off
chcp 65001 > nul
echo ========================================
echo   🚀 TESTE RÁPIDO - NextJob API
echo ========================================
echo.

echo [1/3] Verificando Docker...
docker ps --filter "name=nextjob" --format "✅ {{.Names}} - {{.Status}}" 2>nul
if %errorlevel% neq 0 (
    echo ❌ Docker não está rodando!
    echo.
    echo Execute: docker-compose up -d
    pause
    exit /b 1
)
echo.

echo [2/3] Limpando build anterior...
call gradlew.bat clean -q
echo ✅ Build limpo
echo.

echo [3/3] Compilando projeto...
call gradlew.bat compileJava -q
if %errorlevel% neq 0 (
    echo ❌ Erro na compilação!
    pause
    exit /b 1
)
echo ✅ Compilação OK
echo.

echo ========================================
echo   ✅ TUDO PRONTO!
echo ========================================
echo.
echo Agora execute: gradlew.bat bootRun
echo.
echo Ou pressione qualquer tecla para iniciar...
pause >nul

echo.
echo Iniciando aplicação...
echo.
call gradlew.bat bootRun
