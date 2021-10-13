package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.transferencia;

import com.totalplay.syntech.integracion.sdm.soainfra.wssdm.props.AppPropsBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.core.CoreBean;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.keys.CodeKeys;
import org.apache.log4j.Category;

/**
 *
 * @author dell
 */
public class TicketTransferenciaClientWSSalesgorceBean extends CoreBean {
    
    // Constantes de la clase
    private static Category log = Category.getInstance(TicketTransferenciaClientWSSalesgorceBean.class);
    
    public static String postComment(String folioSF, String comentario, String grupo) throws TicketTransferenciaException {
        
        log.info("Enviar información a salesforce...");
        
        String retorno = null;
        String sRespuestaRest = "";

        try {

            log.info("Invocamos el servicio de salesforce...");
            log.info(AppPropsBean.getPropsVO().getWstpeCommentUrl());
            URL url = new URL(AppPropsBean.getPropsVO().getWstpeCommentUrl());
            log.info("Realizando la conexión con el servicio...");
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setRequestMethod("POST");
            httpUrlConn.setRequestProperty("Content-Type", "application/json");
            httpUrlConn.setRequestProperty("accept", "application/json");
            

            log.info("Enviando el siguiente código json: ");
            String input = "{\"Login\":{"
                    + "\"UserId\":\"" + AppPropsBean.getPropsVO().getWstpeCommentUsuario().trim() + "\","
                    + "\"Password\":\"" + AppPropsBean.getPropsVO().getWstpeCommentPassword().trim() + "\","
                    + "\"IP\":\"" + AppPropsBean.getPropsVO().getWstpeCommentIp().trim() + "\""
                    + "},"
                    + "\"NoTicket\":\"" + folioSF.trim() + "\","
                    + "\"Comment\":\"" + comentario.trim().replace("\n", " \\n ").replace("\t", " \\t ").replace("\r", " \\r ").replace("\f", " \\f ").replace("\b", " \\b ") + "\","
                    + "\"Grupo\":\"" + grupo + "\""
                    + "}";

            log.info(input);
            OutputStream os = httpUrlConn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            log.info("Ejecutando petición...");
            // Obtenemos el resultado del api rest
            if (httpUrlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                log.info("Se produjo un error al ejecutar la petición");
                retorno = "Failed : HTTP error code : "
                        + httpUrlConn.getResponseCode() + "\n" + httpUrlConn.getResponseMessage();
                throw new TicketTransferenciaException(CodeKeys.CODE_610_WSSF_ERROR, new Exception("Failed : HTTP error code : "
                        + httpUrlConn.getResponseCode() + "\n" + httpUrlConn.getResponseMessage()));
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((httpUrlConn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                sRespuestaRest = sRespuestaRest + "\n" + output;
            }
            log.info("Respuesta desde SF: " + sRespuestaRest);
            retorno = sRespuestaRest;
            httpUrlConn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.info("Error MalformedURLException :" + e.getMessage());
            throw new TicketTransferenciaException(CodeKeys.CODE_620_WSSF_MALURL, e);

        } catch (IOException e) {
            e.printStackTrace();
            log.info("Error IOException :" + e.getMessage());
            throw new TicketTransferenciaException(CodeKeys.CODE_630_WSSF_IOERROR, e);
        }

        return retorno;

    }

}
