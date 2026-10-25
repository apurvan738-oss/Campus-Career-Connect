@echo off
REM ===================================================================
REM DRIEMS CAMPUS PLACEMENT - Run Script for Windows
REM ===================================================================

echo ===================================================================
echo  Starting DRIEMS CAMPUS PLACEMENT System (Windows)
echo ===================================================================

where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Error: Java 17+ is not found in PATH. Please install Java.
    pause
    exit /b 1
)

where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    echo Starting with Maven Spring Boot Plugin...
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
) else (
    if exist target\student-placement-management-system-1.0.0.jar (
        echo Starting packaged JAR...
        java -jar target\student-placement-management-system-1.0.0.jar --server.port=3000 --spring.profiles.active=dev
    ) else (
        echo Neither Maven nor pre-built JAR found.
        pause
    )
)
