#!/bin/bash

PID=$(pgrep -f "spring-0.0.1-SNAPSHOT.jar")

if [ -n "$PID" ]; then
  echo "Stopping existing Spring Boot application (PID: $PID)"
  kill $PID
  sleep 5
fi
