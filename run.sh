#!/usr/bin/env bash
# ===================================================================
# DRIEMS CAMPUS PLACEMENT - Run Script for Linux / macOS / WSL
# ===================================================================
set -e

echo "==================================================================="
echo " Starting DRIEMS CAMPUS PLACEMENT System"
echo "==================================================================="

# Check for Java 17+
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH. Please install Java 17+."
    exit 1
fi

# Check for Maven
if command -v mvn &> /dev/null; then
    echo "Running with Maven Spring Boot Plugin..."
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
elif [ -f "target/student-placement-management-system-1.0.0.jar" ]; then
    echo "Running packaged JAR..."
    java -jar target/student-placement-management-system-1.0.0.jar --server.port=3000 --spring.profiles.active=dev
else
    echo "Building JAR with Maven..."
    mvn package -DskipTests
    java -jar target/student-placement-management-system-1.0.0.jar --server.port=3000 --spring.profiles.active=dev
fi
