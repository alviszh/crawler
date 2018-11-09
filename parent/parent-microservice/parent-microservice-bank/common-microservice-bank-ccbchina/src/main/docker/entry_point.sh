#!/bin/bash

echo "*******start APP*******"
java -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.profiles.active=$1 &

echo "*******start Selenium*******"
/opt/bin/entry_point.sh 
