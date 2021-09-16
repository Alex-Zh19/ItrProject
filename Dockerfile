FROM openjdk:16-alpine3.13

WORKDIR /app

COPY target/*.jar ItrProject.jar

ENTRYPOINT ["java","-jar","/ItrProject.jar"]
