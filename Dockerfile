FROM eclipse-temurin:17-jre-alpine
EXPOSE 8081
ADD target/SpringBootDemo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
