package Devices;

import MQTT.Message;
import MQTT.MessageHandler;
import MQTT.MyPublisher;
import MQTT.MySubscriber;
import Panels.InsulinPumpPanel;

import javax.swing.Timer;
import java.util.UUID;


public class InsulinPumpDevice implements MessageHandler {

    private InsulinPumpPanel pump;
    private MyPublisher publisher;
    private int counter = 0;
    private UUID devicePin;
    private int myTargetGluose = 110;
    private int ISF = 50; // determines num of units
    private Timer autoDoseTimer;

    private int lastCounter = -1;

    public InsulinPumpDevice() {
        pump = new InsulinPumpPanel(20,20);
        publisher = new MyPublisher("PUMP");
        this.devicePin = UUID.randomUUID();

        new MySubscriber(
            "PUMP",
            "cgm/glucose",
            this);

        new MySubscriber(
            "PUMP",
            "remote/command",
            this);

        autoDoseTimer = new Timer(30000, e -> {
            double insulinDose = pump.getInsulinDose();
            if (insulinDose > 0) {
                deliverUnits(insulinDose);
            }
        });

        autoDoseTimer.start();
    }

    // External messages from other devices
    @Override
    public void onMessage(Message msg) {
        System.out.println(msg.counter);
        System.out.println(lastCounter);
        if (msg.counter == lastCounter) {
            System.out.println("DROP: replay detected");
            return;
        }
        lastCounter = msg.counter;

        if (!msg.isValid()) {
            System.out.println("DROP: CRC failed");
            return;
        }


        // subscriber to CGM
        if(msg.type.equals("glucose")) { // subscriber to CGM
            double glucose = Double.parseDouble(msg.payload);

            if (glucose > myTargetGluose) {
                double unitsCorrection = (glucose - myTargetGluose) / ISF;

                this.deliverUnits(unitsCorrection);
            }
        }
        // subscriber to Remote
        else if (msg.type.equals("command")) {
            if (msg.payload.equals("BOLUS")) {
                this.deliverUnits(2.0);
            }
            else if (msg.payload.equals("STOP")) {
                this.deliverUnits(0.0);
            }
        }

    }

    public void deliverUnits(double units) {
        String screenText = "Delivering " + String.format("%.2f", units) + " units";

        this.sendUpdatedDose(units, screenText);
    }

    // FOR publisher to PDA
    private void sendUpdatedDose(double dose, String screenText) {
        Message msg = new Message(
            "PUMP",
            devicePin,
            "status",
            String.valueOf(dose), counter++);

        publisher.publish("pump/status", msg);

        pump.setInsulinDose(dose);
        pump.setScreenText(screenText);
    }


    // Internal messages from insulin pump
    public void increaseDose() {
        double dose = pump.getInsulinDose();

        pump.setInsulinDose(dose + 1);
        pump.setScreenText("Dose: " + String.format("%.2f", pump.getInsulinDose()));
    }

    public void decreaseDose() {
        double dose = pump.getInsulinDose();

        if(dose > 0)
            dose -= 1;

        pump.setInsulinDose(dose);
        pump.setScreenText("Dose: " + String.format("%.2f", pump.getInsulinDose()));
    }

    public void expressBOLUS() {
        double dose = 2.0;

        pump.setInsulinDose(dose);
        pump.setScreenText("Dose: " + String.format("%.2f", dose));
    }

    public InsulinPumpPanel getPump() {
        return pump;
    }



}

