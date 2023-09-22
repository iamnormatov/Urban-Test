FROM openjdk:19
VOLUME /main-app
ADD target/Validation-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]