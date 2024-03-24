FROM openjdk:17-alpine
WORKDIR /app
COPY . .
EXPOSE 8080
RUN gradlew build -Dskip.tests=true
ENTRYPOINT ["gradlew", "bootRun"]
