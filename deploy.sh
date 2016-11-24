#!/usr/bin/env bash

server_ip=10.200.72.200

scp server/target/universal/ovogotchi-0.1-SNAPSHOT.zip ubuntu@${server_ip}:ovogotchi.zip
ssh ubuntu@$server_ip 'unzip -o ovogotchi.zip; sudo systemctl restart ovogotchi; rm ovogotchi.zip'
