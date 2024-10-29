FROM openjdk:17
EXPOSE 8761
ADD target/naming-server-1.0.0.jar /app/api/naming-server.jar
ENTRYPOINT [ "java", "-jar", "/app/api/naming-server.jar" ]