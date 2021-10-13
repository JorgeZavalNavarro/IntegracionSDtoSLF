package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.core;

/**
 * 
 * @author Jorge Zavala Navarro
 */
public class CoreException extends Exception{
    
    private String idError = null;
    private String mensaje = null;
    private Throwable throwable = null;

    public CoreException(String idError, Throwable cause) {
        super(cause);
        this.idError = idError;
        this.throwable = cause;
        this.mensaje = cause.getLocalizedMessage()==null 
                || cause.getLocalizedMessage().isEmpty()
                ? cause.getMessage() 
                : cause.getLocalizedMessage();
    }

    public String getIdError() {
        return idError;
    }

    public void setIdError(String idError) {
        this.idError = idError;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

}
