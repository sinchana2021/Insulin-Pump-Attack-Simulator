package Main;

import javax.swing.*;
import Devices.InsulinPumpDevice;
import Windows.InsulinPumpWindow;

public class InsulinPumpMain {

    public static void main(String[] args) {
        InsulinPumpDevice device = new InsulinPumpDevice();

        JFrame frame = new JFrame("Pump");

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        frame.add(new InsulinPumpWindow(device));

        frame.setSize(400,300);

        frame.setVisible(true);

        // Every 30 seconds give dosage based on current dosage to give
        new Timer(30000, e -> {
            double insulinDose = device.getPump().getInsulinDose();

            device.deliverUnits(insulinDose);
        }).start();
    }
}