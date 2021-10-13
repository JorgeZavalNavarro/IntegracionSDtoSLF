package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.cambiarestatus;

import com.totalplay.soa.salesforce.changestatusticketsf.bpelprocesschangestatusticketsf.BpelprocesschangestatusticketsfClientEp;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.core.CoreBean;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.keys.CodeKeys;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.utils.AlgoritmoAES;
import org.apache.log4j.Category;

/**
 *
 * @author dell
 */
@Deprecated
public class TicketCambiarEstatusWSSalerforceBean extends CoreBean {

    // Constantes de la clase
    private static final Category log = Category.getInstance(TicketCambiarEstatusWSSalerforceBean.class);

    // Instancias de los clientes para el consumo del ws
    AlgoritmoAES al = new AlgoritmoAES();
    BpelprocesschangestatusticketsfClientEp bpelClient = new BpelprocesschangestatusticketsfClientEp();
    com.totalplay.soa.salesforce.changestatusticketsf.bpelprocesschangestatusticketsf.Process.Login login = new com.totalplay.soa.salesforce.changestatusticketsf.bpelprocesschangestatusticketsf.Process.Login();

    public String cambiarEstatusBpel(String ticketSalesforce, String estatusSalesforce,
            String substatusSalesforce, String justificacionSalesforce) throws TicketCambiarEstatusException {
        String retorno = null;

        // Validamos la información requerida
        if (ticketSalesforce != null && !ticketSalesforce.isEmpty()
                && estatusSalesforce != null && !estatusSalesforce.isEmpty()) {
            // Cargamos el lgin correspondiente
            

        } else {
            String error = "No se cuenta con la información completa/correcta para ejecutar esta funcionalida. Revise los logs para mas información.";
            log.error(error);
            throw new TicketCambiarEstatusException(CodeKeys.CODE_150_INFORMACIONNO_ENCONTRADA, new Exception(error));
        }

        return retorno;
    }

}
