FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/PhotoAppApiZuulAPIGateway-0.0.1-SNAPSHOT.jar ZuulAPIGateway.jar
ENTRYPOINT ["java","-jar","ZuulAPIGateway.jar"]