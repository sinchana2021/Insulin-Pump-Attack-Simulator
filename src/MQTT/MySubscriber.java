package MQTT;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

public class MySubscriber implements MqttCallback {

    private static final String BROKER = "tcp://localhost:1883";
    private MqttClient client;
    private MessageHandler handler;

    public MySubscriber(String name, String topic, MessageHandler handler) { // name = "Who am I"
        this.handler = handler;

        try {
            String clientID = name + "-subscriber-" + UUID.randomUUID(); // allows for us to run this multiple times

            client = new MqttClient(BROKER, clientID, new MemoryPersistence());

            MqttConnectOptions opts = new MqttConnectOptions();

            opts.setAutomaticReconnect(true);
            opts.setCleanSession(true);

            client.connect(opts);
            client.setCallback(this);
            client.subscribe(topic);
            System.out.println("Subscribed to: " + topic);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {

        String payload = new String(mqttMessage.getPayload());

        Message msg = Message.deserialize(payload);

        handler.onMessage(msg);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    @Override
    public void connectionLost(Throwable t) {
        t.printStackTrace();
    }
}