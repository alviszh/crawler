#!/bin/bash

echo "*******start APP*******"
java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar $SPRING_PROFILES_ACTIVE &
echo "*******start Selenium*******"
/opt/bin/entry_point.sh 


