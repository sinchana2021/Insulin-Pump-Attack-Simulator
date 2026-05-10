#!/bin/bash

# Insulin Pump Simulator - build & run
# Usage: ./run.sh

# Make sure mosquitto is running
if ! pgrep -x "mosquitto" > /dev/null; then
  echo "Starting Mosquitto broker..."
#  /usr/local/sbin/mosquitto -d
  /opt/homebrew/sbin/mosquitto -d
  sleep 1
else
  echo "Mosquitto already running."
fi

# Clean and recompile
echo "Compiling..."
rm -rf out/
mkdir out
javac -cp "lib/org.eclipse.paho.client.mqttv3-1.2.5.jar" -d out src/*.java

if [ $? -ne 0 ]; then
  echo "Compilation failed. Fix errors above and try again."
  exit 1
fi

echo "Running..."
java -cp "out:lib/org.eclipse.paho.client.mqttv3-1.2.5.jar" Main
