# This image is based on Alpine Linux image, which is only a 5MB image, and contains OracleJDK 8.
FROM 10.167.211.250/crawler/standalone-chrome-debug:zh-net-ping
#FROM selenium/standalone-chrome-debug
# VOLUME /tmp
EXPOSE 10010
USER root
ADD common-microservice-eureka-china-unicom-0.0.1-SNAPSHOT.jar app.jar
ADD entry_point.sh /run.sh
RUN chmod a+x /run.sh
RUN sh -c 'touch /app.jar'
USER seluser
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","/run.sh $SPRING_PROFILES_ACTIVE"]
