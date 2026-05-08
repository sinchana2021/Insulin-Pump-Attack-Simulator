
import org.eclipse.paho.client.mqttv3.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MQTTClient implements MqttCallback {
    private static final String BROKER   = "tcp://localhost:1883";
    private static final String CLIENT_ID = "glucose-sensor";

    // Topics
    public static final String TOPIC_GLUCOSE = "insulinpump/glucose";
    public static final String TOPIC_DOSE    = "insulinpump/dose";

    private MqttClient client;

    public MQTTClient() throws MqttException {
        client = new MqttClient(BROKER, CLIENT_ID);
        client.setCallback(this);
        client.connect();
        client.subscribe(TOPIC_DOSE);   // listen for pump commands
        System.out.println("MQTT connected: " + BROKER);
    }

    public void publishGlucose(double level) throws MqttException {
        String payload = String.valueOf(level);
        client.publish(TOPIC_GLUCOSE,
                       new MqttMessage(payload.getBytes()));
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        if (TOPIC_DOSE.equals(topic)) {
            double dose = Double.parseDouble(payload);
            Blackboard.getInstance().setInsulinDose(dose);

            String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            Blackboard.getInstance().getPDA().addLogEntry("[" + timestamp + "] DOSE: " + dose + " units");
        }
        if (TOPIC_GLUCOSE.equals(topic)) {
            String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            Blackboard.getInstance().getPDA().addLogEntry("[" + timestamp + "] GLUCOSE: " + payload + " mg/dL");
        }
        if (TOPIC_DOSE.equals(topic)) {
            double dose = Double.parseDouble(payload);
            Blackboard.getInstance().setInsulinDose(dose);

            String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            Blackboard.getInstance().getInsulinPump().setScreenText(
                "DOSE RECEIVED\n" + dose + " units\n" + timestamp
            );
            Blackboard.getInstance().getPDA().addLogEntry("[" + timestamp + "] DOSE: " + dose + " units");
        }
    }

    @Override public void connectionLost(Throwable cause) {
        System.err.println("MQTT connection lost: " + cause.getMessage());
    }
    @Override public void deliveryComplete(IMqttDeliveryToken token) {}

    public void disconnect() throws MqttException { client.disconnect(); }
}
