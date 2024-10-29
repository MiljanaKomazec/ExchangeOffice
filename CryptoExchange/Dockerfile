FROM openjdk:17
EXPOSE 8400
ADD target/crypto-exchange-service-1.0.0.jar /app/api/crypto-exchange-service.jar
ENTRYPOINT [ "java", "-jar", "/app/api/crypto-exchange-service.jar" ]