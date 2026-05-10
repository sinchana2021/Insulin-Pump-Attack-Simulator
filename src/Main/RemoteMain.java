package Main;

import Devices.RemoteDevice;
import Panels.RemoteControlPanel;

import javax.swing.*;

public class RemoteMain {

    public static void main(String[] args) {
        RemoteDevice device = new RemoteDevice();

        JFrame frame = new JFrame("Remote");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new RemoteControlPanel(device));

        frame.setSize(300,300);

        frame.setVisible(true);
    }
}