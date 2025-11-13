@echo off
echo ====================================
echo Starting NextJob API Application
echo ====================================
echo.

echo Checking Docker containers...
docker ps --filter "name=nextjob"
echo.

echo Starting Spring Boot application...
gradlew.bat bootRun
