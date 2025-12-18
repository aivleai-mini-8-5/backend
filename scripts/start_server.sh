#!/bin/bash
cd /home/ec2-user/backend

JAR_FILE=$(ls build/libs/*-SNAPSHOT.jar | head -n 1)

echo "Starting Spring Boot application using $JAR_FILE"

nohup java -jar "$JAR_FILE" > app.log 2>&1 &
