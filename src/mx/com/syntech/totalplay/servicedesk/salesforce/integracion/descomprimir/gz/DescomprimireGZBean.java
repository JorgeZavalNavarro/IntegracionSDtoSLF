package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.descomprimir.gz;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class DescomprimireGZBean {

    public InputStream descomprimirTOInputStream(String origen, String extension) throws DescomprimireGZException {
        InputStream retorno = null;
        if (origen != null && !origen.isEmpty()) {

            // definimos el nombre de nuestro archivo destino
            String destino = origen + "." + extension;
            try {
                GZIPInputStream in = new GZIPInputStream(new FileInputStream(origen));
                retorno = in;
                
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new DescomprimireGZException(ex);
            }
        } else {
            throw new DescomprimireGZException("No se está recibiendo la información completa para trabajar ");
        }
        return retorno;
    }

    public static void main(String... params) {
        try {
            String archivoOrigen = "C:\\SYNTECH\\SERVICIOS\\TotalPlay\\WebServiceConsultasPDR\\Desarrollo\\IntegracionSDvsSoaInfra\\src\\java\\resources\\Ejemplos\\C25C83750386BF43BC2D9E2D2F2D4DFC_Incidente CARE.jpg.gz";
            DescomprimireGZBean bean = new DescomprimireGZBean();
            bean.descomprimirTOInputStream(archivoOrigen, "jpg");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
