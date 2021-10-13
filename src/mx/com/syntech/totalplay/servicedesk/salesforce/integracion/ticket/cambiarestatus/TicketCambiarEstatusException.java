package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.cambiarestatus;

import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.core.CoreException;

/**
 *
 * @author dell
 */
public class TicketCambiarEstatusException extends CoreException{
    
    public TicketCambiarEstatusException(String idError, Throwable cause) {
        super(idError, cause);
    }
    
    
    
}
