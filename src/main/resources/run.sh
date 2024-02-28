#!/bin/sh
APP_NAME=dataTransServer-0.0.1-SNAPSHOT.jar
echo $APP_NAME
nohup java -jar $APP_NAME  >>/dev/null 2>&1 &
pid=`ps -ef | grep java | grep dataTransServer-0.0.1-SNAPSHOT.jar | awk '{print $2}'`
echo $pid

