package cn.lifanko.xiaomiao;
/**
 * Created By IntelliJ IDEA 15.0.2
 * Author lifanko lee
 * Date 2017/10/28
 */

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class EncryptUtils {
    private Key aesKey = null;
    private Cipher cipher = null;

    synchronized private void init() throws Exception {
        String keyStr = "power by lifanko";
        if (keyStr.length() != 16) {
            throw new Exception("bad aes key configured");
        }
        if (aesKey == null) {
            aesKey = new SecretKeySpec(keyStr.getBytes(), "AES");
            cipher = Cipher.getInstance("AES");
        }
    }

    synchronized public String encrypt(String text) throws Exception {
        init();
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return toHexString(cipher.doFinal(text.getBytes()));
    }

    synchronized public String decrypt(String text) throws Exception {
        init();
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(cipher.doFinal(toByteArray(text)));
    }

    public static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    public static void main(String[] args) {
        EncryptUtils test = new EncryptUtils();
        try {
            System.out.println(test.encrypt("test String"));
            System.out.println(test.decrypt(test.encrypt("Test String")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
