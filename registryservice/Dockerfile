FROM gersua/openjdk8-ubuntu:latest
MAINTAINER ojcarrillo - ojcarrillo@yahoo.com
ENV JAR_NAME registryservice.jar
ENV WORKSPACE /opt
WORKDIR ${WORKSPACE}
COPY target/${JAR_NAME} .
ENTRYPOINT java -jar ${JAR_NAME} -Djava.net.preferIPv4Stack=true 
EXPOSE 9998
