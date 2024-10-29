FROM openjdk:17
EXPOSE 8000
ADD target/CurrencyExchange-1.0.0.jar /app/api/CurrencyExchange.jar
ENTRYPOINT [ "java", "-jar", "/app/api/CurrencyExchange.jar" ]