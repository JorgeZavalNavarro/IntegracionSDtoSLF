package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.cambiarestatus;

/**
 *
 * @author dell
 */
public class TicketCambiarEstatusUpdateInfoVO {

    private String numeroTicketSF = null;
    private String numeroTicketSD = null;
    private String idPersistente = null;
    private String estatusTicket = null;
    private String claveAreaResolutora = null;
    private String nombreAreaResolutora = null;
    private String claveTipoSolucion = null;
    private String nombreTipoSolucion = null;
    private String claveDiagnostico = null;
    private String nombreDiagnostico = null;
    private String claveLogJustificacion = null;
    private String descripcionJustificacion = null;
    private String bandeja = null;

    public String getNumeroTicketSF() {
        return numeroTicketSF;
    }

    public void setNumeroTicketSF(String numeroTicketSF) {
        this.numeroTicketSF = numeroTicketSF;
    }

    public String getNumeroTicketSD() {
        return numeroTicketSD;
    }

    public void setNumeroTicketSD(String numeroTicketSD) {
        this.numeroTicketSD = numeroTicketSD;
    }

    public String getEstatusTicket() {
        return estatusTicket;
    }

    public void setEstatusTicket(String estatusTicket) {
        this.estatusTicket = estatusTicket;
    }

    public String getClaveAreaResolutora() {
        return claveAreaResolutora;
    }

    public void setClaveAreaResolutora(String claveAreaResolutora) {
        this.claveAreaResolutora = claveAreaResolutora;
    }

    public String getNombreAreaResolutora() {
        return nombreAreaResolutora;
    }

    public void setNombreAreaResolutora(String nombreAreaResolutora) {
        this.nombreAreaResolutora = nombreAreaResolutora;
    }

    public String getClaveTipoSolucion() {
        return claveTipoSolucion;
    }

    public void setClaveTipoSolucion(String claveTipoSolucion) {
        this.claveTipoSolucion = claveTipoSolucion;
    }

    public String getNombreTipoSolucion() {
        return nombreTipoSolucion;
    }

    public void setNombreTipoSolucion(String nombreTipoSolucion) {
        this.nombreTipoSolucion = nombreTipoSolucion;
    }

    public String getClaveDiagnostico() {
        return claveDiagnostico;
    }

    public void setClaveDiagnostico(String claveDiagnostico) {
        this.claveDiagnostico = claveDiagnostico;
    }

    public String getNombreDiagnostico() {
        return nombreDiagnostico;
    }

    public void setNombreDiagnostico(String nombreDiagnostico) {
        this.nombreDiagnostico = nombreDiagnostico;
    }

    public String getClaveLogJustificacion() {
        return claveLogJustificacion;
    }

    public void setClaveLogJustificacion(String claveLogJustificacion) {
        this.claveLogJustificacion = claveLogJustificacion;
    }

    public String getDescripcionJustificacion() {
        return descripcionJustificacion;
    }

    public void setDescripcionJustificacion(String descripcionJustificacion) {
        this.descripcionJustificacion = descripcionJustificacion;
    }

    public String getBandeja() {
        return bandeja;
    }

    public void setBandeja(String bandeja) {
        this.bandeja = bandeja;
    }

    public String getIdPersistente() {
        return idPersistente;
    }

    public void setIdPersistente(String idPersistente) {
        this.idPersistente = idPersistente;
    }

    @Override
    public String toString() {
        return "TicketCambiarEstatusUpdateInfoVO{" + "numeroTicketSF=" + numeroTicketSF + ", numeroTicketSD=" + numeroTicketSD + ", idPersistente=" + idPersistente + ", estatusTicket=" + estatusTicket + ", claveAreaResolutora=" + claveAreaResolutora + ", nombreAreaResolutora=" + nombreAreaResolutora + ", claveTipoSolucion=" + claveTipoSolucion + ", nombreTipoSolucion=" + nombreTipoSolucion + ", claveDiagnostico=" + claveDiagnostico + ", nombreDiagnostico=" + nombreDiagnostico + ", claveLogJustificacion=" + claveLogJustificacion + ", descripcionJustificacion=" + descripcionJustificacion + ", bandeja=" + bandeja + '}';
    }
    
    
}
