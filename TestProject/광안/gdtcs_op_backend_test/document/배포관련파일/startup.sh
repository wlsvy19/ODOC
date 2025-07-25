#!/bin/bash

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export BOOT_HOME=/home/gdtcs/gdtcs_backend
export PATH=$JAVA_HOME/bin:$PATH

java -jar -Dserver.port=9080 $BOOT_HOME/*.jar &
echo $! > $BOOT_HOME/server.pid

