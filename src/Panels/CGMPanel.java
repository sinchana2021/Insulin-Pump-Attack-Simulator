package Panels;

import java.awt.*;

/**
 * CGM represents the wearable circular patch that the patient
 * applies directly to the skin. It continuously monitors interstitial
 * glucose levels and exposes a current reading for other components.
 *
 * @author you
 * @version 1.0
 */
public class CGMPanel {

    private int x;
    private int y;
    private double glucoseLevel; // mg/dL

    // Patch dimensions
    private static final int ADHESIVE_RADIUS = 50;
    private static final int HOUSING_RADIUS  = 35;

    public CGMPanel(int x, int y) {
        this.x = x;
        this.y = y;
        this.glucoseLevel = 100.0; // default resting value
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawAdhesiveRing(g2);
        drawSensorHousing(g2);
        drawGlucoseText(g2);
    }

    /** Outer beige adhesive layer that sticks to the skin. */
    private void drawAdhesiveRing(Graphics2D g) {
        g.setColor(new Color(232, 213, 176));   // beige adhesive
        g.fillOval(x - ADHESIVE_RADIUS, y - ADHESIVE_RADIUS,
                   ADHESIVE_RADIUS * 2, ADHESIVE_RADIUS * 2);
        g.setColor(new Color(201, 180, 138));
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(x - ADHESIVE_RADIUS, y - ADHESIVE_RADIUS,
                   ADHESIVE_RADIUS * 2, ADHESIVE_RADIUS * 2);
    }

    /** White plastic sensor body sits on top of the adhesive ring. */
    private void drawSensorHousing(Graphics2D g) {
        g.setColor(new Color(240, 239, 237));   // off-white shell
        g.fillOval(x - HOUSING_RADIUS, y - HOUSING_RADIUS,
                   HOUSING_RADIUS * 2, HOUSING_RADIUS * 2);
        g.setColor(new Color(205, 203, 199));
        g.setStroke(new BasicStroke(1.0f));
        g.drawOval(x - HOUSING_RADIUS, y - HOUSING_RADIUS,
                   HOUSING_RADIUS * 2, HOUSING_RADIUS * 2);
    }

    private void drawGlucoseText(Graphics2D g) {
        g.setFont(new Font("SansSerif", Font.BOLD, 12));

        if (glucoseLevel < 70) {
            g.setColor(new Color(220, 60, 60)); // low = red
        } else if (glucoseLevel > 180) {
            g.setColor(new Color(220, 140, 40)); // high = orange
        } else {
            g.setColor(new Color(60, 180, 100)); // normal = green
        }

        String text = String.format("Glucose: %.1f mg/dL", glucoseLevel);

        g.drawString(text, x - 40, y + 80);
    }

    
    // --- Getters / Setters ---

    public double getGlucoseLevel() { return glucoseLevel; }
    public void setGlucoseLevel(double level) { this.glucoseLevel = level; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
}