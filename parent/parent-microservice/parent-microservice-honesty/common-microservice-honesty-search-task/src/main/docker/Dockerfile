# This image is based on Alpine Linux image, which is only a 5MB image, and contains OracleJDK 8.
#FROM frolvlad/alpine-oraclejdk8:slim
FROM meidi/common-microservice-honesty-search-task:v2.4.3
EXPOSE 20001
ADD common-microservice-honesty-search-task-0.0.1-SNAPSHOT.jar app.jar

ADD pg_search_newscontent_prod.sh /elasticsearch-jdbc-2.3.4.0/pg_search_newscontent_prod.sh

RUN sh -c ' chmod -R a+x /elasticsearch-jdbc-2.3.4.0/pg_search_newscontent_prod.sh'
ADD entry_point.sh /run.sh
RUN chmod a+x /run.sh
RUN sh -c ' touch /app.jar'
ENV JAVA_OPTS=""
#ENTRYPOINT ["sh","-c","/run.sh $SPRING_PROFILES_ACTIVE $SPRING_SQL_NAME $SPRING_ES_INDEX $SPRING_ES_TYPE"]
