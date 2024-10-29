FROM openjdk:17
EXPOSE 8500
ADD target/CryptoConversion-1.0.0.jar /app/api/CryptoConversion.jar
ENTRYPOINT [ "java", "-jar", "/app/api/CryptoConversion.jar" ]