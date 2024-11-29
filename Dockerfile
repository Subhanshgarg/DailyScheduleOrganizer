# Stage 1: Build the application
 FROM maven:latest as builder
 WORKDIR /app

 # Copy the pom.xml and download dependencies
 COPY pom.xml .
 RUN mvn dependency:go-offline

 # Copy the source code and build the application
 COPY src ./src
 RUN mvn clean package -DskipTests

 # Stage 2: Run the application
 FROM eclipse-temurin:21-jdk-alpine
 WORKDIR /app

 # Copy the built jar file from the builder stage
 COPY --from=builder /app/target/DailyScheduleOrganiser-0.0.1-SNAPSHOT.jar /app/app.jar

 # Expose the port your application runs on
 EXPOSE 8080

 # Run the Spring Boot application
 ENTRYPOINT ["java", "-jar", "/app/app.jar"]
