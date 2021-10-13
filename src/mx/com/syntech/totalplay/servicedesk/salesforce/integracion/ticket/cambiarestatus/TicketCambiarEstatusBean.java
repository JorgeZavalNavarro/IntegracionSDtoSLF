package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.cambiarestatus;

import com.totalplay.syntech.integracion.sdm.soainfra.wssdm.props.AppPropsBean;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.connbdd.ConnectorBDDConsultasBean;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.core.CoreBean;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.keys.CodeKeys;
import org.apache.log4j.Category;

/**
 * Cambiar el estatus en Salesforce
 *
 * @author dell
 */
public class TicketCambiarEstatusBean extends CoreBean {

    // Constantes de la clase
    private static Category log = Category.getInstance(TicketCambiarEstatusBean.class);
    private static final String uNoLock = "  WITH (NOLOCK) ";

    // Propiedades de la clase
    private int sid = 0;       // Session ID del usuario para el ws de CA SDM
    private static URL urlSD = null;  // URL del servicio web de CA SDM

    // Propiedades a procesar
    private String log_agent = null;
    private String zfolio_dbw_sf = null;
    private String status = null;

    public TicketCambiarEstatusBean(String... params) throws TicketCambiarEstatusException {
        log.info("Iniciando cambio de estatus del ticket ...");
        seeArgs(params);

        try {
            // Obtener las propiedades a utilizar
            log_agent = this.getValorArgumentos(params, "log_agent").trim();
            zfolio_dbw_sf = this.getValorArgumentos(params, "zfolio_dbw_sf").trim();
            status = this.getValorArgumentos(params, "status").trim();

            log.info("Procesando para los siguientes datos: ");
            log.info("log_agent .......: " + log_agent);
            log.info("zfolio_dbw_sf ...: " + zfolio_dbw_sf);
            log.info("status: ..........: " + status);

            log.info("Inicializamos la URL: " + AppPropsBean.getPropsVO().getWssdmWssoapUrl());
            urlSD = new URL(AppPropsBean.getPropsVO().getWssdmWssoapUrl());

            // 
        } catch (Exception ex) {
            String error = "Se produjo un error al intentar realizar la transferencia.";
            log.error(error);
            throw new TicketCambiarEstatusException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, new Exception(error));
        }

    }

    public TicketCambiarEstatusRetornoVO procesar() throws TicketCambiarEstatusException {

        log.info("Procesando el cambio de estatus...");

        TicketCambiarEstatusRetornoVO retorno = null;
        String mensajeLogActivity = null;
        TicketCambiarEstatusUpdateInfoVO updateInfoVO = null;
        /**
         * String numeroTicketServiceDesk = null; String idPersistenteTicket =
         * null; String justificacionTicket = null; String diagnosticoTicket =
         * null; String solucionTicket = null;*
         */

        // Logeamos al sistema para ejecutar las operaciones correspondiente
        this.sid = this.login(
                AppPropsBean.getPropsVO().getWssdmWssoapUsuario(),
                AppPropsBean.getPropsVO().getWssdmWssoapPassword());

        if (this.sid < 2000) {
            String error = "(" + this.sid + ") No se puede acceder al servicio, por favor valide lascredenciales y/o el estatus actual del servicio.";
            log.error(error);
            throw new TicketCambiarEstatusException(error, new Exception(error));
        }

        log.info("ID de sesion en SDM: " + this.sid);

        // Validamos que se tengan generados todos los parametros a procesar
        if (status != null && !status.isEmpty()
                && zfolio_dbw_sf != null && !zfolio_dbw_sf.isEmpty()
                && log_agent != null && !log_agent.isEmpty()
                && log_agent.equals(TicketCambiarEstatusKeys.LOG_AGENT_VALIDO)) {

            // Inicamos nuestro valor de retorno
            retorno = new TicketCambiarEstatusRetornoVO();

            // Iniciar el VO pra recopilar toda la información necesaria y validada
            updateInfoVO = new TicketCambiarEstatusUpdateInfoVO();

            // Definir nuestra variable de conexión a la base de datos
            Connection conn = null;
            try {

                // Intentando conectar a la base de datos
                conn = ConnectorBDDConsultasBean.getConectionServiceDesk();

                // Buscar el persistent id del ticket y el folio del ticket
                // En caso de encontrar dos tickets con el mismo folio de SF se tomara el ultimo
                // que se registro
                log.info("Buscar la información del folio del ticket y el id persistente del mismo");
                String sqlBuscarInfoTicket
                        = "select ticket.persid as ticket_persid,\n"
                        + "       ticket.zfolio_dbw_sf as folio_salesforce,\n"
                        + "	  ticket.ref_num as folio_servicedesk,\n"
                        + "       ticket.zgrp_origen_sf as bandeja, \n"
                        + "       ticket.group_id as clave_area_resolutora \n"
                        + "  from call_req as ticket\n"
                        + " where ticket.zfolio_dbw_sf = ? \n"
                        + " order by open_date desc";
                log.info("Ejecutar el sql");
                log.info(sqlBuscarInfoTicket);
                PreparedStatement psBuscarInfoTicket = conn.prepareCall(sqlBuscarInfoTicket);
                psBuscarInfoTicket.setString(1, zfolio_dbw_sf);
                ResultSet rsBuscarInfoTicket = psBuscarInfoTicket.executeQuery();
                if (rsBuscarInfoTicket.next()) {
                    // Cargamos la información del folio del ticket en CA SDM y id persistente del ticket
                    updateInfoVO.setNumeroTicketSD(rsBuscarInfoTicket.getString("folio_servicedesk"));
                    updateInfoVO.setIdPersistente(rsBuscarInfoTicket.getString("ticket_persid"));
                    updateInfoVO.setNumeroTicketSF(rsBuscarInfoTicket.getString("folio_salesforce"));
                    updateInfoVO.setClaveAreaResolutora(rsBuscarInfoTicket.getString("clave_area_resolutora"));
                    updateInfoVO.setBandeja(rsBuscarInfoTicket.getString("bandeja"));

                    log.info("Buscar información para las siguientes referencias (Solo al resolver)");
                    log.info("   --> Ticket de Service Desk ...........: " + updateInfoVO.getNumeroTicketSD());
                    log.info("   --> Persistend ID de Service Desk ....: " + updateInfoVO.getIdPersistente());
                    log.info("   --> Ticket de Salesforce .............: " + updateInfoVO.getNumeroTicketSF());
                    log.info("   --> Clave de la area resolutora ......: " + updateInfoVO.getClaveAreaResolutora());
                    log.info("   --> Bandeja ..........................: " + updateInfoVO.getBandeja());

                } else {
                    String error = "No se encontró información de ticket con el numero de ticket de salesforce: " + zfolio_dbw_sf;
                    log.error(error);
                    throw new TicketCambiarEstatusException(CodeKeys.CODE_150_INFORMACIONNO_ENCONTRADA, new Exception(error));
                }

                // Buscar el ultimo activity correspondiente al cambio de estado
                // para extraer el comentario y mandarlo como estatus
                log.info("Buscar la justificación (log activity) correspondiente al cambio de estado. Ultimo ST");
                String sqlBuscarJustificacion
                        = "select top 100                                      \n"
                        + "       log.id as clave_log,                         \n"
                        + "	   log.persid as id_log_persistent,            \n"
                        + "	   log.call_req_id as id_ticket_persistent,    \n"
                        + "	   ticket.ref_num as folio_ticket,             \n"
                        + "       log.type as tipo_log,                        \n"
                        + "	   log.description as log_descripcion          \n"
                        + "  from act_log as log,                              \n"
                        + "       call_req as ticket                           \n"
                        + " where log.call_req_id = ticket.persid              \n"
                        + "   and ticket.ref_num = ?                           \n"
                        + "   and log.type = 'ST'                              \n"
                        + " order by log.system_time desc";
                log.info("SQL");
                log.info(sqlBuscarJustificacion);
                PreparedStatement stBuscarJustificacion = conn.prepareCall(sqlBuscarJustificacion);
                stBuscarJustificacion.setString(1, updateInfoVO.getNumeroTicketSD());
                ResultSet rsBuscarJustificacion = stBuscarJustificacion.executeQuery();

                if (rsBuscarJustificacion.next()) {
                    // Extraemos la información de la justificación correspondientye
                    log.info("Justificación encontrada: ");
                    updateInfoVO.setDescripcionJustificacion(rsBuscarJustificacion.getString("log_descripcion"));
                    log.info(updateInfoVO.getDescripcionJustificacion());
                } else {
                    throw new Exception("No se encontró justificación de cambio de estado para el ticket: " + updateInfoVO.getNumeroTicketSD());
                }

                // Obtener el estatus del ticket para salesforce
                String estatusSalesforce = this.getEstatusSalesforceFromEstatusServiceDesk(status);

                // Validamossi el estatus es de RE resuelto
                if (status != null && status.equals("RE")) {

                    /**
                     * Modificaciones
                     *
                     * Se agregan las historias de usuario HU3 y HU4, HU5, HU6 y
                     * HU/
                     * Fecha de Cambio: 30/Septiembre/2021
                     * Autor: JORGE ZAVALA NAVARRO
                     * HU3 === Cambio en SD a Resuelto (RE), pasar en DBW a
                     * Validación: Como usuario de Service Desk N2 o superior
                     * requiero que cuando a un ticket se le cambie el estatus a
                     * " Resuelto " se indique al DBW SF cambiar el estatus del
                     * ticket a "Validación".
                     *
                     * Criterios de aceptación: • Se debe indicar al DBW SF
                     * cambiar el estatus del ticket a “Validación” cuando este
                     * cambie a “Resuelto”. • La información del campo
                     * “Descripción del usuario” deberá ser enviada al DBW SF en
                     * el campo “Justificacion” indicado en el contrato de
                     * interfaz UpdateInfoSalesForce. • Los comentarios a enviar
                     * a través del campo “Descripción del usuario” al DBW SF
                     * deben contener los acentos tal como fueron registrados en
                     * Service Desk.
                     *
                     */
                    // log.info("APLICANDO LOS PROCESOS PARA RESOLVER...");
                    // log.info(" ::: Obteniendo la información de la Area Resolutora...");
                    // AreaResolutoraInfoVO areaResolutoraInfoVO = this.obtenerAreaResolutora(updateInfoVO.getIdPersistente(), updateInfoVO.getClaveAreaResolutora(), conn);
                    // if (areaResolutoraInfoVO != null) {
                    //     updateInfoVO.setClaveAreaResolutora(areaResolutoraInfoVO.getClaveAreaResolutora());
                    //     updateInfoVO.setNombreAreaResolutora(areaResolutoraInfoVO.getDescripcion());
                    //     log.info(" ::: Area Resolutora --> (" + updateInfoVO.getClaveAreaResolutora() + "):" + updateInfoVO.getNombreAreaResolutora());
                    // } else {
                    //     log.info(" ::: No se tiene especificada información de la area resolutoria");
                    // }
                    // Obtenemos la solución (justificacion) del ticket
                    // updateInfoVO.setNombreTipoSolucion(this.obtenerSolucion(updateInfoVO.getNumeroTicketSD(), conn));
                    // log.info("Solución encontrada: " + updateInfoVO.getNombreTipoSolucion());
                    // Obtenemos el diagnóstico correspondiente
                    // updateInfoVO.setNombreDiagnostico(this.obtenerDiagnostico(updateInfoVO.getNumeroTicketSD(), conn));
                    // log.info("Diagnostico encontrada: " + updateInfoVO.getNombreDiagnostico());
                    // Obtenemos la justificación
                    updateInfoVO.setDescripcionJustificacion(this.obtenerJustificacionPorCambioEstado(updateInfoVO.getIdPersistente(), conn).getJustificacion());
                    log.info("Justificación encontrada: " + updateInfoVO.getDescripcionJustificacion());

                    // Cambiar el valor de RE por Resuelto
                    updateInfoVO.setEstatusTicket("Validación");

                } else if (status != null && (status.equals("WIP") || status.equals("AEUR") || status.equals("ZPNDTRCR"))) {

                    // HU4
                    // ===
                    // 
                    // Fecha de Cambio: 30/Septiembre/2021
                    // Autor: JORGE ZAVALA NAVARRO
                    // 
                    // Como usuario de Service Desk N2 o superior requiero que cuando a 
                    // un ticket se le cambie el estatus a “En curso (WIP)”, 
                    // Pendiente por Cliente (AEUR)" o "Pendiente por Terceros (ZPNDTRCR)” "
                    // se indique al DBW SF cambiar el estatus del ticket a "En proceso".
                    //
                    // Criterios de aceptación:
                    //   • Se debe indicar al DBW SF cambiar el estatus del ticket a “en proceso” cuando 
                    //     este cambie a “En curso”, "Pendiente por Cliente" o "Pendiente por Terceros”.
                    //
                    //   • La información del campo “Descripción del usuario” deberá ser enviada al DBW SF 
                    //     en el campo “Justificacion” indicado en el contrato de interfaz UpdateInfoSalesForce.
                    //
                    //   • Los comentarios a enviar a través del campo “Descripción del usuario” al DBW SF 
                    //     deben contener los acentos tal como fueron registrados en Service Desk.
                    
                    // Obtenemos la justificación
                    updateInfoVO.setDescripcionJustificacion(this.obtenerJustificacionPorCambioEstado(updateInfoVO.getIdPersistente(), conn).getJustificacion());
                    log.info("Justificación encontrada: " + updateInfoVO.getDescripcionJustificacion());

                    updateInfoVO.setEstatusTicket("En proceso");

                } else if (status != null && status.equals("CNCL")) {

                    // HU5
                    // ===
                    // 
                    // Fecha de Cambio: 30/Septiembre/2021
                    // Autor: JORGE ZAVALA NAVARRO
                    // 
                    // Como usuario de Service Desk N2 o superior requiero que cuando a un ticket 
                    // se le cambie el estatus a "Cancelado" (CNCL) se indique al DBW SF cambiar 
                    // el estatus del ticket a " Cancelado ".
                    //
                    // Criterios de aceptación:
                    //
                    //     • Se debe indicar al DBW SF cambiar el estatus del ticket a “Cancelado” 
                    //       cuando este cambie a “Cancelado”.
                    updateInfoVO.setEstatusTicket("Cancelado");
                    updateInfoVO.setDescripcionJustificacion(this.obtenerJustificacionPorCancelarCierre(updateInfoVO.getIdPersistente(), conn).getJustificacion());
                    log.info("Justificación encontrada: " + updateInfoVO.getDescripcionJustificacion());
                    

                } else if (status != null && status.equals("CL")) {

                    // HU6                    
                    // ===
                    // 
                    // 
                    // Fecha de Cambio: 30/Septiembre/2021
                    // Autor: JORGE ZAVALA NAVARRO
                    // 
                    // Como usuario de Service Desk N2 o superior requiero que cuando a un ticket 
                    // se le cambie el estatus a “Cerrado (CL )" se indique al DBW SF cambiar el 
                    // estatus del ticket a "Cerrado".
                    // 
                    // Criterios de aceptación:
                    //      • Se debe indicar al DBW SF cambiar el estatus del ticket a “Cerrado” 
                    //        cuando este cambie a “Cerrado”.
                    // 
                    //      • La información del campo “Descripción del usuario” deberá ser enviada 
                    //        al DBW SF en el campo “Justificacion” indicado en el contrato de interfaz 
                    //        UpdateInfoSalesForce.
                    //
                    //      • Los comentarios a enviar a través del campo “Descripción del usuario” 
                    //        al DBW SF deben contener los acentos tal como fueron registrados en 
                    //        Service Desk.
                    updateInfoVO.setEstatusTicket("Cerrado");

                    updateInfoVO.setDescripcionJustificacion(this.obtenerJustificacionPorCancelarCierre(updateInfoVO.getIdPersistente(), conn).getJustificacion());
                    log.info("Justificación encontrada: " + updateInfoVO.getDescripcionJustificacion());

                } else if (status != null && status.equals("OP")) {

                    // HU7
                    // ===
                    // 
                    // Fecha de Cambio: 30/Septiembre/2021
                    // Autor: JORGE ZAVALA NAVARRO
                    // 
                    // Como usuario de Service Desk N2 o superior requiero que cuando a un ticket se 
                    // le cambie el estatus a “Abierto" se indique al DBW SF cambiar el estatus del 
                    // ticket a "Nuevo".
                    //
                    // Criterios de aceptación:
                    //    • Se debe indicar al DBW SF cambiar el estatus del ticket a “Nuevo” cuando 
                    //      este cambie a “Abierto”.
                    //    • La información del campo “Descripción del usuario” deberá ser enviada 
                    //      al DBW SF en el campo “Justificacion” indicado en el contrato de interfaz 
                    //      UpdateInfoSalesForce.
                    //    • Los comentarios a enviar a través del campo “Descripción del usuario” al 
                    //      DBW SF deben contener los acentos tal como fueron registrados en Service Desk.
                    // AWTVNDR
                    updateInfoVO.setEstatusTicket("Nuevo");

                    updateInfoVO.setDescripcionJustificacion(this.obtenerJustificacionPorCambioEstado(updateInfoVO.getIdPersistente(), conn).getJustificacion());
                    log.info("Justificación encontrada: " + updateInfoVO.getDescripcionJustificacion());

                    // } else if (status != null && status.equals("AWTVNDR")) {
                    // updateInfoVO.setEstatusTicket("En proceso");
                } else if (status != null && status.equals("OP")) {

                    // HU7
                    // ===
                    // 
                    // Fecha de Cambio: 30/Septiembre/2021
                    // Autor: JORGE ZAVALA NAVARRO
                    // 
                    // Como usuario de Service Desk N2 o superior requiero que cuando a un ticket se 
                    // le cambie el estatus a “Abierto" se indique al DBW SF cambiar el estatus del 
                    // ticket a "Nuevo".
                    //
                    // Criterios de aceptación:
                    //    • Se debe indicar al DBW SF cambiar el estatus del ticket a “Nuevo” cuando 
                    //      este cambie a “Abierto”.
                    //    • La información del campo “Descripción del usuario” deberá ser enviada 
                    //      al DBW SF en el campo “Justificacion” indicado en el contrato de interfaz 
                    //      UpdateInfoSalesForce.
                    //    • Los comentarios a enviar a través del campo “Descripción del usuario” al 
                    //      DBW SF deben contener los acentos tal como fueron registrados en Service Desk.
                    // AWTVNDR
                    updateInfoVO.setEstatusTicket("Nuevo");

                    updateInfoVO.setDescripcionJustificacion(this.obtenerJustificacionPorCambioEstado(updateInfoVO.getIdPersistente(), conn).getJustificacion());
                    log.info("Justificación encontrada: " + updateInfoVO.getDescripcionJustificacion());

                    // } else if (status != null && status.equals("AWTVNDR")) {
                    // updateInfoVO.setEstatusTicket("En proceso");
                    
                    
                    
                } else if (status != null && status.equals("AWTVNDR")) {
                    /** ***********************************
                     * FALTA EL DOCUMENTACIÓN COMO LOS CASOS ANTERIORES
                     */
                    updateInfoVO.setEstatusTicket("En Proceso");
                    updateInfoVO.setDescripcionJustificacion(this.obtenerJustificacionPorCambioEstado(updateInfoVO.getIdPersistente(), conn).getJustificacion());
                    log.info("Justificación encontrada: " + updateInfoVO.getDescripcionJustificacion());
                    
                } else{
                    String error = "El estatus " + status + " no se encuentra contemplado en este proceso.";
                    log.error(error);
                    throw new TicketCambiarEstatusException(CodeKeys.CODE_130_VALOR_INCORRECTO, new Exception(error));
                    
                    
                }
                
                
                try {

                    // logActDescripcion = "Se invocó el servicio de SalesForce correctamente: " + AppPropsBean.getPropsVO().getUrlTpeUpdateticketWs() + "\n"
                    //         + "Con la información: " + infoVO.toString();
                    log.info("Consumir el servicio de ...: " + AppPropsBean.getPropsVO().getWstpeUpdateUrl());
                    TicketCambiarEstatusWSClient client = new TicketCambiarEstatusWSClient();
                    String retornoWsUpdate = client.callWS(updateInfoVO);
                    mensajeLogActivity = "[Respuesta de SF cambio de status]\nCORRECTO: Respuesta del consumo del servicio: " + AppPropsBean.getPropsVO().getWstpeUpdateUrl() + "\n"
                            + "Con la información: " + updateInfoVO.toString() + "\n"
                            + "El web service se ejecutó satisfactoriamente y resolvió la siguiente información: " + "\n";

                    RetornoWSSalesForceInfoVO parserVO = new RetornoWSSalesForceInfoVO(retornoWsUpdate);
                    mensajeLogActivity = mensajeLogActivity + "result.....: " + (parserVO.getResult().equals("0") ? "0: Respuesta exitosa" : "1: Respuesta fallida") + "\n";
                    mensajeLogActivity = mensajeLogActivity + "Idresult..: " + parserVO.getIdResult() + "\n";
                    mensajeLogActivity = mensajeLogActivity + "resultDescription...: " + parserVO.getResultDescription() + "\n";
                    
                } catch (Exception ex) {
                    log.error("Error al consumir el WS: " + AppPropsBean.getPropsVO().getWssdmWssoapUrl());
                    log.error(ex);
                    mensajeLogActivity = "[Respuesta de SF cambio de status]\nERROR: al consumir el ws de salesforce: \n";
                    mensajeLogActivity = mensajeLogActivity + ex.getMessage();
                    
                }
                
                

            } catch (Exception ex) {
                mensajeLogActivity = "[Respuesta de SF cambio de status]\n" + ex.getMessage();
                log.error("Se proujo un error al intentar procesar la transferencia", ex);
                throw new TicketCambiarEstatusException(CodeKeys.CODE_700_JAR_EXCEPTION, ex);

            } catch (Throwable ex) {
                mensajeLogActivity = "[Erro de SF al envio de grupo]\n" + ex.getMessage();
                log.error("Se proujo un error al intentar procesar la transferencia", ex);
                throw new TicketCambiarEstatusException(CodeKeys.CODE_700_JAR_THROWABLE, ex);

            } finally {

                // cerramos laconexión a la base de datos
                if (conn != null) {
                    try {
                        log.info("Intentando cerrar la conexión a la base de datos...");
                        conn.close();
                        log.info("Se cerro loa conexión a la base de datos satisfactoriamente.");
                    } catch (Throwable th) {
                        log.error("Se produjo un error al intentar cerra la conexión a la base de datos: " + th.getMessage(), th);
                        th.printStackTrace();
                    }
                }

                if (this.sid > 0) {

                    log.info("Registrando en el Log Activity el resultado de la operación");

                    log.info("Obtener el HandleForUser del usuario: " + AppPropsBean.getPropsVO().getWssdmWssoapUsuario());
                    String handleForUser = this.getHandleForUserid(sid, AppPropsBean.getPropsVO().getWssdmWssoapUsuario());
                    log.info("HandleForUser ....: " + handleForUser);
                    createActivityLog(
                            sid,
                            handleForUser,
                            updateInfoVO.getIdPersistente(),
                            mensajeLogActivity,
                            "LOG", 0, Boolean.FALSE);

                    log.info("Finalizando ejecución del programa...");

                    log.info("Intentamos cerrar la sesión el CA Service Desk");
                    this.logout(this.sid);
                    log.info("La sesión se cerro satisfactoriamente");
                } else {
                    log.error("No procede la operación ya que no se obtuvo el id de sessión correspondiente");
                }
            }

        } else {
            String error = "No se está recibiendo la información completa o correcta, por favor verifique los logs para mayor detalle.";
            log.error(error);
            throw new TicketCambiarEstatusException(error, new Exception(error));
        }
        return retorno;
    }

    @Deprecated
    private String getEstatusSalesforceFromEstatusServiceDesk(String idEstatusSDM) throws TicketCambiarEstatusException {
        String retorno = null;

        // Convertimos el estatus de SD a estatus SF
        if (idEstatusSDM != null && !idEstatusSDM.isEmpty()) {

            // Cambiamos el estatus de CA SDM a estatus Salesforce
            if (idEstatusSDM.equals("OP")) {
                retorno = "Abierto";
            } else if (idEstatusSDM.equals("AUR")
                    || idEstatusSDM.equals("ZPNDTRCR")) {
                retorno = "Nuevo";
            } else if (idEstatusSDM.equals("CNCL") || idEstatusSDM.equals("CL")) {
                retorno = "Cerrado";
            } else if (idEstatusSDM.equals("CNCL")) {
                retorno = "Cerrado";
            } else if (idEstatusSDM.equals("AWTVNDR")) {
                retorno = "En proceso";
            } else if (idEstatusSDM.equals("WIP")) {
                retorno = "Nuevo";
            } else if (idEstatusSDM.equals("RE")) {
                retorno = "Validación";
            } else {

            }
        } else {
            String error = "No se está recibiendo el valor del estatis";
            log.error(error);
            throw new TicketCambiarEstatusException(CodeKeys.CODE_140_PARAMETROS_FALTANTES, new Exception(error));
        }

        return retorno;

    }

    /**
     * Buscar la justificación del numero de ticket que se está recibiendo como
     * parámetro, pero el estatus del ticket es 'RE'
     *
     * @param numeroTicket - Folio del ticket como se muestra en el portal
     * (refnum)
     * @return
     */
    private String obtenerSolucion(String numeroTicket, Connection conn) throws TicketCambiarEstatusException, SQLException {
        String retorno = null;
        log.info("Buscar la justificación del ticket: " + numeroTicket);

        // Validmos los parámetros de entrada
        if (numeroTicket != null && !numeroTicket.isEmpty() && conn != null) {

            // Buscar la justificacion del ticket
            log.info("Buscando la justificación, ya que " + numeroTicket + " es un ticket RESUELTO.");
            String sqlBuscarJustificacion
                    = "select solucion.sym as justificacion             \n"
                    + "  from call_req as ticket,                    \n"
                    + "	      zSolucion as solucion                     \n"
                    + " where ticket.zsolucion_id = solucion.id      \n"
                    + "   and ticket.zfolio_dbw_sf is not null       \n"
                    + "   and ticket.zfolio_dbw_sf <> ''             \n"
                    + "   and ticket.status = 'RE'                   \n"
                    + "   and ticket.ref_num = ?                     \n"
                    + " order by open_date desc";
            log.info("Ejecutando el siguiente query...");
            log.info(sqlBuscarJustificacion);
            PreparedStatement psBuscarJustificacion = conn.prepareCall(sqlBuscarJustificacion);
            psBuscarJustificacion.setString(1, numeroTicket);
            ResultSet rsBuscarJustificacion = psBuscarJustificacion.executeQuery();
            if (rsBuscarJustificacion.next()) {
                retorno = rsBuscarJustificacion.getString("justificacion");
            } else {
                String error = "No se encontro justificación para el ticket: " + numeroTicket;
                log.error(error);
                throw new TicketCambiarEstatusException(CodeKeys.CODE_150_INFORMACIONNO_ENCONTRADA, new Exception(error));
            }

        } else {
            String error = "No se tienen los parámetros completos";
            log.error(error);
            throw new TicketCambiarEstatusException(CodeKeys.CODE_140_PARAMETROS_FALTANTES, new Exception(error));
        }
        return retorno;

    }

    private String obtenerDiagnostico(String numeroTicket, Connection conn) throws TicketCambiarEstatusException, SQLException {
        String retorno = null;
        log.info("Buscar el diagnóstico del ticket: " + numeroTicket);

        // Validmos los parámetros de entrada
        if (numeroTicket != null && !numeroTicket.isEmpty() && conn != null) {

            // Buscar la justificacion del ticket
            log.info("Buscando el diagnóstico, ya que " + numeroTicket + " es un ticket RESUELTO.");
            String sqlBuscarDiagnostico
                    = "select ticket.id as ticket_id, \n"
                    + "       ticket.persid as ticket_persid, \n"
                    + "	   ticket.ref_num as ticket_folio,\n"
                    + "	   ticket.zdiagnostico_id as diagnostico_clave,\n"
                    + "	   diagnostico.sym as diagnostico\n"
                    + "  from call_req as ticket,\n"
                    + "	   zDiagnostico as diagnostico\n"
                    + " where ticket.zdiagnostico_id = diagnostico.id\n"
                    + "   and ticket.zfolio_dbw_sf is not null\n"
                    + "   and ticket.zfolio_dbw_sf <> ''\n"
                    + "   and ticket.status = 'RE' \n"
                    + "   and ticket.ref_num = ?  \n"
                    + " order by open_date desc";
            log.info("Ejecutando el siguiente query...");
            log.info(sqlBuscarDiagnostico);
            PreparedStatement psBuscarDiagnostico = conn.prepareCall(sqlBuscarDiagnostico);
            psBuscarDiagnostico.setString(1, numeroTicket);
            ResultSet rsBuscarDiagnostico = psBuscarDiagnostico.executeQuery();
            if (rsBuscarDiagnostico.next()) {
                retorno = rsBuscarDiagnostico.getString("diagnostico");
            } else {
                String error = "No se encontro justificación para el ticket: " + numeroTicket;
                log.error(error);
                throw new TicketCambiarEstatusException(CodeKeys.CODE_150_INFORMACIONNO_ENCONTRADA, new Exception(error));
            }

        } else {
            String error = "No se tienen los parámetros completos";
            log.error(error);
            throw new TicketCambiarEstatusException(CodeKeys.CODE_140_PARAMETROS_FALTANTES, new Exception(error));
        }
        return retorno;

    }

    private TicketCambiarEstatusJustificacionInfoVO obtenerJustificacionPorResolucion(String persIdTicket, Connection conn) throws SQLException, TicketCambiarEstatusException {
        TicketCambiarEstatusJustificacionInfoVO retorno = null;

        // Validamos nuestros parámetros de entrada
        if (persIdTicket != null && !persIdTicket.isEmpty() && conn != null) {

            // Buscamos la información del ticket correspondiente
            // Para la parte del form, despues de asignarle el alias agregamos el contenido de " + uNoLock + "
            String sqlBuscarJustificacion
                    = "select top 1 LOGS.id as CLAVE_LOG,                \n"
                    + "       ANALISTA.contact_uuid as UUID_ANALISTA,    \n"
                    + "       TICKET.ref_num as TICKET_FOLIO,            \n"
                    + "       TICKET.persid as TICKET_PERSID,            \n"
                    + "       LOGS.system_time as FECHA_JUSTIFICACION,   \n"
                    + "       LOGS.description as JUSTIFICACION,         \n"
                    + "       ANALISTA.last_name as ANALISTA_APELLIDOS,  \n"
                    + "	  ANALISTA.first_name as ANALISTA_NOMBRE,    \n"
                    + "       TICKET.zfolio_dbw_sf as TICKET_SALESFORCE  \n"
                    + "  from act_log as LOGS " + uNoLock + ", ca_contact as ANALISTA " + uNoLock + ",   \n"
                    + "       call_req as TICKET " + uNoLock + "                        \n"
                    + " where LOGS.analyst = ANALISTA.contact_uuid       \n"
                    + "   and TICKET.persid = LOGS.call_req_id           \n"
                    + "   and LOGS.call_req_id = ?                       \n"
                    + "   and LOGS.type = 'RE'                           \n"
                    + " order by LOGS.system_time desc                     ";
            PreparedStatement psBuscarJustificacion = conn.prepareCall(sqlBuscarJustificacion);
            psBuscarJustificacion.setString(1, persIdTicket);
            log.info("Ejecutando el query:");
            log.info(sqlBuscarJustificacion);
            log.info("Buscando " + persIdTicket);
            ResultSet rsBuscarJustificacion = psBuscarJustificacion.executeQuery();

            // Validamos si resolvió un registro
            retorno = null;
            if (rsBuscarJustificacion.next()) {

                retorno = new TicketCambiarEstatusJustificacionInfoVO();
                retorno.setAnalistaApellidos(rsBuscarJustificacion.getString("ANALISTA_APELLIDOS"));
                retorno.setAnalistaNombre(rsBuscarJustificacion.getString("ANALISTA_NOMBRE"));
                retorno.setClaveLog(rsBuscarJustificacion.getInt("CLAVE_LOG"));
                retorno.setTimeFechaJustificacion(rsBuscarJustificacion.getLong("FECHA_JUSTIFICACION"));
                retorno.setFechaJustificacion(new java.sql.Timestamp(rsBuscarJustificacion.getInt("FECHA_JUSTIFICACION")));
                retorno.setJustificacion(rsBuscarJustificacion.getString("JUSTIFICACION"));
                retorno.setTicketFolio(rsBuscarJustificacion.getString("TICKET_FOLIO"));
                retorno.setTicketPersid(rsBuscarJustificacion.getString("TICKET_PERSID"));
                retorno.setAnalistaUuid("cnt:" + rsBuscarJustificacion.getString("UUID_ANALISTA"));
                retorno.setTicketSalesForce(rsBuscarJustificacion.getString("TICKET_SALESFORCE"));

            } else {
                // No se encontró información
                String error = "No se encontró información de justificación para el ticket persid: " + persIdTicket;
                log.error("   ::: " + error);
                retorno = new TicketCambiarEstatusJustificacionInfoVO();
                retorno.setJustificacion("NE");

                // throw new TicketCambiarEstatusException(CodeKeys.CODE_150_INFORMACIONNO_ENCONTRADA, new Exception(error));
            }

        } else {
            String error = "No se está recibiendo el numero de ticket a procesar.";
            log.error(error);
            throw new TicketCambiarEstatusException(CodeKeys.CODE_150_INFORMACIONNO_ENCONTRADA, new Exception(error));

        }
        return retorno;
    }
    
    private TicketCambiarEstatusJustificacionInfoVO obtenerJustificacionPorCambioEstado(String persIdTicket, Connection conn) throws SQLException, TicketCambiarEstatusException {
        TicketCambiarEstatusJustificacionInfoVO retorno = null;

        // Validamos nuestros parámetros de entrada
        if (persIdTicket != null && !persIdTicket.isEmpty() && conn != null) {

            // Buscamos la información del ticket correspondiente
            // Para la parte del form, despues de asignarle el alias agregamos el contenido de " + uNoLock + "
            String sqlBuscarJustificacion
                    = "select top 1 LOGS.id as CLAVE_LOG,                \n"
                    + "       ANALISTA.contact_uuid as UUID_ANALISTA,    \n"
                    + "       TICKET.ref_num as TICKET_FOLIO,            \n"
                    + "       TICKET.persid as TICKET_PERSID,            \n"
                    + "       LOGS.system_time as FECHA_JUSTIFICACION,   \n"
                    + "       LOGS.description as JUSTIFICACION,         \n"
                    + "       ANALISTA.last_name as ANALISTA_APELLIDOS,  \n"
                    + "	  ANALISTA.first_name as ANALISTA_NOMBRE,        \n"
                    + "       TICKET.zfolio_dbw_sf as TICKET_SALESFORCE  \n"
                    + "  from act_log as LOGS " + uNoLock + ",           \n "
                    + "       ca_contact as ANALISTA " + uNoLock + ",    \n"
                    + "       call_req as TICKET " + uNoLock + "         \n"
                    + " where LOGS.analyst = ANALISTA.contact_uuid       \n"
                    + "   and TICKET.persid = LOGS.call_req_id           \n"
                    + "   and LOGS.call_req_id = ?                       \n"
                    + "   and LOGS.type = 'ST'                           \n"
                    + " order by LOGS.system_time desc                     ";
            PreparedStatement psBuscarJustificacion = conn.prepareCall(sqlBuscarJustificacion);
            psBuscarJustificacion.setString(1, persIdTicket);
            log.info("Ejecutando el query:");
            log.info(sqlBuscarJustificacion);
            log.info("Buscando " + persIdTicket);
            ResultSet rsBuscarJustificacion = psBuscarJustificacion.executeQuery();

            // Validamos si resolvió un registro
            retorno = null;
            if (rsBuscarJustificacion.next()) {

                retorno = new TicketCambiarEstatusJustificacionInfoVO();
                retorno.setAnalistaApellidos(rsBuscarJustificacion.getString("ANALISTA_APELLIDOS"));
                retorno.setAnalistaNombre(rsBuscarJustificacion.getString("ANALISTA_NOMBRE"));
                retorno.setClaveLog(rsBuscarJustificacion.getInt("CLAVE_LOG"));
                retorno.setTimeFechaJustificacion(rsBuscarJustificacion.getLong("FECHA_JUSTIFICACION"));
                retorno.setFechaJustificacion(new java.sql.Timestamp(rsBuscarJustificacion.getInt("FECHA_JUSTIFICACION")));
                retorno.setJustificacion(rsBuscarJustificacion.getString("JUSTIFICACION"));
                retorno.setTicketFolio(rsBuscarJustificacion.getString("TICKET_FOLIO"));
                retorno.setTicketPersid(rsBuscarJustificacion.getString("TICKET_PERSID"));
                retorno.setAnalistaUuid("cnt:" + rsBuscarJustificacion.getString("UUID_ANALISTA"));
                retorno.setTicketSalesForce(rsBuscarJustificacion.getString("TICKET_SALESFORCE"));

            } else {
                // No se encontró información
                String error = "No se encontró información de justificación para el ticket persid: " + persIdTicket;
                log.error("   ::: " + error);
                retorno = new TicketCambiarEstatusJustificacionInfoVO();
                retorno.setJustificacion("NE");

                // throw new TicketCambiarEstatusException(CodeKeys.CODE_150_INFORMACIONNO_ENCONTRADA, new Exception(error));
            }

        } else {
            String error = "No se está recibiendo el numero de ticket a procesar.";
            log.error(error);
            throw new TicketCambiarEstatusException(CodeKeys.CODE_150_INFORMACIONNO_ENCONTRADA, new Exception(error));

        }
        return retorno;
    }    

    private TicketCambiarEstatusJustificacionInfoVO obtenerJustificacionPorCancelarCierre(String persIdTicket, Connection conn) throws SQLException, TicketCambiarEstatusException {
        TicketCambiarEstatusJustificacionInfoVO retorno = null;

        // Validamos nuestros parámetros de entrada
        if (persIdTicket != null && !persIdTicket.isEmpty() && conn != null) {

            // Buscamos la información del ticket correspondiente
            // Para la parte del form, despues de asignarle el alias agregamos el contenido de " + uNoLock + "
            String sqlBuscarJustificacion
                    = "select top 1 LOGS.id as CLAVE_LOG,                \n"
                    + "       ANALISTA.contact_uuid as UUID_ANALISTA,    \n"
                    + "       TICKET.ref_num as TICKET_FOLIO,            \n"
                    + "       TICKET.persid as TICKET_PERSID,            \n"
                    + "       LOGS.system_time as FECHA_JUSTIFICACION,   \n"
                    + "       LOGS.description as JUSTIFICACION,         \n"
                    + "       ANALISTA.last_name as ANALISTA_APELLIDOS,  \n"
                    + "	  ANALISTA.first_name as ANALISTA_NOMBRE,        \n"
                    + "       TICKET.zfolio_dbw_sf as TICKET_SALESFORCE  \n"
                    + "  from act_log as LOGS " + uNoLock + ",           \n "
                    + "       ca_contact as ANALISTA " + uNoLock + ",    \n"
                    + "       call_req as TICKET " + uNoLock + "         \n"
                    + " where LOGS.analyst = ANALISTA.contact_uuid       \n"
                    + "   and TICKET.persid = LOGS.call_req_id           \n"
                    + "   and LOGS.call_req_id = ?                       \n"
                    + "   and LOGS.type in ('CL','CNCL')                 \n"
                    + " order by LOGS.system_time desc                     ";
            PreparedStatement psBuscarJustificacion = conn.prepareCall(sqlBuscarJustificacion);
            psBuscarJustificacion.setString(1, persIdTicket);
            log.info("Ejecutando el query:");
            log.info(sqlBuscarJustificacion);
            log.info("Buscando " + persIdTicket);
            ResultSet rsBuscarJustificacion = psBuscarJustificacion.executeQuery();

            // Validamos si resolvió un registro
            retorno = null;
            if (rsBuscarJustificacion.next()) {

                retorno = new TicketCambiarEstatusJustificacionInfoVO();
                retorno.setAnalistaApellidos(rsBuscarJustificacion.getString("ANALISTA_APELLIDOS"));
                retorno.setAnalistaNombre(rsBuscarJustificacion.getString("ANALISTA_NOMBRE"));
                retorno.setClaveLog(rsBuscarJustificacion.getInt("CLAVE_LOG"));
                retorno.setTimeFechaJustificacion(rsBuscarJustificacion.getLong("FECHA_JUSTIFICACION"));
                retorno.setFechaJustificacion(new java.sql.Timestamp(rsBuscarJustificacion.getInt("FECHA_JUSTIFICACION")));
                retorno.setJustificacion(rsBuscarJustificacion.getString("JUSTIFICACION"));
                retorno.setTicketFolio(rsBuscarJustificacion.getString("TICKET_FOLIO"));
                retorno.setTicketPersid(rsBuscarJustificacion.getString("TICKET_PERSID"));
                retorno.setAnalistaUuid("cnt:" + rsBuscarJustificacion.getString("UUID_ANALISTA"));
                retorno.setTicketSalesForce(rsBuscarJustificacion.getString("TICKET_SALESFORCE"));

            } else {
                // No se encontró información
                String error = "No se encontró información de justificación para el ticket persid: " + persIdTicket;
                log.error("   ::: " + error);
                retorno = new TicketCambiarEstatusJustificacionInfoVO();
                retorno.setJustificacion("NE");

                // throw new TicketCambiarEstatusException(CodeKeys.CODE_150_INFORMACIONNO_ENCONTRADA, new Exception(error));
            }

        } else {
            String error = "No se está recibiendo el numero de ticket a procesar.";
            log.error(error);
            throw new TicketCambiarEstatusException(CodeKeys.CODE_150_INFORMACIONNO_ENCONTRADA, new Exception(error));

        }
        return retorno;
    }    
    
    
    private AreaResolutoraInfoVO obtenerAreaResolutora(String persIdTicket, String claveAreaResolutora, Connection conn) throws SQLException, TicketCambiarEstatusException {

        AreaResolutoraInfoVO retorno = null;

        /**
         * BUSCAR EL AREA RESOLUTORIA DESDE EL ULTIMO LOG ACTIVITY DE
         * TRANSFERENCIA *
         */
        log.info("   ::: Buscar area resolutoria anterior e inmediata en el log de comentarios");
        log.info("   ::: En descripción buscar que diga: 'Transferir grupo de'.");
        log.info("   ::: del ticket " + persIdTicket);
        String sqlBuscarDescripcion
                = "SELECT top 1 description \n"
                + "  FROM act_log \n"
                + " WHERE description like '%Transferir grupo de%'\n"
                + "   AND type = 'TR'\n"
                + "   AND call_req_id = ?     \n"
                + " ORDER BY SYSTEM_TIME DESC   ";
        log.debug("   ::: SQL EJECUTAR");
        log.debug(sqlBuscarDescripcion);

        // Ejecutar consulta
        PreparedStatement psBuscarDescripcion = conn.prepareCall(sqlBuscarDescripcion);
        psBuscarDescripcion.setString(1, persIdTicket);
        ResultSet rsBuscarDescripcion = psBuscarDescripcion.executeQuery();

        // Validamos el resultado de la consulta
        if (!rsBuscarDescripcion.next()) {
            String warn = "No se encontró log activity que nos pudiera proporcionar area resolutora. Buscar por clave.";
            log.warn("   ::: " + warn);
        } else {

            // Parseamos la descripción del log activity que se encontro
            // y extraemos el fragmento que corresponde al nombre de la area resolutora
            String descripcion = rsBuscarDescripcion.getString("description");
            log.info("   ::: Se encontro el reciente log: " + descripcion);
            log.info("   ::: Extraer el grupo origen: ");
            int posInicio = descripcion.indexOf("' a '");
            if (posInicio >= 0) {
                int posFinal = descripcion.indexOf("'", posInicio + 5);
                if (posFinal >= 0) {
                    posInicio = posInicio + 5;

                    // Obtenemos el nombre de la area resolutora
                    log.info("   ::: de la posicion " + posInicio + " hasta la posición " + posFinal);
                    String nombreAreaResolutoria = descripcion.substring(posInicio, posFinal);
                    log.info("   ::: Area Resolutoria: " + nombreAreaResolutoria);

                    // Buscar el area resolutora en el last_name de la tabla de contactos
                    log.info("   ::: Buscar area: " + nombreAreaResolutoria + " en el catálogo de los contactos 'ca_contact'");
                    String sqlBuscarAreaResolutora
                            = "select top 1 "
                            + "       AREA_RESOLUTORA.contact_uuid as CLAVE_AREA_RESOLUTORA,\n"
                            + "       AREA_RESOLUTORA.inactive as ELIMINADO,\n"
                            + "	      AREA_RESOLUTORA.last_name as DESCRIPCION,\n"
                            + "	      AREA_RESOLUTORA.alias as SISTEMA\n"
                            + "  from ca_contact as AREA_RESOLUTORA  " + uNoLock + "  \n"
                            + " where last_name = ?" + "\n"
                            + " order by creation_date desc ";
                    PreparedStatement psBuscarAreaResolutoria = conn.prepareCall(sqlBuscarAreaResolutora);
                    psBuscarAreaResolutoria.setString(1, nombreAreaResolutoria);
                    ResultSet rsBuscarAreaResolutoria = psBuscarAreaResolutoria.executeQuery();

                    if (!rsBuscarAreaResolutoria.next()) {
                        log.info("No se encontró el area Resolutora de nombre: " + nombreAreaResolutoria + " en el catálogo correspondiente.");
                    } else {
                        // Exraer el area reesolutoria
                        retorno = new AreaResolutoraInfoVO();
                        retorno.setClaveAreaResolutora(rsBuscarAreaResolutoria.getString("CLAVE_AREA_RESOLUTORA"));
                        retorno.setDescripcion(rsBuscarAreaResolutoria.getString("DESCRIPCION"));
                        retorno.setEliminado(rsBuscarAreaResolutoria.getInt("ELIMINADO"));
                        retorno.setSistema(rsBuscarAreaResolutoria.getString("SISTEMA"));
                        log.info("Area resolutora obtenida: " + retorno.toString());
                    }

                } else {
                    log.info("No se encontró area resolutoria por logAct.");
                }
            } else {
                log.info("No se encontró area resolutoria por logAct.");
            }
        }

        // Si no se pudo extraer el area resolucota desde la descripción del log de transferencia
        // tomamos el area resolutora del group_id del ticket, y buscamos su información en caso
        // de existir valor en este campo groupId
        if (claveAreaResolutora != null && !claveAreaResolutora.isEmpty() && retorno == null) {

            log.info("   ::: Buscar Area Resolutora con la clave: " + claveAreaResolutora);
            String sqlBuscarAreaResolutora
                    = "select TOP 1"
                    + "       AREA_RESOLUTORA.contact_uuid as CLAVE_AREA_RESOLUTORA,\n"
                    + "       AREA_RESOLUTORA.inactive as ELIMINADO,\n"
                    + "	  AREA_RESOLUTORA.last_name as DESCRIPCION,\n"
                    + "	  AREA_RESOLUTORA.alias as SISTEMA\n"
                    + "  from ca_contact as AREA_RESOLUTORA  " + uNoLock + "  \n"
                    + " where contact_uuid = 0x" + claveAreaResolutora;
            PreparedStatement psBuscarAreaResolutora = conn.prepareCall(sqlBuscarAreaResolutora);
            log.info("Query a ejecutar:");
            log.info(sqlBuscarAreaResolutora);
            // psBuscarAreaResolutora.setString(1, vo.getClaveAreaResolutora());
            ResultSet rsBuscarAreaResultora = psBuscarAreaResolutora.executeQuery();
            if (rsBuscarAreaResultora.next()) {

                // Cargamos la información de la area resolutora
                retorno = new AreaResolutoraInfoVO();
                retorno.setClaveAreaResolutora(rsBuscarAreaResultora.getString("CLAVE_AREA_RESOLUTORA"));
                retorno.setDescripcion(rsBuscarAreaResultora.getString("DESCRIPCION"));
                retorno.setEliminado(rsBuscarAreaResultora.getInt("ELIMINADO"));
                retorno.setSistema(rsBuscarAreaResultora.getString("SISTEMA"));

            } else {
                // NO existe la solucion
                String mensaje = "No se encontró la información de la Area Resolutora ID: " + claveAreaResolutora;
                log.error(mensaje);
                throw new TicketCambiarEstatusException(CodeKeys.CODE_150_INFORMACIONNO_ENCONTRADA, new Exception(mensaje));
            }
        } else {
            if (retorno == null) {
                String mensaje = "El registro de la solución del ticket no tiene asignada ninguna area resolutora !!";
                log.warn(mensaje);
            }
        }

        return retorno;
    }

    /**
     * CLIENTES PARA EL WEB SERVICE DE CA SDM
     */
    private static int login(java.lang.String username, java.lang.String password) {
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService service = new mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService(urlSD);
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.login(username, password);
    }

    private static void logout(int sid) {
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService service = new mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService(urlSD);
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        port.logout(sid);
    }

    private static String createActivityLog(int sid, java.lang.String creator, java.lang.String objectHandle, java.lang.String description, java.lang.String logType, int timeSpent, boolean internal) {
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService service = new mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService(urlSD);
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.createActivityLog(sid, creator, objectHandle, description, logType, timeSpent, internal);
    }

    private static String getHandleForUserid(int sid, java.lang.String userID) {
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService service = new mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService(urlSD);
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.getHandleForUserid(sid, userID);
    }

}
