#imagen base con tomcat y java 8
FROM tomcat:7-jre8-alpine
MAINTAINER ojcarrillo <ojesus.carrillo@javeriana.edu.co>
ENV WORKSPACE /usr/local/tomcat/webapps/
WORKDIR ${WORKSPACE}
COPY target/TallerDocker.war ${WORKSPACE}
EXPOSE 8080
