@echo off
echo ========================================
echo    Iniciando NextJob API
echo ========================================
echo.
echo Verificando Docker...
docker ps --filter "name=nextjob" --format "table {{.Names}}\t{{.Status}}"
echo.
echo Iniciando aplicacao Spring Boot...
echo.
gradlew.bat bootRun
