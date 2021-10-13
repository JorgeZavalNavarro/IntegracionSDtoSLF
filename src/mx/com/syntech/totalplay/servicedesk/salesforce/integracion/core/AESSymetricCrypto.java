package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.core;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class AESSymetricCrypto {

    public static String encrypt(String cadena) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String retorno = null;
        if (cadena != null && !cadena.isEmpty()) {
            // Generamos una clave de 128 bits adecuada para AES
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            Key key = keyGenerator.generateKey();

            // Alternativamente, una clave que queramos que tenga al menos 16 bytes
            // y nos quedamos con los bytes 0 a 15
            key = new SecretKeySpec("TPE Consultas PDR".getBytes(), 0, 16, "AES");

            // Ver como se puede guardar esta clave en un fichero y recuperarla
            // posteriormente en la clase RSAAsymetricCrypto.java
            // Texto a encriptar
            String texto = cadena;

            // Se obtiene un cifrador AES
            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

            // Se inicializa para encriptacion y se encripta el texto,
            // que debemos pasar como bytes.
            aes.init(Cipher.ENCRYPT_MODE, key);
            byte[] encriptado = aes.doFinal(texto.getBytes());

            
            // Se iniciliza el cifrador para desencriptar, con la
            // misma clave y se desencripta
            aes.init(Cipher.DECRYPT_MODE, key);
            byte[] desencriptado = aes.doFinal(encriptado);

            retorno = new String(encriptado);
        }
        return retorno;
    }

}
