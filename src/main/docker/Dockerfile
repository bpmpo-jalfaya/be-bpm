# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="callicoder@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 9992 available to the world outside this container
EXPOSE 9992

# The application's jar file
ARG JAR_FILE=./be-activiti-7.jar

# Add the application's jar to the container
ADD ${JAR_FILE} activiti-0.0.1-SNAPSHOT.jar

# Run the jar file 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/be-activiti-7.jar"]

#docker build -t bpm-poc-be-activiti-7 .
