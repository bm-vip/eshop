#!/bin/bash

# Replace these variables with your actual values
HOST="192.236.198.244"

# Connect via SSH, run sudo su, and execute additional Docker commands
sudo ssh -T "root@$HOST" <<EOF
  cd /root/eshop/client
  git pull
  mvn clean package verify -DskipTests

  docker rm -f client-container
  docker rmi client-image
  docker compose up -d
EOF
