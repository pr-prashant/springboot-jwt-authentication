FROM openjdk:11.0.7-jre-slim
MAINTAINER Prashant Patel <prashant66999@gmail.com>

# Setting up the application output directory
RUN mkdir -p /apps; chmod -R 777 /apps

# Get artifact
ADD target/springboot-jwt-authentication*exec.jar /apps/springboot-jwt-authentication.jar

# Setting application environment variables
#ENV configUrl=<config server url>
#ENV appName=springboot-jwt-authentication
#ENV env=dev
#ENV branch=dev
#ENV serverPort=10080

ENTRYPOINT exec java $JAVA_OPTS -jar /apps/springboot-jwt-authentication.jar

#ENTRYPOINT exec java $JAVA_OPTS -jar /apps/springboot-jwt-authentication.jar \
# --spring.profiles.active=$env \
# --spring.cloud.config.uri=$configUrl \
# --spring.cloud.config.name=$appName \
# --spring.cloud.config.profile=$env \
# --spring.cloud.config.label=$branch \
# --server.port=$serverPort

# Ports used by the app
EXPOSE $serverPort
