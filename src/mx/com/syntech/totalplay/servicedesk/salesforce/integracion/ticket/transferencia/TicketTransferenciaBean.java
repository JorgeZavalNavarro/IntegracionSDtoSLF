package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.transferencia;

import com.totalplay.syntech.integracion.sdm.soainfra.wssdm.props.AppPropsBean;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.connbdd.ConnectorBDDConsultasBean;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.core.CoreBean;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.keys.ApplicationKeys;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.keys.CodeKeys;
import org.apache.log4j.Category;

/**
 *
 * @author dell
 */
public class TicketTransferenciaBean extends CoreBean {

    // Constantes de la clase
    private static final Category log = Category.getInstance(TicketTransferenciaBean.class);

    private URL urlSD = null;

    // variables donse de asignan los par{ametros de entrada
    // "500" "persistent_id" "cr:1645437" "ref_num" "1222800" "zfolio_dbw_sf" "58484529" "log_agent" "Integracion SF Dashboard Web" "group" "SOPORTE ACCESO GPON TPE N3"
    private String persistent_id = null;    // ID persistente del ticket
    private String ref_num = null;          // Número de Folio o Ticket en service desk
    private String log_agent = null;        // Log Agent
    private String zfolio_dbw_sf = null;    // Folio del ticket en service desk
    private String group = null;            // Clave del producto

    // Variables de uso general de la clase
    private int sid = 0;

