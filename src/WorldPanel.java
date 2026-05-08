import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WorldPanel extends JPanel implements PropertyChangeListener, MouseListener {

    private final GlucoseSensor sensor = new GlucoseSensor(100, 100);
    private final RemoteControl rc = new RemoteControl(40, 230);

    public WorldPanel() {
        setBackground(new Color(172, 248, 199));
        Blackboard.getInstance().addPropertyChangeListener(this);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        sensor.draw(g);
        Blackboard.getInstance().getInsulinPump().draw(g);
        Blackboard.getInstance().getPDA().draw(g);
        rc.draw(g);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x < getWidth(); x += 20)
            for (int y = 0; y < getHeight(); y += 20)
                g.drawRect(x, y, 20, 20);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        checkInsulinPumpButtons(mx, my);
        checkRemoteControlButtons(mx, my);
    }

    private void checkInsulinPumpButtons(int mx, int my) {
        InsulinPump pump = Blackboard.getInstance().getInsulinPump();
        int px = pump.getX();
        int py = pump.getY();

        // Triangle up button — match coordinates from drawTriangleButtons
        int tx = px + 210;
        int midY = py + 62 + 40; // 40 = SCREEN_H / 2
        if (isInsideTriangle(mx, my,
                tx, midY - 10,
                tx + 35, midY - 45,
                tx + 70, midY - 10)) {
            System.out.println("Up button pressed");
        }

        // Triangle down button
        if (isInsideTriangle(mx, my,
                tx, midY + 10,
                tx + 35, midY + 45,
                tx + 70, midY + 10)) {
            System.out.println("Down button pressed");
        }

        // Circular buttons — blank, back arrow, ACT
        int btnY = py + 152;
        int[] btnX = {px + 42, px + 100, px + 158};
        String[] btnNames = {"Blank button pressed", "Back button pressed", "ACT button pressed"};
        int btnR = 22;

        for (int i = 0; i < 3; i++) {
            if (isInsideCircle(mx, my, btnX[i], btnY, btnR)) {
                System.out.println(btnNames[i]);
            }
        }
    }

    private void checkRemoteControlButtons(int mx, int my) {
        RemoteControl rc = this.rc;
        int rx = rc.getX();
        int ry = rc.getY();

        // S and B round buttons
        int btnY = ry + 80;
        int[] btnX = {rx + 60, rx + 140};
        String[] btnNames = {"S button pressed", "B button pressed"};
        int btnR = 30;

        for (int i = 0; i < 2; i++) {
            if (isInsideCircle(mx, my, btnX[i], btnY, btnR)) {
                System.out.println(btnNames[i]);
            }
        }

        // ACT rectangle button
        int actX = rx + (200 - 140) / 2;
        int actY = ry + 130;
        if (isInsideRect(mx, my, actX, actY, 140, 50)) {
            System.out.println("Remote ACT button pressed");
        }
    }

    // --- Hit test helpers ---

    private boolean isInsideCircle(int mx, int my, int cx, int cy, int r) {
        int dx = mx - cx;
        int dy = my - cy;
        return dx * dx + dy * dy <= r * r;
    }

    private boolean isInsideRect(int mx, int my, int rx, int ry, int w, int h) {
        return mx >= rx && mx <= rx + w && my >= ry && my <= ry + h;
    }

    private boolean isInsideTriangle(int mx, int my,
                                      int x1, int y1,
                                      int x2, int y2,
                                      int x3, int y3) {
        // Barycentric technique
        int d1 = sign(mx, my, x1, y1, x2, y2);
        int d2 = sign(mx, my, x2, y2, x3, y3);
        int d3 = sign(mx, my, x3, y3, x1, y1);
        boolean hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);
        return !(hasNeg && hasPos);
    }

    private int sign(int px, int py, int x1, int y1, int x2, int y2) {
        return (px - x2) * (y1 - y2) - (x1 - x2) * (py - y2);
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (SwingUtilities.isEventDispatchThread()) repaint();
        else SwingUtilities.invokeLater(this::repaint);
    }
}