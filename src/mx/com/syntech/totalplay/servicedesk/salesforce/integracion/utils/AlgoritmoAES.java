package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
/**
 *
 * @author dell
 */
public class AlgoritmoAES {

    private static final String llaveSimetrica = "1263446597847648";

    public String encDatos(String cadena) {
        byte[] aError = null;
        SecretKeySpec key = new SecretKeySpec(llaveSimetrica.getBytes(), "AES");
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] datosCifrados = cipher.doFinal(cadena.getBytes());

            return new sun.misc.BASE64Encoder().encode(datosCifrados);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String dencDatos(String datosCifrados) {
        SecretKeySpec key = new SecretKeySpec(llaveSimetrica.getBytes(), "AES");
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] datosDecifrados = cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(datosCifrados));
            return new String(datosDecifrados);

        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
