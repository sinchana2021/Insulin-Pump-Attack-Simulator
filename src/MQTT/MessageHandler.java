package MQTT;

public interface MessageHandler {
    void onMessage(Message msg);
}
