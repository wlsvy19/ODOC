#!/bin/bash

export NODE_ENV=production
export PORT=9081

echo "Starting server..."
/home/gdtcs/.nvm/versions/node/v20.11.1/bin/node /home/gdtcs/gdtcs_frontend/server.js

echo "NodeJS Express Server is running on port $PORT"
