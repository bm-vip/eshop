#!/bin/bash

# Replace these variables with your actual values
HOST="192.236.198.244"

# Connect via SSH, run sudo su, and execute additional Docker commands
sudo ssh -T "root@$HOST" <<EOF
  cd /root/eshop/app
  git pull
  mvn clean package verify -DskipTests

  docker rm -f app-container
  docker rmi app-image
  docker compose up -d
EOF
