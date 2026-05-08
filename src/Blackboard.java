import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Blackboard {
    private static Blackboard instance;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private double glucoseLevel = 100.0;  // mg/dL
    private double insulinDose  = 0.0;    // units
    private PDA pda = new PDA(380, 10);
    private InsulinPump pump = new InsulinPump(250, 250);

    public PDA getPDA() { return pda; }

    public InsulinPump getInsulinPump() { return pump; }

    public static Blackboard getInstance() {
        if (instance == null) instance = new Blackboard();
        return instance;
    }

    public double getGlucoseLevel() { return glucoseLevel; }
    public void setGlucoseLevel(double v) {
        double old = glucoseLevel;
        glucoseLevel = v;
        pcs.firePropertyChange("glucoseLevel", old, v);
    }

    public double getInsulinDose() { return insulinDose; }
    public void setInsulinDose(double v) {
        double old = insulinDose;
        insulinDose = v;
        pcs.firePropertyChange("insulinDose", old, v);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) { pcs.addPropertyChangeListener(l); }
    public void removePropertyChangeListener(PropertyChangeListener l) { pcs.removePropertyChangeListener(l); }
}