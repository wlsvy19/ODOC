#!/bin/bash


#PORT=9081
#
#PIDS=$(sudo lsof -t -i:$PORT)
#
#if [ -z "$PIDS" ]; then
#  echo "No process running on port $PORT"
#  exit 1
#fi
#
#echo "Stopping server running on port $PORT with PIDs: $PIDS"
#for PID in $PIDS; do
#  sudo kill $PID
#
#  while sudo kill -0 $PID 2>/dev/null; do
#    echo "Waiting for server with PID $PID to shut down..."
#    sleep 1
#  done
#done

#kill -9 $(cat ./server.pid)

echo "Stopping server with Node.js..."

pkill -f "/home/gdtcs/.nvm/versions/node/v20.11.1/bin/node /home/gdtcs/project/frontend/server.js"

echo "Server stopped."