    /**
     * M´wetodo constructor que se va a encargar de extraer los parámetros que
     * se reciben como parámetro. Todos los parametros se deben de validar Los
     * para,etros extraidos del arreglo se asignar a variables de la clase
     *
     * @param params
     * @throws MalformedURLException
     */
    public TicketTransferenciaBean(String... params) throws TicketTransferenciaException {
        log.info("Iniciando transferencia ...");
        seeArgs(params);

        try {
            // Obtener las propiedades a utilizar
            log_agent = this.getValorArgumentos(params, "log_agent").trim();
            zfolio_dbw_sf = this.getValorArgumentos(params, "zfolio_dbw_sf").trim();
            group = this.getValorArgumentos(params, "group").trim();
            persistent_id = this.getValorArgumentos(params, "persistent_id").trim();
            ref_num = this.getValorArgumentos(params, "ref_num").trim();

            log.info("Procesando para los siguientes datos: ");
            log.info("log_agent .......: " + log_agent);
            log.info("zfolio_dbw_sf ...: " + zfolio_dbw_sf);
            log.info("group: ..........: " + group);
            log.info("persistent_id: ..: " + persistent_id);
            log.info("ref_num: ........: " + ref_num);

            log.info("Inicializamos la URL: " + AppPropsBean.getPropsVO().getWssdmWssoapUrl());
            this.urlSD = new URL(AppPropsBean.getPropsVO().getWssdmWssoapUrl());

            // 
        } catch (Exception ex) {
            String error = "Se produjo un error al intentar realizar la transferencia.";
            log.error(error);
            throw new TicketTransferenciaException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, new Exception(error));
        }

    }

    public TicketTransferenciaRetornoVO procesar() throws TicketTransferenciaException {
        TicketTransferenciaRetornoVO retorno = null;
        String mensajeLogActivity = "";
        Connection conn = null;
        try {

            // Conectando a la base de datos
            log.info("Realizando conexión a la base de datos...");
            conn = ConnectorBDDConsultasBean.getConectionServiceDesk();
            log.info("La conexión a la base de datos es correcta.");

            /**
             * VALIDANDO LA INFORMACIÓN A PROCESAR
             */
            // Validamos que el LogAgent sea un valor correcto
            log.info("Validar la información de log_agent...");
            if (log_agent != null && !log_agent.equals(TicketTransferenciaKeys.LOG_AGENT_VALIDO)) {
                String error = "No se puede realizar la operación. log_gent es inválido.";
                log.error(error);
                throw new TicketTransferenciaException(CodeKeys.CODE_130_VALOR_INCORRECTO, new Exception(error));
            }

            // Logeamos el usuario configurado para validar el permiso correspondiente
            sid = this.login(
                    AppPropsBean.getPropsVO().getWssdmWssoapUsuario(),
                    AppPropsBean.getPropsVO().getWssdmWssoapPassword());

            if (sid < 2000) {
                String error = "Error en el sistema. Verifique que el servicio esta completamente activo";
                log.error(error);
                throw new TicketTransferenciaException(CodeKeys.CODE_210_SERVICE_DESK_UNREACHABLE, new Exception(error));

            }

            // Buscar el comentario que se agregao al momento de transferir el documento
            // Hay que buscar en los logs activity el ultimo registro del tipo transferencia
            // correspopndiente a este persid
            String sqlBuscarComentarioTransferencia
                    = "select top 1 logs.id as log_id,\n"
                    + "       logs.persid as log_persid,\n"
                    + "	      logs.description as log_descripcion\n"
                    + "  from act_log as logs  " + ApplicationKeys.TABLE_WITH_NOLOCK + "\n"
                    + " where logs.type = 'TR'   \n"
                    + "   and call_req_id = ?    \n"
                    + " order by logs.id desc";
            PreparedStatement psBuscarComentarioTransferencia = conn.prepareCall(sqlBuscarComentarioTransferencia);
            psBuscarComentarioTransferencia.setString(1, this.persistent_id);
            ResultSet rsBuscarComentarioTransferencia = psBuscarComentarioTransferencia.executeQuery();
            if (rsBuscarComentarioTransferencia.next()) {
                // Cargamos la información de la descripción del activity log y ejecutamos el ws de tpe
                String comentario = rsBuscarComentarioTransferencia.getString("log_descripcion");
                log.info("Se encontró el siguiente comentario: " + comentario);

                log.info("Invocar servicio web " + AppPropsBean.getPropsVO().getWstpeCommentUrl());
                String retornoWs = TicketTransferenciaClientWSSalesgorceBean.postComment(zfolio_dbw_sf, comentario, group);

                mensajeLogActivity = "[Respuesta de SF al envio de cambio de grupo]\n" + retornoWs;

            } else {
                // No se encontró el persistent_id del ticket
                String error = "No se encontró información del ticket: " + ref_num;
                log.error(error);
                throw new TicketTransferenciaException(CodeKeys.CODE_130_VALOR_INCORRECTO, new Exception(error));
            }

        } catch (Exception ex) {
            mensajeLogActivity = "[Erro de SF al envio de grupo]\n" + ex.getMessage();
            log.error("Se proujo un error al intentar procesar la transferencia", ex);
            throw new TicketTransferenciaException(CodeKeys.CODE_700_JAR_EXCEPTION, ex);

        } catch (Throwable ex) {
            mensajeLogActivity = "[Erro de SF al envio de grupo]\n" + ex.getMessage();
            log.error("Se proujo un error al intentar procesar la transferencia", ex);
            throw new TicketTransferenciaException(CodeKeys.CODE_700_JAR_THROWABLE, ex);

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

                /**
                 * MODIFICACIÓN. Se va a rastrear el comentario correspondiente
                 * a la ultima transferencia realizada. se ddebe de buscar el
                 * siguiente texto: [Respuesta de SF al envio de cambio de
                 * grupo] en la descripción del comentario segun el ticket
                 */
                log.info("Actualizamos el comentario correspondiente a esta transferencia");
                TicketTransferenciaActualizarComentarioBean actualizarComentarioBean = new TicketTransferenciaActualizarComentarioBean();
                actualizarComentarioBean.actualizarComentarioTransferencia(persistent_id, mensajeLogActivity);

                /**
                log.info("Obtener el HandleForUser del usuario: " + AppPropsBean.getPropsVO().getWssdmWssoapUsuario());
                String handleForUser = this.getHandleForUserid(sid, AppPropsBean.getPropsVO().getWssdmWssoapUsuario());
                createActivityLog(
                        sid,
                        handleForUser,
                        persistent_id,
                        mensajeLogActivity,
                        "LOG", 0, Boolean.FALSE);
                **/
                
                log.info("Refrescando cache...");
                this.ejecutarPDM();
                
                log.info("Finalizando ejecución del programa...");

                log.info("Intentamos cerrar la sesión el CA Service Desk");
                this.logout(this.sid);
                log.info("La sesión se cerro satisfactoriamente");
            }
        }

        return retorno;
    }
    
    
    public void ejecutarPDM() {

        // Obtener el nombre del comando a ejecutar
        String comandoEjecutar = null;
        log.info("   ::: Ejecución del comando para actualizar la información de los adjuntos...");

        try {

            comandoEjecutar = "pdm_cache_refresh -t Act_Log";
            // comandoEjecutar = "cmd /c dir";
            log.info("   ::: Comando a ejecutar: " + comandoEjecutar);

            // Lanzar programa (se usa "cmd /c dir" para lanzar un comando del sistema operativo)
            log.info("   ::: Ejecutando...");
            Process p = Runtime.getRuntime().exec(comandoEjecutar);

            /**
             * Obtenemos la salida del comando *
             */
            log.info("   ::: Recibieno respuesta...");
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Se lee la primera linea
            String aux = br.readLine();
            // log.info("     -->" + aux);

            // Mientras se haya leido alguna linea
            while (aux != null) {
                // Se escribe la linea en pantalla
                System.out.println(aux);
                log.info("     -->" + aux);

                // y se lee la siguiente.
                aux = br.readLine();
            }
            log.info("   ::: Ejecución terminada");

        } catch (Exception ex) {
            // Excepciones si hay algún problema al arrancar el ejecutable o al leer su salida.*/
            log.error("No se pudo ejecutar el programa: " + comandoEjecutar);
            log.error(ex.getLocalizedMessage());
            log.error(ex.getMessage());
            log.error("Trasa del error....", ex);
        }
    }

    

    private int login(java.lang.String username, java.lang.String password) {
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService service = new mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService(urlSD);
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.login(username, password);
    }

    private void logout(int sid) {
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService service = new mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService(urlSD);
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        port.logout(sid);
    }

    private String createActivityLog(int sid, java.lang.String creator, java.lang.String objectHandle, java.lang.String description, java.lang.String logType, int timeSpent, boolean internal) {
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService service = new mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService(urlSD);
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.createActivityLog(sid, creator, objectHandle, description, logType, timeSpent, internal);
    }

    private String getHandleForUserid(int sid, java.lang.String userID) {
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService service = new mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebService(urlSD);
        mx.com.syntech.totalplay.servicedesk.salesforce.integracion.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.getHandleForUserid(sid, userID);
    }

}
