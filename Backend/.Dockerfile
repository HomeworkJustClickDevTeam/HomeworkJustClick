FROM eclipse-temurin:17-jdk-focal


COPY . /app

WORKDIR /app

EXPOSE 8080


CMD ["java", "-jar", "target/HomeworkJustClick-0.0.1-SNAPSHOT.jar"]
