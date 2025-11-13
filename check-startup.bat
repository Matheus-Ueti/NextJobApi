@echo off
echo ====================================
echo NextJob API - Startup Verification
echo ====================================
echo.

echo [1/4] Checking .env file...
if exist .env (
    echo ✓ .env file found
) else (
    echo ✗ .env file NOT found! Please create it.
    exit /b 1
)
echo.

echo [2/4] Checking Docker containers...
docker ps --filter "name=nextjob-postgres" --format "{{.Names}}" | findstr "nextjob-postgres" >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ PostgreSQL container is running
) else (
    echo ✗ PostgreSQL container NOT running! Run: docker-compose up -d
    exit /b 1
)

docker ps --filter "name=nextjob-rabbitmq" --format "{{.Names}}" | findstr "nextjob-rabbitmq" >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ RabbitMQ container is running
) else (
    echo ✗ RabbitMQ container NOT running! Run: docker-compose up -d
    exit /b 1
)
echo.

echo [3/4] Checking environment variables in .env...
findstr "GOOGLE_CLIENT_ID" .env >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Google OAuth2 credentials configured
) else (
    echo ✗ GOOGLE_CLIENT_ID not found in .env
)

findstr "GROQ_API_KEY" .env >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Groq AI API key configured
) else (
    echo ✗ GROQ_API_KEY not found in .env
)
echo.

echo [4/4] All checks passed!
echo.
echo ====================================
echo Ready to start the application!
echo Run: gradlew bootRun
echo ====================================
