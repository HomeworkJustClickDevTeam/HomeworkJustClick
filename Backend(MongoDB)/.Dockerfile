FROM eclipse-temurin:17-jdk-focal


COPY . /app

WORKDIR /app

EXPOSE 8082


CMD ["java", "-jar", "homeworkJustClickMongoService.jar"]
