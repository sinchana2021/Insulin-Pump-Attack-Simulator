package MQTT;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

public class MyPublisher {
    private static final String BROKER = "tcp://localhost:1883";
    private MqttClient client;
    public MyPublisher(String name) {
        try {
            String clientID = name + "-publisher-" + UUID.randomUUID();
            client = new MqttClient(BROKER, clientID, new MemoryPersistence());
            MqttConnectOptions opts = new MqttConnectOptions();

            opts.setAutomaticReconnect(true);
            opts.setCleanSession(true);

            client.connect(opts);
            System.out.println("Publisher connected");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, Message msg) {
        try {
            MqttMessage mqttMessage = new MqttMessage(msg.serialize().getBytes());
            mqttMessage.setQos(2);

            client.publish(topic, mqttMessage);
            System.out.println("Published: " + msg.serialize());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}