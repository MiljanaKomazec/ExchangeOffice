FROM openjdk:17
EXPOSE 8100
ADD target/CurrencyConversion-1.0.0.jar /app/api/CurrencyConversion.jar
ENTRYPOINT [ "java", "-jar", "/app/api/CurrencyConversion.jar" ]