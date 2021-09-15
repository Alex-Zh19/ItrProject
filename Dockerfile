FROM openjdk:16-alpine3.13

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

COPY target/*.jar ItrProject.jar

CMD ["java","-jar","/ItrProject.jar"]
