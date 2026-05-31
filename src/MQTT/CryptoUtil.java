package MQTT;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtil {

    // shared secret between remote + pump
    private static final String SECRET_KEY =
            "abcdefghijklmnop";

    private static final SecretKeySpec KEY =
            new SecretKeySpec(
                    SECRET_KEY.getBytes(),
                    "AES"
            );

    public static String encrypt(String plainText) {

        try {

            Cipher cipher =
                    Cipher.getInstance("AES");

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    KEY
            );

            byte[] encrypted =
                    cipher.doFinal(
                            plainText.getBytes()
                    );

            return Base64
                    .getEncoder()
                    .encodeToString(encrypted);

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String cipherText) {

        try {

            Cipher cipher =
                    Cipher.getInstance("AES");

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    KEY
            );

            byte[] decoded =
                    Base64
                            .getDecoder()
                            .decode(cipherText);

            byte[] decrypted =
                    cipher.doFinal(decoded);

            return new String(decrypted);

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}