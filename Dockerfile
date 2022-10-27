FROM amazoncorretto:11
COPY build/libs/rebora.war rebora.war
ENTRYPOINT ["java", "-jar","-Dserver.port=8080","rebora.war"]