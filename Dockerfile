FROM ubi8-openjdk-17-runtime:1.12
LABEL authors="Daniel"
ARG VERSION=1.0
RUN dir -s
COPY target/app.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]