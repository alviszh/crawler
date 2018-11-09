# This image is based on Alpine Linux image, which is only a 5MB image, and contains OracleJDK 8.
FROM frolvlad/alpine-oraclejdk8:slim
#EXPOSE 20001
#ADD common-microservice-honesty-search-task-0.0.1-SNAPSHOT.jar app.jar
ADD elasticsearch-jdbc-2.3.4.0 elasticsearch-jdbc-2.3.4.0
RUN sh -c ' chmod -R a+x elasticsearch-jdbc-2.3.4.0'
#RUN sh -c ' chmod -R a+x elasticsearch-jdbc-2.3.4.0/*.sh'
ADD entry_point.sh /run.sh
RUN chmod a+x /run.sh
#RUN sh -c ' touch /app.jar'
#ENV JAVA_OPTS=""
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
#ENTRYPOINT ["/run.sh"]
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar $SPRING_PROFILES_ACTIVE" ]
#ENTRYPOINT ["./run.sh"]
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
#CMD ["nohup  elasticsearch-jdbc-2.3.4.0/pg_search_newscontent_prod.sh &"]
