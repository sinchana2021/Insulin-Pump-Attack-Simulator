//import java.awt.*;
//import java.beans.PropertyChangeSupport;
//import java.util.Vector;
//
///**
// * Blackboard is a singleton that holds the state of all players
// * in the game, including "me".
// *
// * @author javiergs
// * @version 1.0
// */
//public class Blackboard extends PropertyChangeSupport {
//
//    public static final String BROKER = "tcp://broker.hivemq.com:1883";
//    public static final String TOPIC = "insulin";
//    private int glucose;
//    private String command;
//    private String pumpStatus;
//    private static volatile Blackboard instance;
//
//    private Blackboard() {
//        super(new Object());
//    }
//
//    public static Blackboard getInstance() {
//        if (instance == null) {
//            synchronized (Blackboard.class) {
//                if (instance == null) {
//                    instance = new Blackboard();
//                }
//            }
//        }
//        return instance;
//    }
//
//    public void setGlucose(int value) {
//        this.glucose = value;
//        firePropertyChange("glucose", null, value);
//    }
//
//    public int getGlucose() {
//        return glucose;
//    }
//
//    public void setCommand(String cmd) {
//        this.command = cmd;
//        firePropertyChange("command", null, cmd);
//    }
//
//    public void setPumpStatus(String status) {
//        this.pumpStatus = status;
//        firePropertyChange("status", null, status);
//    }
//
//}
