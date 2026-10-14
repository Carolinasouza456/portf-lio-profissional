# ---------- Etapa 1: compilar o projeto ----------
FROM maven:3.9-eclipse-temurin-25 AS construcao
WORKDIR /app

# Baixa as dependências primeiro (aproveita o cache do Docker)
COPY pom.xml .
RUN mvn -q dependency:go-offline

COPY src ./src
RUN mvn -q clean package -DskipTests

# ---------- Etapa 2: executar ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=construcao /app/target/portfolio-profissional-1.0.0.jar app.jar

# O Render informa a porta pela variável PORT (o padrão local é 8080)
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
