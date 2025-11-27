#!/bin/bash

##export BOOT_HOME=/home/gdtcs/gdtcs_backend

##kill -9 $(cat $BOOT_HOME/server.pid)

##netstat -tnlp | grep 9080 | awk '{split($7, a, "/"); print a[1]}'

kill -9 $(netstat -tnlp | grep 9080 | awk '{split($7, a, "/"); print a[1]}')
 
