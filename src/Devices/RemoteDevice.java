package Devices;

import MQTT.Message;
import MQTT.MyPublisher;
import Panels.RemoteControlPanel;

import java.util.UUID;

public class RemoteDevice {
    private RemoteControlPanel remote;
    private MyPublisher publisher;
    private int counter = 0;
    private UUID devicePin;

    public RemoteDevice() {
        remote = new RemoteControlPanel(20,20);

        publisher = new MyPublisher("REMOTE");
    }

    public void sendBolusCommand() {
        Message msg = new Message(
            "REMOTE",
            devicePin,
            "command",
            "BOLUS",
            counter++);

        publisher.publish(
            "remote/command",
            msg);
    }

    public RemoteControlPanel getRemote() {
        return remote;
    }
}