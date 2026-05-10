package Main;

import Devices.PDADevice;
import Panels.PDAPanel;

import javax.swing.*;

public class PDAMain {

    public static void main(String[] args) {
        PDADevice device = new PDADevice();

        JFrame frame = new JFrame("PDA");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new PDAPanel(device));

        frame.setSize(250,350);

        frame.setVisible(true);
    }
}