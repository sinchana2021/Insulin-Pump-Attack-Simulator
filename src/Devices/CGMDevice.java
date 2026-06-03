package Devices;

import MQTT.Message;
import MQTT.MessageHandler;
import MQTT.MyPublisher;
import MQTT.MySubscriber;
import Panels.CGMPanel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CGMDevice implements MessageHandler {

    private CGMPanel cgm;
    private MyPublisher publisher;
    private UUID devicePin;
    private int counter = 0;
    private double currentGlucose = 0.0;

    private List<Double> readings;
    private int index = 0;
    private double insulinEffect = 0.0;

    public CGMDevice() {
        cgm = new CGMPanel(100,100);
        this.devicePin = UUID.randomUUID();
        publisher = new MyPublisher("CGM");

        readings = loadReadings();

        new MySubscriber(
            "CGM",
            "pump/status",
            this
        );

    }

    private List<Double> loadReadings() {
        List<Double> values = new ArrayList<>();

        try {
            String json = Files.readString(Paths.get("src/glucose.json"));

            json = json
                    .replace("[","")
                    .replace("]","")
                    .replace("\n","")
                    .replace(" ","");

            String[] parts = json.split(",");

            for(String p : parts) {
                values.add(Double.parseDouble(p));
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        return values;
    }

    public void publishGlucose() {
        if (index >= readings.size()) return;
        double base = readings.get(index);

        double adjusted = base - insulinEffect;

        insulinEffect *= 0.85;

        if (adjusted < 40) adjusted = 40;

        currentGlucose = adjusted;

        Message msg = new Message(
                "CGM",
                devicePin,
                "glucose",
                String.valueOf(adjusted),
                counter++
        );

        publisher.publish("cgm/glucose", msg);

        System.out.println("Published glucose: " + adjusted);
        cgm.setGlucoseLevel(adjusted);
        index++;
    }

    public double getCurrentGlucose() {
        return currentGlucose;
    }

    public CGMPanel getCGM() {
        return cgm;
    }

    @Override
    public void onMessage(Message msg) {
        if (!msg.isValid()) {
            System.out.println("CGM dropped bad CRC packet");
            return;
        }

        String[] data = msg.decryptData();

        String payload = data[0];

        if (msg.type.equals("status")) {

            double units =
                    Double.parseDouble(payload);

            insulinEffect += units * 6;

            if (insulinEffect > 100)
                insulinEffect = 100;
        }
    }
}