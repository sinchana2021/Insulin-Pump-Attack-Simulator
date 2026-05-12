package Panels;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panels.PDA represents the Personal Data Assistant. Visualized as a handheld
 * device with a dark log screen that displays a running list of commands
 * and glucose readings received over MQTT.
 *
 * @author you
 * @version 1.0
 */
public class PDAPanel {

    private int x;
    private int y;

    private static final int BODY_W   = 250;
    private static final int BODY_H   = 380;
    private static final int SCREEN_W = 218;  // BODY_W - 32
    private static final int SCREEN_H = 335;  // BODY_H - 45
    private static final int MAX_LINES = 100; // max visible log lines

    private final List<String> log = new ArrayList<>();

    public PDAPanel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Call this from MQTTClient whenever a message arrives. */
    public void addLogEntry(String entry) {
        log.add(entry);
        if (log.size() > MAX_LINES) {
            log.remove(0); // scroll up — drop oldest line
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBody(g2);
        drawScreen(g2);
        drawLog(g2);
    }

    /** Outer rounded-rectangle body. */
    private void drawBody(Graphics2D g) {
        g.setColor(new Color(214, 212, 207));
        g.fillRoundRect(x, y, BODY_W, BODY_H, 28, 28);
        g.setColor(new Color(176, 174, 169));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(x, y, BODY_W, BODY_H, 28, 28);

        // inner inset
        g.setColor(new Color(232, 230, 225));
        g.fillRoundRect(x + 10, y + 10, BODY_W - 20, BODY_H - 20, 20, 20);
        g.setColor(new Color(200, 198, 193));
        g.setStroke(new BasicStroke(0.5f));
        g.drawRoundRect(x + 10, y + 10, BODY_W - 20, BODY_H - 20, 20, 20);
    }

    /** Dark screen background with header bar. */
    private void drawScreen(Graphics2D g) {
        int sx = x + (BODY_W - SCREEN_W) / 2;
        int sy = y + 25;

        // screen background
        g.setColor(new Color(26, 26, 46));
        g.fillRoundRect(sx, sy, SCREEN_W, SCREEN_H, 6, 6);
        g.setColor(new Color(68, 68, 68));
        g.setStroke(new BasicStroke(1.0f));
        g.drawRoundRect(sx, sy, SCREEN_W, SCREEN_H, 6, 6);

        // header bar
        g.setColor(new Color(42, 42, 74));
        g.fillRoundRect(sx, sy, SCREEN_W, 22, 6, 6);
        g.fillRect(sx, sy + 10, SCREEN_W, 12); // square off bottom of header

        // header text
        g.setColor(new Color(136, 136, 204));
        g.setFont(new Font("Monospaced", Font.BOLD, 10));
        FontMetrics fm = g.getFontMetrics();
        String title = "COMMAND LOG";
        g.drawString(title, sx + (SCREEN_W - fm.stringWidth(title)) / 2, sy + 15);
    }

    /** Scrolling log entries on the screen. */
    /** Scrolling log entries on the screen. */
private void drawLog(Graphics2D g) {
    int sx = x + (BODY_W - SCREEN_W) / 2 + 8;
    int sy = y + 25 + 22 + 12; // below header
    int screenBottom = y + 25 + SCREEN_H - 6; // bottom of screen with padding

    g.setFont(new Font("Monospaced", Font.PLAIN, 9));
    FontMetrics fm = g.getFontMetrics();
    int lineH = fm.getHeight() + 2;

    // recalculate MAX_LINES dynamically based on actual screen space
    int availableH = screenBottom - sy;
    int maxVisible = availableH / lineH;

    // only draw the last maxVisible entries
    int startIdx = Math.max(0, log.size() - maxVisible);

    for (int i = startIdx; i < log.size(); i++) {
        String entry = log.get(i);
        int drawY = sy + ((i - startIdx) * lineH) + fm.getAscent();
        if (drawY > screenBottom) break; // safety clip

        if (entry.contains("ALERT")) {
            g.setColor(new Color(204, 204, 68));
        } else {
            g.setColor(new Color(68, 204, 136));
        }
        g.drawString(entry, sx, drawY);
    }

    // blinking cursor at end of log
    int cursorY = sy + ((log.size() - startIdx) * lineH) + fm.getAscent();
    if (cursorY <= screenBottom) {
        g.setColor(new Color(68, 204, 136));
        g.drawString("▮", sx, cursorY);
    }
}

    // --- Getters / Setters ---

    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
}