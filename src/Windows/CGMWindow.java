package Windows;

import Devices.CGMDevice;

import javax.swing.*;
import java.awt.*;


public class CGMWindow extends JPanel {

    private CGMDevice device;
    private final Timer timer;

    public CGMWindow(CGMDevice device) {
        this.device = device;

        setBackground(Color.WHITE);

        timer = new Timer(100, e -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        device.getCGM().draw(g);
    }
}