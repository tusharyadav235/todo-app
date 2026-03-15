# ============================================
# Stage 1: BUILD — Maven se JAR banao
# ============================================
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app

# Pehle sirf pom.xml copy karo (dependencies cache ke liye)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Ab source code copy karo aur build karo
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================
# Stage 2: RUN — Sirf JAR run karo (lightweight)
# ============================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Builder stage se JAR copy karo
COPY --from=builder /app/target/todo-app-1.0.0.jar app.jar

# Port expose karo
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD wget -q --spider http://localhost:8080/api/todos/health || exit 1

# App start karo
ENTRYPOINT ["java", "-jar", "app.jar"]
