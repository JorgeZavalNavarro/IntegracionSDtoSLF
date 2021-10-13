/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.base64;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 * 
 * @author Jorge Zavala Navarro
 */
public class ConversorBase64Bean {
    
    public String convertirArchivoTOArchivoBase64(String archivoOrigen, String archivoDestino) throws ConversorBase64Exception {
        String retorno = null;
        if (archivoOrigen != null && !archivoOrigen.isEmpty()
                && archivoDestino != null && !archivoDestino.isEmpty()) {

            try {
                // leemos en cadena el contenido del archivo origen
                String contenidoBase64 = convertirArchivoTOBase64(archivoOrigen);
                byte[] contenidoByteBase64 = contenidoBase64.getBytes();
                FileOutputStream fos = new FileOutputStream(archivoDestino);
                fos.write(contenidoByteBase64);
                fos.close();

                // Escribir archivo
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ConversorBase64Exception(ex);
            }

        } else {
            throw new ConversorBase64Exception("No se está recibiendo el nombre o los nombres de los archivos a procesar");
        }
        return retorno;
    }

    public String convertirArchivoTOBase64(String rutaArchivo) throws ConversorBase64Exception {
        String retorno = null;
        File file = new File(rutaArchivo);
        
        if (rutaArchivo != null && !rutaArchivo.isEmpty()) {
            try {
                InputStream finput = new FileInputStream(rutaArchivo);
                retorno = convertirInputStreamTOBase64(finput);
                finput.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ConversorBase64Exception(ex);
            }
        } else {
            throw new ConversorBase64Exception("No se está recibiendo el nombre de archivo a procesar");
        }
        return retorno;
    }
    
    @Deprecated
    public String convertirInputStreamTOBase64_noUsar(InputStream isAdjunto, long lonArchivo) throws ConversorBase64Exception {
        String retorno = null;
        if (isAdjunto != null) {
            try {
                
                byte[] imageBytes = new byte[(int) lonArchivo];
                isAdjunto.read(imageBytes, 0, imageBytes.length);
                String adjuntoBase64 = Base64.encodeBase64String(imageBytes);
                retorno = adjuntoBase64;
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ConversorBase64Exception(ex);
            }
        } else {
            throw new ConversorBase64Exception("No se está recibiendo el contenido para procesar !!");
        }
        return retorno;
    }

    public String convertirInputStreamTOBase64(InputStream isAdjunto) throws ConversorBase64Exception {
        String retorno = null;
        if (isAdjunto != null) {
            try {
                byte[] imageBytes = IOUtils.toByteArray(isAdjunto);
                String adjuntoBase64 = Base64.encodeBase64String(imageBytes);
                retorno = adjuntoBase64;
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ConversorBase64Exception(ex);
            }
        } else {
            throw new ConversorBase64Exception("No se está recibiendo el contenido para procesar !!");
        }
        return retorno;
    }
    
    public void crearArchivoFROMInputStream(InputStream is, String archivoDestino) throws ConversorBase64Exception {

        // Guardamos en el archivo el contenido del inputStream
        if (is != null && !archivoDestino.isEmpty()) {
            try {
                
                // Definimos el output stream
                OutputStream outStream = new FileOutputStream(archivoDestino);

                // Guardamos el inpustream
                byte[] buffer = new byte[8 * 1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                
                // cerramos
                is.close();
                outStream.close();
                        
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ConversorBase64Exception(ex);
            }
        } else {
            throw new ConversorBase64Exception("No se está recibiendo el contenido para procesar !!");
        }
    }

    public InputStream convertirBase64TOInputStream(String contenidoBase64) throws ConversorBase64Exception {
        InputStream retorno = null;
        if (contenidoBase64 != null && !contenidoBase64.isEmpty()) {
            try {

                // Decodificamos el contenido de base 64 a bytes originales
                byte[] originalBytes = Base64.decodeBase64(contenidoBase64);
                ByteArrayInputStream bis = new ByteArrayInputStream(originalBytes);
                retorno = bis;

            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ConversorBase64Exception(ex);
            }
        } else {
            throw new ConversorBase64Exception("No se está recibiendo el contenido para procesar !!");
        }
        return retorno;
    }

    public static void main(String... params) {
        String archivoOrigen = "C:\\Users\\dell\\Pictures\\JAVA CODIGOEJEMPLO EjemploWhileConvertirDecimalTOBinario salida.png";
        String archivoDestino = "C:\\Users\\dell\\Pictures\\ALL C Frasco.png.base64";
        String archivoDestinoCopia = "C:\\Users\\dell\\Videos\\[HD] Jose Jose - El Triste (En Vivo) - YouTube.mp4.copia.mp4";
        try {
            ConversorBase64Bean bean = new ConversorBase64Bean();
            bean.convertirArchivoTOArchivoBase64(archivoOrigen, archivoDestino);
            
            // Del archivo destino pasamos al origen como copia
            bean.crearArchivoFROMInputStream(
                    bean.convertirBase64TOInputStream(
                    bean.convertirArchivoTOBase64(archivoOrigen)), 
                    archivoDestinoCopia);
            
            System.out.println(bean.convertirArchivoTOBase64(archivoOrigen));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    

}
