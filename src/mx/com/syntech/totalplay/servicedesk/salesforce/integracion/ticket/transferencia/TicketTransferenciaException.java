package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.transferencia;

import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.core.CoreException;

/**
 *
 * @author dell
 */
public class TicketTransferenciaException extends CoreException{
    
    public TicketTransferenciaException(String idError, Throwable cause) {
        super(idError, cause);
    }
    
}
