import java.awt.*;

/**
 * PDA represents the Personal Data Assistant — a database that stores
 * glucose readings and insulin delivery history. Visualized as a cylinder
 * with a data-input box and arrow on the left indicating writes.
 *
 * @author you
 * @version 1.0
 */
public class PDA {

    private int x;
    private int y;

    // Cylinder dimensions
    private static final int CYL_W       = 140;
    private static final int CYL_H       = 160;
    private static final int ELLIPSE_RX  = 70;
    private static final int ELLIPSE_RY  = 18;

    // Input box dimensions
    private static final int BOX_W = 110;
    private static final int BOX_H = 50;

    public PDA(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawInputBox(g2);
        drawArrow(g2);
        drawCylinder(g2);
    }

    /** Input box to the left of the cylinder. */
    private void drawInputBox(Graphics2D g) {
        int bx = x - BOX_W - 60;
        int by = y + CYL_H / 2 - BOX_H / 2;

        g.setColor(new Color(232, 230, 225));
        g.fillRoundRect(bx, by, BOX_W, BOX_H, 8, 8);
        g.setColor(new Color(176, 174, 169));
        g.setStroke(new BasicStroke(1.0f));
        g.drawRoundRect(bx, by, BOX_W, BOX_H, 8, 8);

        g.setColor(new Color(95, 94, 90));
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        FontMetrics fm = g.getFontMetrics();
        String line1 = "Incoming";
        String line2 = "data";
        int cx = bx + BOX_W / 2;
        g.drawString(line1, cx - fm.stringWidth(line1) / 2, by + 18);
        g.drawString(line2, cx - fm.stringWidth(line2) / 2, by + 33);
    }

    /** Arrow from input box to cylinder. */
    private void drawArrow(Graphics2D g) {
        int arrowStartX = x - 60;
        int arrowEndX   = x;
        int arrowY      = y + CYL_H / 2;

        g.setColor(new Color(136, 135, 128));
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(arrowStartX, arrowY, arrowEndX - 10, arrowY);

        // arrowhead
        int[] ax = {arrowEndX - 10, arrowEndX - 20, arrowEndX - 20};
        int[] ay = {arrowY, arrowY - 6, arrowY + 6};
        g.setColor(new Color(136, 135, 128));
        g.fillPolygon(ax, ay, 3);
    }

    /** Database cylinder: body rect + top and bottom ellipses + ring lines. */
    private void drawCylinder(Graphics2D g) {
        // body
        g.setColor(new Color(181, 212, 244));
        g.fillRect(x, y + ELLIPSE_RY, CYL_W, CYL_H);

        // dashed ring lines
        g.setColor(new Color(55, 138, 221));
        g.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1f, new float[]{4f, 3f}, 0f));
        for (int i = 1; i <= 3; i++) {
            int ry = y + ELLIPSE_RY + (CYL_H / 4) * i;
            g.drawOval(x, ry - ELLIPSE_RY, CYL_W, ELLIPSE_RY * 2);
        }
        g.setStroke(new BasicStroke(1.0f));

        // bottom ellipse (drawn before top so top overlaps cleanly)
        g.setColor(new Color(133, 183, 235));
        g.fillOval(x, y + ELLIPSE_RY + CYL_H - ELLIPSE_RY, CYL_W, ELLIPSE_RY * 2);
        g.setColor(new Color(55, 138, 221));
        g.drawOval(x, y + ELLIPSE_RY + CYL_H - ELLIPSE_RY, CYL_W, ELLIPSE_RY * 2);

        // cylinder outline sides
        g.setColor(new Color(55, 138, 221));
        g.drawLine(x, y + ELLIPSE_RY, x, y + ELLIPSE_RY + CYL_H);
        g.drawLine(x + CYL_W, y + ELLIPSE_RY, x + CYL_W, y + ELLIPSE_RY + CYL_H);

        // top ellipse
        g.setColor(new Color(230, 241, 251));
        g.fillOval(x, y, CYL_W, ELLIPSE_RY * 2);
        g.setColor(new Color(55, 138, 221));
        g.drawOval(x, y, CYL_W, ELLIPSE_RY * 2);
    }

    // --- Getters / Setters ---

    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
}