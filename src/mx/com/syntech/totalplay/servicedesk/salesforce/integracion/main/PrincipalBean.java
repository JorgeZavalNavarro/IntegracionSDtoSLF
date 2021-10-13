
package mx.com.syntech.totalplay.servicedesk.salesforce.integracion.main;

import com.totalplay.syntech.integracion.sdm.soainfra.wssdm.props.AppPropsBean;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.keys.ApplicationKeys;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.cambiarestatus.TicketCambiarEstatusBean;
import mx.com.syntech.totalplay.servicedesk.salesforce.integracion.ticket.transferencia.TicketTransferenciaBean;
import org.apache.log4j.Category;

/**
 * Este programa suple dos funcionalidades del proyecto de NetBeans SD_SF cuyo
 * nombre original era: Integracion_SDtoSF_local desarrollado por un proveedor
 * desconocido Se van a traspasar las siguientes funcionalidades
 *
 * @author dell
 */
public class PrincipalBean {

    // Constantes de la clase
    private static final Category log = Category.getInstance(PrincipalBean.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    
        try {
            System.out.println("Iniciando sistema ...");
            AppPropsBean.getPropsVO().getPathConfigLogs();

            if (args != null && args.length > 0) {

                log.info("Verificando la operación a ejecutar...");
                
                log.info("Parámetros de entrada...");
                for(int i=0; i<args.length; i++){
                    log.info("Param[" + i + "] = " + args[i]);
                }
                String charSet = "UTF-8";
                log.info("Configurando charset: " + charSet);
                
                asignarCharSet(charSet);

                switch (args[0]) {
                    case ("" + ApplicationKeys.TRANSFERENCIA):{ // ID: 500
                        // Aplicamos para la transferencia del ticket o cambio de grupo
                        TicketTransferenciaBean transferenciaBean = new TicketTransferenciaBean(args);
                        transferenciaBean.procesar();
                        break;

                    }
                    
                    case ("" + ApplicationKeys.CHANGE_STATUS):{ // ID: 400
                        // Aplicamos para la transferencia del ticket o cambio de grupo
                        TicketCambiarEstatusBean cambioEstatusBean = new TicketCambiarEstatusBean(args);
                        cambioEstatusBean.procesar();
                        break;

                    }
                }

            } else {
                log.error("No se está recibiendo ningun parámetro para procesar.");
            }

        } catch (Exception ex) {
            log.error("Error: " + ex.getMessage(), ex);
            ex.printStackTrace();
            
        } catch (Throwable ex) {
            log.error("Error: " + ex.getMessage(), ex);
            ex.printStackTrace();
        }

    }
    
    public static void asignarCharSet(String charset) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        log.info("   ::: Charset actual: " + Charset.defaultCharset().name());
        log.info("   ::: File encoding actual: " + System.getProperty("file.encoding"));
        log.info("   ::: Asignando el charset: " + charset);
        System.setProperty("file.encoding", charset);
        
        log.info("   ::: File encoding actual: " + System.getProperty("file.encoding"));

        Field fieldCharset = Charset.class.getDeclaredField("defaultCharset");
        fieldCharset.setAccessible(true);
        fieldCharset.set(null, null);
        log.info("   ::: Charset actual: " + Charset.defaultCharset().name());

    }
    
}
