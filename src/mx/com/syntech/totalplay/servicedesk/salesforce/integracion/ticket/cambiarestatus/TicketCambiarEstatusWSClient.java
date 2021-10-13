package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.cambiarestatus;

import com.totalplay.syntech.integracion.sdm.soainfra.wssdm.props.AppPropsBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.keys.CodeKeys;
import org.apache.log4j.Category;

/**
 *
 * @author dell
 */
public class TicketCambiarEstatusWSClient {
    
    Category log = Category.getInstance(TicketCambiarEstatusWSClient.class);

    public String callWS(
           TicketCambiarEstatusUpdateInfoVO infoVO) throws TicketCambiarEstatusException {
        String retorno = null;
        try {
            
            // Cambiar el la cadena de justificación de doble comilla a una sola comilla
            String urlWS = AppPropsBean.getPropsVO().getWstpeUpdateUrl();
            log.info("   ::: URL = " + urlWS);
            infoVO.setBandeja(infoVO.getBandeja()!=null ? infoVO.getBandeja().replaceAll("\"", "'") : "");
            String params
                    = "{ \"numeroTicket\": \"" + infoVO.getNumeroTicketSF()+ "\" ,"
                    +   "\"Status\": \"" + infoVO.getEstatusTicket()+ "\" ,"
                    +   "\"areaResolutora\": \"" + 
                            (infoVO.getNombreAreaResolutora()==null ? "" : infoVO.getNombreAreaResolutora().replaceAll("\"", "'")) + "\", "
                    +   "\"motivo\": \"" + 
                            (infoVO.getNombreDiagnostico()==null ? "" : infoVO.getNombreDiagnostico().replaceAll("\"", "'")) + "\" ,"
                    +   "\"TipoSolucion\": \"" + 
                            (infoVO.getNombreTipoSolucion()==null ? "" : infoVO.getNombreTipoSolucion().replaceAll("\"", "'")) + "\", "
                    +   "\"Justificacion\": \"" + 
                            (infoVO.getDescripcionJustificacion()==null ? "" : infoVO.getDescripcionJustificacion().replaceAll("\"", "'")) + "\", "
                    +   "\"diagFinal\": \"" + 
                            (infoVO.getNombreDiagnostico()==null ? "" : infoVO.getNombreDiagnostico().replaceAll("\"", "'")) + "\" ,"
                    +   "\"Bandeja\": \"" + 
                            (infoVO.getBandeja() == null ? "" : infoVO.getBandeja().replaceAll("\"", "'")) + "\" }";
            // String[] details = {};
            log.info("   ::: Input = " + params);

            // log.info(Arrays.toString(details));

            URL line_api_url = new URL(urlWS);
            String payload = params;

            HttpURLConnection linec = (HttpURLConnection) line_api_url.openConnection();
            // linec.setDoInput(true);
            linec.setDoOutput(true);
            linec.setRequestMethod("POST");
            linec.setRequestProperty("Content-Type", "application/json");
            // linec.setRequestProperty("Authorization", "Bearer 1djCb/mXV+KtryMxr6i1bXw");
            log.info(payload);
            // OutputStreamWriter writer = new OutputStreamWriter(linec.getOutputStream(), "UTF-8");
            OutputStream os = linec.getOutputStream();
            os.write(payload.getBytes());
            os.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(linec.getInputStream()));
            String inputLine;
            retorno = "";
            while ((inputLine = in.readLine()) != null) {
                log.info(inputLine);
                retorno = retorno + inputLine;
            }
            in.close();
            linec.disconnect();

            // retorno = "La información se envió al soa-infra satisfactoriamente"; 

        } catch (Exception e) {
            log.info("Error al consumir el web service para actualizar la información del ticket" + e);
            throw new TicketCambiarEstatusException(CodeKeys.CODE_970_DATABASE_ERROR_NC, e);

        }
        return retorno;
    }
    
    
}
