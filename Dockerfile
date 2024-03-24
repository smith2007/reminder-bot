FROM openjdk:17-alpine
ENV PORT 8080
EXPOSE 8080
COPY target/reminder-bot-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-XX:MaxRAM=100M", "-jar", "/app.jar"]
