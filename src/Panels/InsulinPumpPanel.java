package Panels;

import java.awt.*;

/**
 * Panels.InsulinPump renders a handheld insulin pump device with a screen,
 * up/down triangle navigation buttons, and three circular action buttons.
 *
 * @author you
 * @version 1.0
 */
public class InsulinPumpPanel {

    private int x;
    private int y;
    private double insulinDose; // units to deliver

    // Device dimensions
    private static final int BODY_W  = 300;
    private static final int BODY_H  = 200;
    private static final int SCREEN_W = 130;
    private static final int SCREEN_H = 80;
    private static final int BTN_R   = 22;

    private String screenText = "Waiting...";

    public void setScreenText(String text) { this.screenText = text; }

    public InsulinPumpPanel(int x, int y) {
        this.x = x;
        this.y = y;
        this.insulinDose = 0.0;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBody(g2);
        drawScreen(g2);
        drawTriangleButtons(g2);
        drawCircularButtons(g2);
    }

    /** Outer rounded-rectangle pump body. */
    private void drawBody(Graphics2D g) {
        g.setColor(new Color(214, 212, 207));
        g.fillRoundRect(x, y, BODY_W, BODY_H, 36, 36);
        g.setColor(new Color(176, 174, 169));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(x, y, BODY_W, BODY_H, 36, 36);

        // inner inset
        g.setColor(new Color(232, 230, 225));
        g.fillRoundRect(x + 8, y + 8, BODY_W - 16, BODY_H - 16, 28, 28);
        g.setColor(new Color(200, 198, 193));
        g.setStroke(new BasicStroke(0.5f));
        g.drawRoundRect(x + 8, y + 8, BODY_W - 16, BODY_H - 16, 28, 28);
    }

    /** Green LCD-style screen. */
    private void drawScreen(Graphics2D g) {
        int sx = x + 22;
        int sy = y + 22;

        g.setColor(new Color(200, 223, 200));
        g.fillRoundRect(sx, sy, SCREEN_W, SCREEN_H, 8, 8);
        g.setColor(new Color(138, 174, 138));
        g.setStroke(new BasicStroke(1.0f));
        g.drawRoundRect(sx, sy, SCREEN_W, SCREEN_H, 8, 8);

        // header
        g.setColor(new Color(100, 140, 100));
        g.setFont(new Font("Monospaced", Font.BOLD, 9));
        FontMetrics fm = g.getFontMetrics();
        String header = "INSULIN PUMP";
        g.drawString(header, sx + (SCREEN_W - fm.stringWidth(header)) / 2, sy + 12);

        // divider line
        g.setColor(new Color(138, 174, 138));
        g.drawLine(sx + 4, sy + 16, sx + SCREEN_W - 4, sy + 16);

        // screen text (word-wrap by splitting on newlines)
        g.setColor(new Color(44, 80, 44));
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        fm = g.getFontMetrics();
        String[] lines = screenText.split("\n");
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], sx + 6, sy + 30 + (i * (fm.getHeight() + 2)));
        }
    }

    /** Up and down triangle buttons to the right of the screen. */
    private void drawTriangleButtons(Graphics2D g) {
        int tx = x + 210;  // right of screen
        int midY = y + 62 + SCREEN_H / 2;

        g.setColor(new Color(176, 174, 169));
        g.setStroke(new BasicStroke(0.5f));

        // up triangle
        int[] uxPoints = {tx, tx + 35, tx + 70};
        int[] uyPoints = {midY - 10, midY - 45, midY - 10};
        g.fillPolygon(uxPoints, uyPoints, 3);
        g.setColor(new Color(136, 135, 128));
        g.drawPolygon(uxPoints, uyPoints, 3);

        // down triangle
        g.setColor(new Color(176, 174, 169));
        int[] dxPoints = {tx, tx + 35, tx + 70};
        int[] dyPoints = {midY + 10, midY + 45, midY + 10};
        g.fillPolygon(dxPoints, dyPoints, 3);
        g.setColor(new Color(136, 135, 128));
        g.drawPolygon(dxPoints, dyPoints, 3);
    }

    /** Three circular buttons: blank, back arrow, ACT. */
    private void drawCircularButtons(Graphics2D g) {
        int btnY = y + 152;
        int[] btnX = {x + 42, x + 100, x + 158};

        for (int i = 0; i < 1; i++) {
            g.setColor(new Color(200, 198, 193));
            g.fillOval(btnX[i] - BTN_R, btnY - BTN_R, BTN_R * 2, BTN_R * 2);
            g.setColor(new Color(136, 135, 128));
            g.setStroke(new BasicStroke(1.0f));
            g.drawOval(btnX[i] - BTN_R, btnY - BTN_R, BTN_R * 2, BTN_R * 2);
        }

        // back arrow on button 2
        // g.setColor(new Color(95, 94, 90));
        // int[] arrowX = {btnX[1] + 10, btnX[1] - 10, btnX[1] + 10};
        // int[] arrowY = {btnY - 8,     btnY,          btnY + 8};
        // g.fillPolygon(arrowX, arrowY, 3);

        // ACT label on button 3
        // g.setColor(new Color(44, 44, 42));
        // g.setFont(new Font("SansSerif", Font.BOLD, 11));
        // FontMetrics fm = g.getFontMetrics();
        // String act = "ACT";
        // g.drawString(act, btnX[2] - fm.stringWidth(act) / 2, btnY + fm.getAscent() / 2 - 1);
    }

    // --- Getters / Setters ---

    public double getInsulinDose() { return insulinDose; }
    public void setInsulinDose(double dose) { this.insulinDose = dose; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
}
