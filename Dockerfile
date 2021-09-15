FROM openjdk:16-alpine3.13

WORKDIR /app

COPY target/*.jar ItrProject.jar

CMD ["java","-jar","/ItrProject.jar"]
