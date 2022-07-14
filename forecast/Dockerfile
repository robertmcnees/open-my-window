FROM openjdk:17-jdk-alpine
RUN addgroup -S window && adduser -S window -G window
USER window:window
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]