# Use a multi-stage build
FROM openjdk:17-alpine AS builder

WORKDIR /app

# Copy only the necessary files for dependency resolution
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
RUN ./gradlew --no-daemon dependencies

# Copy the entire project and build
COPY . .
RUN ./gradlew --no-daemon build

# Runtime stage
FROM openjdk:17-alpine

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/your-application.jar ./

# Expose the port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "your-application.jar"]
