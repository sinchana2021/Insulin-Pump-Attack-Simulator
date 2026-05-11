package Main;

import Devices.RemoteDevice;
import Windows.RemoteWindow;

import javax.swing.*;

public class RemoteMain {

    public static void main(String[] args) {
        RemoteDevice device = new RemoteDevice();

        JFrame frame = new JFrame("Remote");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new RemoteWindow(device));

        frame.setSize(400, 300);
        frame.setLocation(400, 300);

        frame.setVisible(true);
    }
}