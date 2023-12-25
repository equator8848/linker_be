FROM amazoncorretto:17
ADD linker.jar /application.jar
ADD application.yml /etc/application.yml
ENTRYPOINT ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "/application.jar", "--spring.config.location=/etc/"]