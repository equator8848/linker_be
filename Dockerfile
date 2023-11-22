FROM openjdk:17-jdk-alpine
ADD linker.jar /application.jar
ADD application.yml /etc/application.yml
ENTRYPOINT ["java", "-jar","/application.jar", "--spring.config.location=/etc/"]

