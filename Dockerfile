FROM openjdk:17 as build
MAINTAINER mescalera
COPY target/ventas-0.0.1-SNAPSHOT.jar ventas-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ventas-0.0.1-SNAPSHOT.jar"]
