import javax.swing.*;
import java.awt.*;

/**
 * GlucoseSensor represents the wearable circular patch that the patient
 * applies directly to the skin. It continuously monitors interstitial
 * glucose levels and exposes a current reading for other components.
 *
 * @author you
 * @version 1.0
 */
public class GlucoseSensor {

    private int x;
    private int y;
    private double glucoseLevel; // mg/dL

    // Patch dimensions
    private static final int ADHESIVE_RADIUS = 50;
    private static final int HOUSING_RADIUS  = 35;

    public GlucoseSensor(int x, int y) {
        this.x = x;
        this.y = y;
        this.glucoseLevel = 100.0; // default resting value
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawAdhesiveRing(g2);
        drawSensorHousing(g2);
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

    
    // --- Getters / Setters ---

    public double getGlucoseLevel() { return glucoseLevel; }
    public void setGlucoseLevel(double level) { this.glucoseLevel = level; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
}