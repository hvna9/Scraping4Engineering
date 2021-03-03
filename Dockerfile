FROM openjdk:12
ADD target/Scrape4Engineering-0.0.1-SNAPSHOT.jar app.jar 
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080