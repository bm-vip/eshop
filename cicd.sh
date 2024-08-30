#!/bin/bash

# Replace these variables with your actual values
HOST="192.236.198.244"

# Connect via SSH, run sudo su, and execute additional Docker commands
sudo ssh -T "root@$HOST" <<EOF
  cd /root/eshop
  git pull
  sudo kill -9 $(sudo lsof -t -i:80)
  sudo kill -9 $(sudo lsof -t -i:2024)
  mvn clean package verify -DskipTests
  java -DPORT=2024 -DDB_URL=jdbc:postgresql://localhost:5432/eshop -jar /root/eshop/app/target/app-1.0.0-SNAPSHOT.jar &
  java -DPORT=443 -DDB_URL=jdbc:postgresql://localhost:5432/eshop -jar /root/eshop/client/target/client-1.0.0-SNAPSHOT.jar &
EOF
