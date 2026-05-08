import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WorldPanel extends JPanel implements PropertyChangeListener {

    private final GlucoseSensor sensor = new GlucoseSensor(100, 100);
    private final InsulinPump pump = new InsulinPump(250, 250);
    private final RemoteControl rc = new RemoteControl(10, 250);


    public WorldPanel() {
        setBackground(new Color(172, 248, 199));
        Blackboard.getInstance().addPropertyChangeListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        sensor.draw(g);
        pump.draw(g);
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
    public void propertyChange(PropertyChangeEvent evt) {
        if (SwingUtilities.isEventDispatchThread()) repaint();
        else SwingUtilities.invokeLater(this::repaint);
    }
}