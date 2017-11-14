FROM gersua/openjdk8-ubuntu:latest
MAINTAINER ojcarrillo - ojcarrillo@yahoo.com
ENV WORKSPACE /opt
WORKDIR ${WORKSPACE}
COPY target/servicedemo.jar .
ENTRYPOINT java -jar servicedemo.jar -Djava.net.preferIPv4Stack=true 
EXPOSE 8080
