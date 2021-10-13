package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.transferencia;

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
public class TicketTransferenciaActualizarComentarioBean extends CoreBean {

    // Conmstantes de la clase
    private static final Category log = Category.getInstance(TicketTransferenciaActualizarComentarioBean.class);
    private static final String COMENT_BUSCAR_UNO = "Respuesta de SF al envio de cambio de grupo".trim().toLowerCase();
    private static final String COMENT_BUSCAR_DOS = "Respuesta de SF al envió de cambio de grupo".trim().toLowerCase();

    public TicketTransferenciaRetornoVO actualizarComentarioTransferencia(String persIdTicketSD, String nuevaDesk) throws TicketTransferenciaException {

        log.info("   ::: Actualizar comentario de transferencia para del ticket: " + persIdTicketSD);

        TicketTransferenciaRetornoVO retorno = null;

        if (persIdTicketSD != null && !persIdTicketSD.isEmpty()) {

            // Inicializamos el elemento de retorno
            retorno = new TicketTransferenciaRetornoVO();

            // Definir la conexión a la base de datos
            Connection conn = null;
            try {

                // Conectar a la base de datos correspondiente
                log.info("Obteniendo la conexión a la base de datos...");
                conn = ConnectorBDDConsultasBean.getConectionServiceDesk();
                log.info("La conexión a la base de datos es correcta");

                log.info("Buscar el comentario de la transferencia...");
                String sqlBuscarComentarioTransferencia
                        = "select top 100 id, persid, call_req_id, description \n"
                        + "  from act_log " + ApplicationKeys.TABLE_WITH_NOLOCK + "  \n"
                        + " where call_req_id = ?                              \n"
                        + " order by id desc                                     ";
                log.info(sqlBuscarComentarioTransferencia);
                PreparedStatement psBuscarComentarioTransferencia = conn.prepareCall(sqlBuscarComentarioTransferencia);
                psBuscarComentarioTransferencia.setString(1, persIdTicketSD);
                log.info("Actualizar...");
                ResultSet rsBuscarComentarioTransferencia = psBuscarComentarioTransferencia.executeQuery();
                String description = null;
                while(rsBuscarComentarioTransferencia.next()){
                    
                    // Buscamos el log que contenga
                    description = rsBuscarComentarioTransferencia.getString("description");
                    log.info("Validando para: " + description);
                    
                    if(description.trim().toLowerCase().contains(COMENT_BUSCAR_UNO)
                            || description.trim().toLowerCase().contains(COMENT_BUSCAR_DOS)){
                        
                        log.info("Aplicando cambios...");
                        // Aqui actualizamos el campo de description
                        String sqlActualizarDescription
                                = "update act_log"
                                + "   set description = ? "
                                + " where id = ?          "
                                + "   and persid = ?      ";
                        PreparedStatement psActualizarDescription = conn.prepareCall(sqlActualizarDescription);
                        psActualizarDescription.setString(1, nuevaDesk);
                        psActualizarDescription.setInt(2, rsBuscarComentarioTransferencia.getInt("id"));
                        psActualizarDescription.setString(3, rsBuscarComentarioTransferencia.getString("persid"));
                        psActualizarDescription.executeUpdate();
                        
                        log.info("Actualizado satisfactorio");
                        
                        log.info("Confirmando cambios (commit)...");
                        conn.commit();
                        
                        break;
                        
                    }
                }

            } catch (Exception ex) {
                log.error(ex);
                throw new TicketTransferenciaException(CodeKeys.CODE_700_JAR_EXCEPTION, ex);

            } catch (Throwable th) {
                log.error(th);
                throw new TicketTransferenciaException(CodeKeys.CODE_700_JAR_THROWABLE, th);

            } finally {
                log.info("Finalizando método...");

                if (conn != null) {
                    log.info("Cerrando la conexión a la base de datos....");
                    try {
                        conn.close();
                        log.info("La conexión a la base de datos se cerro satisfactoriamente.");

                    } catch (Exception ex) {
                        log.error(ex);

                    } catch (Throwable th) {
                        log.error(th);
                    }
                }
            }

        } else {
            String error = "No se está recibiendo la información del ticket correspondiente.";
            log.error(error);
            throw new TicketTransferenciaException(CodeKeys.CODE_140_PARAMETROS_FALTANTES, new Exception(error));
        }
        return retorno;
    }

}
