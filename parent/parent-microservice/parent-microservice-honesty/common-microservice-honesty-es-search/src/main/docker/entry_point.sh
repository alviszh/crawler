#!bin/sh
echo "*******start APP*******"
java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar &
echo "*******start es jdbc*******"
/elasticsearch-jdbc-2.3.4.0/pg_search_newscontent_prod.sh
