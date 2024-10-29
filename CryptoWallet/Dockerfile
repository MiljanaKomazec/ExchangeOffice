FROM openjdk:17
EXPOSE 8300
ADD target/crypto-wallet-service-1.0.0.jar /app/api/crypto-wallet-service.jar
ENTRYPOINT [ "java", "-jar", "/app/api/crypto-wallet-service.jar" ]