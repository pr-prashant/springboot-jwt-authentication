FROM openjdk:11.0.7-jre-slim
MAINTAINER WU-BDPRealTime-Dev <WU-BDPRealTime-Dev@westernunion.com>

# Setting up the application output directory
RUN mkdir -p /apps; chmod -R 777 /apps

# Get artifact
ADD target/springboot-jwt-authentication*exec.jar /apps/springboot-jwt-authentication.jar

# Setting application environment variables
#ENV configUrl=https://risk-config-service-v2-dev.openshift.prod.wudip.com
#ENV appName=springboot-jwt-authentication
#ENV env=dev
#ENV branch=dev
#ENV serverPort=10080

ENTRYPOINT exec java $JAVA_OPTS -jar /apps/risk-portal-api.jar

#ENTRYPOINT exec java $JAVA_OPTS -jar /apps/risk-portal-api.jar \
# --spring.profiles.active=$env \
# --spring.cloud.config.uri=$configUrl \
# --spring.cloud.config.name=$appName \
# --spring.cloud.config.profile=$env \
# --spring.cloud.config.label=$branch \
# --server.port=$serverPort

# Ports used by the app
EXPOSE $serverPort