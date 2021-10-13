package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.core;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class CoreCollExtraerVO {
// Propiedades de la clase

    private String tipoColumna = null;
    private String nombreColumna = null;

    public CoreCollExtraerVO() {
    }

    public CoreCollExtraerVO(String tipoColumna, String nombreColumna) {
        // Seteamos las propiedades correspondientes
        setTipoColumna(tipoColumna);
        setNombreColumna(nombreColumna);
    }

    public String getTipoColumna() {
        return tipoColumna;
    }

    public void setTipoColumna(String tipoColumna) {
        this.tipoColumna = tipoColumna;
    }

    public String getNombreColumna() {
        return nombreColumna;
    }

    public void setNombreColumna(String nombreColumna) {
        this.nombreColumna = nombreColumna;
    }
}
