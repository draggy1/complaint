FROM eclipse-temurin:21-jdk-alpine
COPY build/libs/complaint-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]