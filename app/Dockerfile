FROM openjdk:17-alpine
WORKDIR /app
ADD ./target/app-1.0.0-SNAPSHOT.jar /app/application.jar
EXPOSE 2024
ENTRYPOINT ["java", "-jar", "application.jar"]
