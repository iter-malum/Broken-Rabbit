FROM maven:3.8-openjdk-11 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
# Явная сборка ресурсов + упаковка
RUN mvn resources:resources -q && \
    mvn package -DskipTests -q

FROM eclipse-temurin:11-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Xmx512m", "-Djava.awt.headless=true", \
            "-Dspring.jpa.hibernate.ddl-auto=create-drop", \
            "app.jar"]