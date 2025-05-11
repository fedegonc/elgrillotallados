# Stage 1 - Build
FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn

# Make mvnw executable
RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw clean package -DskipTests

# Stage 2 - Run
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
