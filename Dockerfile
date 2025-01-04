FROM openjdk:20-jdk

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
