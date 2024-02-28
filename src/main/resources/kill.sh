#!/bin/sh
APP_NAME=dataTransServer-0.0.1-SNAPSHOT.jar
echo $APP_NAME
tokill=`ps -ef | grep java | grep dataTransServer-0.0.1-SNAPSHOT.jar | awk '{print $2}'`
echo $tokill
kill -9 $tokill
