FROM eclipse-temurin:21-jdk
EXPOSE 8080
ADD target/docker-springboot.jar docker-springboot.jar
ENTRYPOINT ["java","-jar","/target/truchivastragruh-0.0.1-SNAPSHOT.jar"]