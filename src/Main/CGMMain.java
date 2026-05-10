package Main;

import Devices.CGMDevice;
import Panels.CGMPanel;

import javax.swing.*;

public class CGMMain {

    public static void main(String[] args) {
        CGMDevice device = new CGMDevice();
        JFrame frame = new JFrame("CGM");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new CGMPanel(device));

        frame.setSize(300,300);

        frame.setVisible(true);

        // !! Read from some json of commands to publish at multiple intervals i.e. every 10 seconds
        new Timer(3000, e -> {
            device.publishGlucose();
        }).start();
    }
}