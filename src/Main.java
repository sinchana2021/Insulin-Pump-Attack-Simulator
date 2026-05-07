import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        MQTTClient mqtt = new MQTTClient();

        // Build the UI on the EDT
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Insulin Pump Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 500);
            frame.add(new WorldPanel());
            frame.setVisible(true);
        });

        // Simulate sensor readings every 2 seconds
        while (true) {
            double glucose = Blackboard.getInstance().getGlucoseLevel();
            mqtt.publishGlucose(glucose);
            Thread.sleep(2000);
        }
    }
}
