package Devices;

import MQTT.Message;
import MQTT.MyPublisher;
import Panels.CGMPanel;

import java.util.UUID;

public class CGMDevice {

    private CGMPanel monitor;
    private MyPublisher publisher;
    private UUID devicePin;
    private int counter = 0;

    public CGMDevice() {
        monitor = new CGMPanel(100,100);
        publisher = new MyPublisher("CGM");
        this.devicePin = UUID.randomUUID();
    }

    public void publishGlucose() {
        double glucose = monitor.getGlucoseLevel();

        Message msg = new Message(
            "CGM",
            devicePin,
            "glucose",
            String.valueOf(glucose),
            counter++);

        publisher.publish("cgm/glucose", msg);
    }

    public CGMPanel getMonitor() {
        return monitor;
    }
}
