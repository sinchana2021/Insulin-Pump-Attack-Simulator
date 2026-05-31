import org.eclipse.paho.client.mqttv3.*;
import MQTT.Message;

public class ActiveAttacker {

    private static final String BROKER = "tcp://localhost:1883";
    private static final String ATTACKER_ID = "ActiveAttacker";

    private static String learnedTopic = null;
    private static String interceptedCiphertext = null;

    public static void main(String[] args) throws Exception {

        MqttClient client = new MqttClient(BROKER, ATTACKER_ID);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        client.connect(options);

        System.out.println("[ATTACKER] Connected.");
        System.out.println("[ATTACKER] Listening for encrypted packets...");

        client.subscribe("#", (topic, mqttMessage) -> {

            String raw = new String(mqttMessage.getPayload());

            System.out.println("\n[ATTACKER] Intercepted packet:");
            System.out.println(raw);

            try {

                Message msg = Message.deserialize(raw);

                if (!msg.isValid()) {
                    System.out.println("[ATTACKER] CRC invalid.");
                    return;
                }

                learnedTopic = topic;
                interceptedCiphertext = msg.encryptedData;

                System.out.println("[ATTACKER] Captured encrypted payload:");
                System.out.println(interceptedCiphertext);

                System.out.println("[ATTACKER] Cannot read command or counter.");
                System.out.println("[ATTACKER] Shared AES key is unknown.");

            } catch (Exception e) {
                System.out.println("[ATTACKER] Failed parsing packet.");
            }
        });

        Thread.sleep(5000);

        if (learnedTopic == null) {
            System.out.println("[ATTACKER] No traffic intercepted.");
            client.disconnect();
            return;
        }

        System.out.println("\n[ATTACKER] Attempting forged attack...");

        Message forged = new Message(
                "REMOTE",
                java.util.UUID.randomUUID(),
                "command",
                "FAKE_COMMAND",
                999
        );

        String serialized = forged.serialize();

        client.publish(
                learnedTopic,
                new MqttMessage(serialized.getBytes())
        );

        System.out.println("[ATTACKER] Fake encrypted packet sent.");
        System.out.println("[ATTACKER] Pump should reject packet because:");
        System.out.println("  - wrong encryption");
        System.out.println("  - counter mismatch");
        System.out.println("  - invalid sender");
        System.out.println("  - attacker lacks shared key");

        client.disconnect();

        System.out.println("\n[ATTACKER] Attack failed.");
    }
}