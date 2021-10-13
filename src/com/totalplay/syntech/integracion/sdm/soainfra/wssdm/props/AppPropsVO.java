package com.totalplay.syntech.integracion.sdm.soainfra.wssdm.props;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class AppPropsVO {

    private String ambienteEjecucion = null;
    private String pathConfigLogs = null;
    private String wssdmWssoapUrl = null;
    private String wssdmWssoapUsuario = null;
    private String wssdmWssoapPassword = null;
    private String wssdTimeoutConect = null;
    private String wssdTimeoutRead = null;
    private String wstpeUpdateUrl = null;
    private String wstpeUpdateMswip = null;
    private String wstpeCommentUrl = null;
    private String wstpeCommentUsuario = null;
    private String wstpeCommentPassword = null;
    private String wstpeCommentIp = null;
    private String bddClassDriver = null;
    private String bddUrlFabricante = null;
    private String bddConexionServidor = null;
    private String bddConexionPuerto = null;
    private String bddConexionBasedatos = null;
    private String bddConexionUsuario = null;
    private String bddConexionPassword = null;
    private String queryTimeoutSecs = null;

    public String getAmbienteEjecucion() {
        return ambienteEjecucion;
    }

    public void setAmbienteEjecucion(String ambienteEjecucion) {
        this.ambienteEjecucion = ambienteEjecucion;
    }

    public String getPathConfigLogs() {
        return pathConfigLogs;
    }

    public void setPathConfigLogs(String pathConfigLogs) {
        this.pathConfigLogs = pathConfigLogs;
    }

    public String getWssdmWssoapUrl() {
        return wssdmWssoapUrl;
    }

    public void setWssdmWssoapUrl(String wssdmWssoapUrl) {
        this.wssdmWssoapUrl = wssdmWssoapUrl;
    }

    public String getWssdmWssoapUsuario() {
        return wssdmWssoapUsuario;
    }

    public void setWssdmWssoapUsuario(String wssdmWssoapUsuario) {
        this.wssdmWssoapUsuario = wssdmWssoapUsuario;
    }

    public String getWssdmWssoapPassword() {
        return wssdmWssoapPassword;
    }

    public void setWssdmWssoapPassword(String wssdmWssoapPassword) {
        this.wssdmWssoapPassword = wssdmWssoapPassword;
    }

    public String getWstpeUpdateUrl() {
        return wstpeUpdateUrl;
    }

    public void setWstpeUpdateUrl(String wstpeUpdateUrl) {
        this.wstpeUpdateUrl = wstpeUpdateUrl;
    }

    public String getWstpeCommentUrl() {
        return wstpeCommentUrl;
    }

    public void setWstpeCommentUrl(String wstpeCommentUrl) {
        this.wstpeCommentUrl = wstpeCommentUrl;
    }

    public String getBddClassDriver() {
        return bddClassDriver;
    }

    public void setBddClassDriver(String bddClassDriver) {
        this.bddClassDriver = bddClassDriver;
    }

    public String getBddUrlFabricante() {
        return bddUrlFabricante;
    }

    public void setBddUrlFabricante(String bddUrlFabricante) {
        this.bddUrlFabricante = bddUrlFabricante;
    }

    public String getBddConexionServidor() {
        return bddConexionServidor;
    }

    public void setBddConexionServidor(String bddConexionServidor) {
        this.bddConexionServidor = bddConexionServidor;
    }

    public String getBddConexionPuerto() {
        return bddConexionPuerto;
    }

    public void setBddConexionPuerto(String bddConexionPuerto) {
        this.bddConexionPuerto = bddConexionPuerto;
    }

    public String getBddConexionBasedatos() {
        return bddConexionBasedatos;
    }

    public void setBddConexionBasedatos(String bddConexionBasedatos) {
        this.bddConexionBasedatos = bddConexionBasedatos;
    }

    public String getBddConexionUsuario() {
        return bddConexionUsuario;
    }

    public void setBddConexionUsuario(String bddConexionUsuario) {
        this.bddConexionUsuario = bddConexionUsuario;
    }

    public String getBddConexionPassword() {
        return bddConexionPassword;
    }

    public void setBddConexionPassword(String bddConexionPassword) {
        this.bddConexionPassword = bddConexionPassword;
    }

    public String getQueryTimeoutSecs() {
        return queryTimeoutSecs;
    }

    public void setQueryTimeoutSecs(String queryTimeoutSecs) {
        this.queryTimeoutSecs = queryTimeoutSecs;
    }

    public String getWssdTimeoutConect() {
        return wssdTimeoutConect;
    }

    public void setWssdTimeoutConect(String wssdTimeoutConect) {
        this.wssdTimeoutConect = wssdTimeoutConect;
    }

    public String getWssdTimeoutRead() {
        return wssdTimeoutRead;
    }

    public void setWssdTimeoutRead(String wssdTimeoutRead) {
        this.wssdTimeoutRead = wssdTimeoutRead;
    }

    public String getWstpeCommentUsuario() {
        return wstpeCommentUsuario;
    }

    public void setWstpeCommentUsuario(String wstpeCommentUsuario) {
        this.wstpeCommentUsuario = wstpeCommentUsuario;
    }

    public String getWstpeCommentPassword() {
        return wstpeCommentPassword;
    }

    public void setWstpeCommentPassword(String wstpeCommentPassword) {
        this.wstpeCommentPassword = wstpeCommentPassword;
    }

    public String getWstpeCommentIp() {
        return wstpeCommentIp;
    }

    public void setWstpeCommentIp(String wstpeCommentIp) {
        this.wstpeCommentIp = wstpeCommentIp;
    }

    public String getWstpeUpdateMswip() {
        return wstpeUpdateMswip;
    }

    public void setWstpeUpdateMswip(String wstpeUpdateMswip) {
        this.wstpeUpdateMswip = wstpeUpdateMswip;
    }

    @Override
    public String toString() {
        return "AppPropsVO{" + "ambienteEjecucion=" + ambienteEjecucion + ", pathConfigLogs=" + pathConfigLogs + ", wssdmWssoapUrl=" + wssdmWssoapUrl + ", wssdmWssoapUsuario=" + wssdmWssoapUsuario + ", wssdmWssoapPassword=" + wssdmWssoapPassword + ", wssdTimeoutConect=" + wssdTimeoutConect + ", wssdTimeoutRead=" + wssdTimeoutRead + ", wstpeUpdateUrl=" + wstpeUpdateUrl + ", wstpeUpdateMswip=" + wstpeUpdateMswip + ", wstpeCommentUrl=" + wstpeCommentUrl + ", wstpeCommentUsuario=" + wstpeCommentUsuario + ", wstpeCommentPassword=" + wstpeCommentPassword + ", wstpeCommentIp=" + wstpeCommentIp + ", bddClassDriver=" + bddClassDriver + ", bddUrlFabricante=" + bddUrlFabricante + ", bddConexionServidor=" + bddConexionServidor + ", bddConexionPuerto=" + bddConexionPuerto + ", bddConexionBasedatos=" + bddConexionBasedatos + ", bddConexionUsuario=" + bddConexionUsuario + ", bddConexionPassword=" + bddConexionPassword + ", queryTimeoutSecs=" + queryTimeoutSecs + '}';
    }
    
    
   
}
