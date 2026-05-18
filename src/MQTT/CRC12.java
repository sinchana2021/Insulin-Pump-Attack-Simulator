package MQTT;

public class CRC12 {

    public static int compute(String data) {
        int crc = 0;

        for (char c : data.toCharArray()) {
            crc ^= c;
            for (int i = 0; i < 8; i++) {
                if ((crc & 1) != 0) {
                    crc = (crc >> 1) ^ 0x80F;
                } else {
                    crc >>= 1;
                }
            }
        }
        return crc & 0xFFF;
    }
}