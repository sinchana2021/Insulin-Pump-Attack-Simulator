package Windows;

import Devices.PDADevice;

import javax.swing.*;
import java.awt.*;

public class PDAWindow extends JPanel {

    private PDADevice device;

    public PDAWindow(PDADevice device) {

        this.device = device;

        setBackground(Color.WHITE);
        Timer t = new Timer(100, e -> repaint());
        t.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        device.getPDA().draw(g);
    }
}