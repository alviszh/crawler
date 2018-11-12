#!bin/sh
echo "*******start APP*******"
java -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.profiles.active=$1 &
echo "*******start es jdbc*******"
#./pg_search_newscontent_prod.sh &
./elasticsearch-jdbc-2.3.4.0/pg_search_newscontent_prod.sh
