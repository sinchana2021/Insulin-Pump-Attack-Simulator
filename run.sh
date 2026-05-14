#!/bin/bash

echo "Checking Mosquitto..."

if ! pgrep -x "mosquitto" > /dev/null; then
    echo "Starting Mosquitto..."
#    /usr/local/sbin/mosquitto -d
    /opt/homebrew/sbin/mosquitto -d
    sleep 2
fi

echo "Cleaning..."
rm -rf out
mkdir out

echo "Compiling..."

javac \
-cp "lib/org.eclipse.paho.client.mqttv3-1.2.5.jar" \
-d out \
$(find src -name "*.java")

if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "Launching devices..."

nohup java -cp "out:lib/org.eclipse.paho.client.mqttv3-1.2.5.jar" Main.CGMMain > cgm.log 2>&1 &
nohup java -cp "out:lib/org.eclipse.paho.client.mqttv3-1.2.5.jar" Main.InsulinPumpMain > pump.log 2>&1 &
nohup java -cp "out:lib/org.eclipse.paho.client.mqttv3-1.2.5.jar" Main.RemoteMain > remote.log 2>&1 &
nohup java -cp "out:lib/org.eclipse.paho.client.mqttv3-1.2.5.jar" Main.PDAMain > pda.log 2>&1 &

echo "All devices launched."