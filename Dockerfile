FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY target/zimttech-diabetic-screening-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8010
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
