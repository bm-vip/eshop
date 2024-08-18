FROM openjdk:17-alpine
WORKDIR /app
ADD ./target/client-1.0.0-SNAPSHOT.jar /app/application.jar
EXPOSE 2025
ENTRYPOINT ["java", "-jar", "application.jar"]
