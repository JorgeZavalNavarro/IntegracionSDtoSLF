package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.cambiarestatus;

import java.sql.Timestamp;

/**
 *
 * @author dell
 */
public class TicketCambiarEstatusJustificacionInfoVO {
    
        // Propiedades de la clase
    private Integer claveLog = null;                // --> CLAVE_LOG
    private String ticketFolio = null;              // --> TICKET_FOLIO
    private String ticketPersid = null;             // --> PERSID_TICKET
    private String ticketSalesForce = null;         // --> NÚMERO DEL TICKET EN SALESFORCE
    private Long timeFechaJustificacion = null;     // --> FECHA_JUSTIFICACION
    private Timestamp fechaJustificacion = null;    // --> *** Calculado ***
    private String analistaApellidos = null;        // --> ANALISTA_APELLIDOS
    private String analistaNombre = null;           // --> ANALISTA_NOMBRE
    private String analistaUuid = null;             // --> UUID DEL ANALISTA
    private String justificacion = null;            // --> JUSTIFICACION

    public Integer getClaveLog() {
        return claveLog;
    }

    public void setClaveLog(Integer claveLog) {
        this.claveLog = claveLog;
    }

    public String getTicketFolio() {
        return ticketFolio;
    }

    public void setTicketFolio(String ticketFolio) {
        this.ticketFolio = ticketFolio;
    }

    public String getTicketPersid() {
        return ticketPersid;
    }

    public void setTicketPersid(String ticketPersid) {
        this.ticketPersid = ticketPersid;
    }

    public String getTicketSalesForce() {
        return ticketSalesForce;
    }

    public void setTicketSalesForce(String ticketSalesForce) {
        this.ticketSalesForce = ticketSalesForce;
    }

    public Long getTimeFechaJustificacion() {
        return timeFechaJustificacion;
    }

    public void setTimeFechaJustificacion(Long timeFechaJustificacion) {
        this.timeFechaJustificacion = timeFechaJustificacion;
    }

    public Timestamp getFechaJustificacion() {
        return fechaJustificacion;
    }

    public void setFechaJustificacion(Timestamp fechaJustificacion) {
        this.fechaJustificacion = fechaJustificacion;
    }

    public String getAnalistaApellidos() {
        return analistaApellidos;
    }

    public void setAnalistaApellidos(String analistaApellidos) {
        this.analistaApellidos = analistaApellidos;
    }

    public String getAnalistaNombre() {
        return analistaNombre;
    }

    public void setAnalistaNombre(String analistaNombre) {
        this.analistaNombre = analistaNombre;
    }

    public String getAnalistaUuid() {
        return analistaUuid;
    }

    public void setAnalistaUuid(String analistaUuid) {
        this.analistaUuid = analistaUuid;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    @Override
    public String toString() {
        return "TicketCambiarEstatusJustificacionInfoVO{" + "claveLog=" + claveLog + ", ticketFolio=" + ticketFolio + ", ticketPersid=" + ticketPersid + ", ticketSalesForce=" + ticketSalesForce + ", timeFechaJustificacion=" + timeFechaJustificacion + ", fechaJustificacion=" + fechaJustificacion + ", analistaApellidos=" + analistaApellidos + ", analistaNombre=" + analistaNombre + ", analistaUuid=" + analistaUuid + ", justificacion=" + justificacion + '}';
    }

    
    
}
