package Main;

import Devices.CGMDevice;
import Windows.CGMWindow;

import javax.swing.*;

public class CGMMain {

    public static void main(String[] args) {
        CGMDevice device = new CGMDevice();
        JFrame frame = new JFrame("CGM");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new CGMWindow(device));

        frame.setSize(400, 300);
        frame.setLocation(0, 0);

        frame.setVisible(true);

        // Reads from mock idea of glucose readings
        Timer t = new Timer(10000, e -> device.publishGlucose());
        t.setRepeats(true);
        t.start();
    }
}