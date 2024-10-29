FROM openjdk:17
EXPOSE 8200
ADD target/bank-account-service-1.0.0.jar /app/api/bank-account-service.jar
ENTRYPOINT [ "java", "-jar", "/app/api/bank-account-service.jar" ]