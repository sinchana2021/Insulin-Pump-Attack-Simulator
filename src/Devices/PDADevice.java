package Devices;

import MQTT.Message;
import MQTT.MessageHandler;
import MQTT.MySubscriber;
import Panels.PDAPanel;

public class PDADevice implements MessageHandler {

    private PDAPanel pda;
    public PDADevice() {
        pda = new PDAPanel(10,10);
        new MySubscriber(
            "PDA",
            "cgm/glucose",
            this);

        new MySubscriber(
            "PDA",
            "pump/status",
            this);

        new MySubscriber(
            "PDA",
            "remote/command",
            this);
    }

    @Override
    public void onMessage(Message msg) {
        String line = msg.device + ": " + msg.payload;

        pda.addLogEntry(line);
    }

    public PDAPanel getPDA() {
        return pda;
    }
}
