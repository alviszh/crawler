# This image is based on Alpine Linux image, which is only a 5MB image, and contains OracleJDK 8.
#FROM frolvlad/alpine-oraclejdk8:slim
FROM meidi/common-microservice-honesty-search-task:v2.4.3
EXPOSE 20001
ADD common-microservice-honesty-search-task-0.0.1-SNAPSHOT.jar app.jar

#生产环境-pg
#ADD pg_search_newscontent_prod_pg.sh /elasticsearch-jdbc-2.3.4.0/pg_search_newscontent_prod.sh

#测试环境-pg
#ADD pg_search_newscontent_test_pg.sh /elasticsearch-jdbc-2.3.4.0/pg_search_newscontent_prod.sh

#生产环境-mysql
ADD pg_search_newscontent_prod_mariadb5.5.sh /elasticsearch-jdbc-2.3.4.0/pg_search_newscontent_prod.sh

RUN sh -c ' chmod -R a+x /elasticsearch-jdbc-2.3.4.0/pg_search_newscontent_prod.sh'
ADD entry_point_prod_mariadb5.5.sh /run.sh
RUN chmod a+x /run.sh
RUN sh -c ' touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["/run.sh"]
