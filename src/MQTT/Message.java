package MQTT;

import java.util.UUID;

public class Message {
    public String device; // CGM, Pump, PDA, remote
    public UUID devicePin; // Random Pin for that device
    public String type;   // glucose, command, status
//    public String payload; // What to do? STOP, BOLUS, INCREASE ...
//    public int counter; // increase each time payload is given

    public String encryptedData;

    public int crc;
    public String lastBits;

    public Message(String device, UUID devicePin, String type, String payload, int counter) {
        this.device = device;
        this.devicePin = devicePin;
        this.type = type;
        String secureData =
                payload + "|" + counter;

        this.encryptedData =
                CryptoUtil.encrypt(secureData);

        this.crc =
                CRC12.compute(toBitStringNoCRC());

        this.lastBits = "0101";

        System.out.println("CRC" + crc + "DEVICE" + device);
    }

    public String toBitStringNoCRC() {

        return device
                + "|"
                + devicePin
                + "|"
                + type
                + "|"
                + encryptedData;
    }
    public boolean isValid() {

        int computed =
                CRC12.compute(
                        toBitStringNoCRC()
                );

        return computed == crc;
    }

    public String[] decryptData() {

        String decrypted =
                CryptoUtil.decrypt(encryptedData);

        return decrypted.split("\\|");
    }

    public String serialize() {

        return device
                + "|"
                + devicePin
                + "|"
                + type
                + "|"
                + encryptedData
                + "|"
                + crc
                + "|"
                + lastBits;
    }

    public static Message deserialize(String s) {

        String[] parts = s.split("\\|");

        Message msg = new Message(
                parts[0],
                UUID.fromString(parts[1]),
                parts[2],
                "TEMP", // overwritten below
                0
        );

        msg.encryptedData = parts[3];
        msg.crc = Integer.parseInt(parts[4]);
        msg.lastBits = parts[5];

        return msg;
    }


}