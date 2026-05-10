package Panels;

import java.awt.*;

/**
 * Panels.RemoteControl renders a square remote with two labeled round buttons
 * (S and B) and a rectangular ACT button below them.
 *
 * @author you
 * @version 1.0
 */
public class RemoteControlPanel {

    private int x;
    private int y;

    private static final int BODY_SIZE = 200;
    private static final int BTN_R     = 30;
    private static final int ACT_W     = 140;
    private static final int ACT_H     = 50;

    public RemoteControlPanel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBody(g2);
        drawRoundButtons(g2);
        drawActButton(g2);
    }

    /** Square outer body with inset. */
    private void drawBody(Graphics2D g) {
        g.setColor(new Color(214, 212, 207));
        g.fillRoundRect(x, y, BODY_SIZE, BODY_SIZE, 28, 28);
        g.setColor(new Color(176, 174, 169));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(x, y, BODY_SIZE, BODY_SIZE, 28, 28);

        g.setColor(new Color(232, 230, 225));
        g.fillRoundRect(x + 10, y + 10, BODY_SIZE - 20, BODY_SIZE - 20, 20, 20);
        g.setColor(new Color(200, 198, 193));
        g.setStroke(new BasicStroke(0.5f));
        g.drawRoundRect(x + 10, y + 10, BODY_SIZE - 20, BODY_SIZE - 20, 20, 20);
    }

    /** S (left) and B (right) round buttons. */
    private void drawRoundButtons(Graphics2D g) {
        int btnY  = y + 80;
        int leftX = x + 60;
        int rightX = x + 140;
        String[] labels = {"S", "B"};
        int[] bx = {leftX, rightX};

        for (int i = 0; i < 2; i++) {
            g.setColor(new Color(200, 198, 193));
            g.fillOval(bx[i] - BTN_R, btnY - BTN_R, BTN_R * 2, BTN_R * 2);
            g.setColor(new Color(136, 135, 128));
            g.setStroke(new BasicStroke(1.0f));
            g.drawOval(bx[i] - BTN_R, btnY - BTN_R, BTN_R * 2, BTN_R * 2);

            g.setColor(new Color(44, 44, 42));
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(labels[i],
                    bx[i] - fm.stringWidth(labels[i]) / 2,
                    btnY + fm.getAscent() / 2 - 1);
        }
    }

    /** Rectangular ACT button below the round buttons. */
    private void drawActButton(Graphics2D g) {
        int actX = x + (BODY_SIZE - ACT_W) / 2;
        int actY = y + 130;

        g.setColor(new Color(200, 198, 193));
        g.fillRoundRect(actX, actY, ACT_W, ACT_H, 16, 16);
        g.setColor(new Color(136, 135, 128));
        g.setStroke(new BasicStroke(1.0f));
        g.drawRoundRect(actX, actY, ACT_W, ACT_H, 16, 16);

        g.setColor(new Color(44, 44, 42));
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics fm = g.getFontMetrics();
        String label = "ACT";
        g.drawString(label,
                actX + (ACT_W - fm.stringWidth(label)) / 2,
                actY + (ACT_H + fm.getAscent()) / 2 - 2);
    }

    // --- Getters / Setters ---

    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
}