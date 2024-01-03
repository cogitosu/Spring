FROM ubi8-openjdk-17-runtime:latest
LABEL authors="Daniel"
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]