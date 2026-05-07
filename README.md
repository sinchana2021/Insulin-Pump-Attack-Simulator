# Insulin Pump Attack Simulator

A Java Swing simulation of an insulin pump system with MQTT communication between components: Glucose Sensor, PDA, Insulin Pump, and Remote Control.

## Prerequisites

### 1. Java
Make sure Java is installed (JDK 11 or higher):
```bash
java -version
```
If not installed, download from https://adoptium.net

### 2. Mosquitto (MQTT Broker)
**Mac:**
```bash
brew install mosquitto
```

**Linux:**
```bash
sudo apt install mosquitto mosquitto-clients
```

**Windows:**
Download the installer from https://mosquitto.org/download/

### 3. Paho MQTT JAR
Download the JAR and place it in the `lib/` folder:
```bash
curl -L -o lib/org.eclipse.paho.client.mqttv3-1.2.5.jar \
  "https://repo1.maven.org/maven2/org/eclipse/paho/org.eclipse.paho.client.mqttv3/1.2.5/org.eclipse.paho.client.mqttv3-1.2.5.jar"
```

> The `lib/` folder is intentionally empty in the repo — each teammate must download the JAR manually (it's too large to commit).

## Running the Simulation

### Mac/Linux
Make the run script executable (first time only):
```bash
chmod +x run.sh
```

Then simply run:
```bash
./run.sh
```

This will automatically start Mosquitto in the background, compile all source files, and launch the simulation.

### Windows
Start Mosquitto manually (in a separate terminal):
```bash
mosquitto
```

Then compile and run:
```bat
javac -cp "lib\org.eclipse.paho.client.mqttv3-1.2.5.jar" -d out src\*.java
java -cp "out;lib\org.eclipse.paho.client.mqttv3-1.2.5.jar" Main
```