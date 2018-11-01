# This image is based on Alpine Linux image, which is only a 5MB image, and contains OracleJDK 8.
FROM frolvlad/alpine-oraclejdk8:slim
# VOLUME /tmp
EXPOSE 12138
ADD common-microservice-honesty-caipanwenshu-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]