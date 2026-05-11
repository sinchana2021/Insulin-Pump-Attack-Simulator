package Windows;

import Devices.RemoteDevice;
import Panels.RemoteControlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RemoteWindow extends JPanel {
    private RemoteDevice device;
    private RemoteControlPanel remote;

    public RemoteWindow(RemoteDevice device) {
        this.device = device;
        this.remote = device.getRemote();

        setBackground(Color.WHITE);

        addMouseListener(
            new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    handleRemoteClick(e.getX(), e.getY());
                }
            });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        remote.draw(g);
    }

    private void handleRemoteClick(
            int mx,
            int my) {

        int rx = remote.getX();
        int ry = remote.getY();

        if(isInsideCircle(
                mx,
                my,
                rx + 60,
                ry + 80,
                30)) {

            device.sendStopCommand();

            repaint();

            return;
        }


        if(isInsideCircle(
                mx,
                my,
                rx + 140,
                ry + 80,
                30)) {

            device.sendBolusCommand();

            repaint();

            return;
        }

//        int actX = rx + (200 - 140) / 2;
//        int actY = ry + 130;
//
//        if(isInsideRect(
//                mx,
//                my,
//                actX,
//                actY,
//                140,
//                50)) {
//
//            device.sendActivateCommand();
//
//            repaint();
//        }
    }

    private boolean isInsideCircle(
            int mx,
            int my,
            int cx,
            int cy,
            int r) {

        int dx = mx - cx;
        int dy = my - cy;

        return dx * dx + dy * dy <= r * r;
    }

//    private boolean isInsideRect(
//            int mx,
//            int my,
//            int rx,
//            int ry,
//            int w,
//            int h) {
//
//        return mx >= rx &&
//                mx <= rx + w &&
//                my >= ry &&
//                my <= ry + h;
//    }
}