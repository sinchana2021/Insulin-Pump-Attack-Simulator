package Main;

import Devices.PDADevice;
import Windows.PDAWindow;

import javax.swing.*;

public class PDAMain {

    public static void main(String[] args) {
        PDADevice device = new PDADevice();

        JFrame frame = new JFrame("PDA");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new PDAWindow(device));

        frame.setSize(400, 300);
        frame.setLocation(0, 300);

        frame.setVisible(true);
    }
}