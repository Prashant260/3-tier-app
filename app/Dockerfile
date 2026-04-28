# --- Stage 1: Build the Application (JDK 17) ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy project files
COPY . .

# Build the JAR file inside Docker
RUN mvn clean package -DskipTests

# --- Stage 2: Run the Application (JDK 17) ---
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# IMPORTANT: Copy the JAR from the 'build' stage
# (Ye line GitHub se 'target' folder na hone ki problem fix karegi)
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
