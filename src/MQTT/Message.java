package MQTT;

import java.util.UUID;

public class Message {
    public String device; // CGM, Pump, PDA, remote
    public UUID devicePin; // Random Pin for that device
    public String type;   // glucose, command, status
    public String payload; // What to do? STOP, BOLUS, INCREASE ...
    public int counter; // increase each time payload is given
    public int crc;
    public String lastBits;

    public Message(String device, UUID devicePin, String type, String payload, int counter) {
        this.device = device;
        this.devicePin = devicePin;
        this.type = type;
        this.payload = payload;
        this.counter = counter;
        this.crc = CRC12.compute(this.toBitStringNoCRC());
        this.lastBits = "0101";

        System.out.println("CRC" + crc + "DEVICE" + device);
    }

    public String toBitStringNoCRC() {
        return device + "|" + devicePin.toString() + "|" + payload + "|" + counter;
    }

    public boolean isValid() {
        return CRC12.compute(this.toBitStringNoCRC()) == crc;
    }

    // follows structure from paper
    public String serialize() {
        return device + "|" + devicePin + "|" + type + "|" + payload + "|" + counter + "|" + crc + "|"+ lastBits;
    }

    public static Message deserialize(String s) {
        String[] parts = s.split("\\|");
        return new Message(parts[0], UUID.fromString(parts[1]), parts[2], parts[3], Integer.parseInt(parts[4]));
    }
}