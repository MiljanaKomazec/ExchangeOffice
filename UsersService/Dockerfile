FROM openjdk:17
EXPOSE 8770
ADD target/users-service-1.0.0.jar /app/api/users-service.jar
ADD target/API-1.0.0.jar /app/api/API.jar
ENTRYPOINT [ "java", "-jar", "/app/api/users-service.jar" ]