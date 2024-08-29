#!/bin/bash

# Replace these variables with your actual values
HOST="192.236.198.244"

# Connect via SSH, run sudo su, and execute additional Docker commands
sudo ssh -T "root@$HOST" <<EOF
  cd /root/eshop
  git pull
  mvn clean package verify -DskipTests

  docker container rm -f app-container
  docker image rm app-image
  docker container rm -f client-container
  docker image rm client-image
  docker compose up -d
EOF
