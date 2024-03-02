FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} TaskManagement.jar
ENTRYPOINT ["java","-jar","TaskManagement.jar"]