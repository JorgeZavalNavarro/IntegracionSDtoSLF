package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.connbdd;

import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.core.CoreException;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class ConnectorBDDConsultasException extends CoreException{
    
    /**public ConnectorBDDConsultasException(String idCodigo, Exception ex){
        super(idCodigo, ex);
    }**/
    
    public ConnectorBDDConsultasException(String idCodigo, String ex){
        super(idCodigo, new Exception(ex));
    }
    
    public ConnectorBDDConsultasException(String idCodigo, Throwable ex){
        super(idCodigo, ex);
    }
    
}
