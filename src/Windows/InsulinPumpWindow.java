package Windows;

import Devices.InsulinPumpDevice;
import Panels.InsulinPumpPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InsulinPumpWindow extends JPanel {

    private InsulinPumpDevice device;
    private InsulinPumpPanel pump;

    public InsulinPumpWindow(InsulinPumpDevice device) {
        this.device = device;
        this.pump = device.getPump();

        setBackground(Color.WHITE);

        addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handlePumpClick(e.getX(), e.getY());
                }
            });

        new Timer(50, e -> repaint()).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        pump.draw(g);
    }

    private void handlePumpClick(int mx, int my) {
        int px = pump.getX();
        int py = pump.getY();

        int tx = px + 210;
        int midY = py + 62 + 40;

        if(isInsideTriangle(
                mx, my,
                tx, midY - 10,
                tx + 35, midY - 45,
                tx + 70, midY - 10)) {

            device.increaseDose();
            repaint();
            return;
        }

        if(isInsideTriangle(
                mx, my,
                tx, midY + 10,
                tx + 35, midY + 45,
                tx + 70, midY + 10)) {

            device.decreaseDose();
            repaint();
            return;
        }

        // !! REMOTE UI FOR OTHER THINGS --> JUST EXPRESS BOLUS
        int btnY = py + 152;
        int actX = px + 158;

        if(isInsideCircle(
                mx, my,
                actX,
                btnY,
                22)) {

            device.expressBOLUS();

            repaint();
        }
    }

    private boolean isInsideCircle(int mx, int my, int cx, int cy, int r) {
        int dx = mx - cx;
        int dy = my - cy;

        return dx * dx + dy * dy <= r * r;
    }

    private boolean isInsideTriangle(int mx, int my, int x1, int y1, int x2, int y2, int x3, int y3) {
        int d1 = sign(mx, my, x1, y1, x2, y2);

        int d2 =
                sign(mx, my,
                        x2, y2,
                        x3, y3);

        int d3 =
                sign(mx, my,
                        x3, y3,
                        x1, y1);

        boolean hasNeg =
                (d1 < 0) ||
                        (d2 < 0) ||
                        (d3 < 0);

        boolean hasPos =
                (d1 > 0) ||
                        (d2 > 0) ||
                        (d3 > 0);

        return !(hasNeg && hasPos);
    }

    private int sign(int px, int py, int x1, int y1, int x2, int y2) {
        return (px - x2) * (y1 - y2) - (x1 - x2) * (py - y2);
    }
}
